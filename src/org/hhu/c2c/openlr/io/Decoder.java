package org.hhu.c2c.openlr.io;

import static org.hhu.c2c.openlr.io.PhysicalDataFormat.AREA_FLAG_BITMASK;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.ATTRIBUTE_FLAG_BITMASK;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.FORM_OF_WAY_BITMASK;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.FRC_BITSHIFT;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.FUNCTIONAL_ROAD_CLASS_BITMASK;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.LFRCNP_BITSHIFT;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.MINIMUM_NUMBER_OF_BYTES;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.MINIMUM_NUMBER_OF_BYTES_FOR_LAST_LRP;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.NEGATIVE_OFFSET_FLAG_BITMASK;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.NUMBER_OF_BYTES_FOR_ABSOLUTE_COORDINATE;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.NUMBER_OF_BYTES_FOR_ABSOLUTE_LRP;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.NUMBER_OF_BYTES_FOR_RELATIVE_COORDINATE;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.NUMBER_OF_BYTES_FOR_RELATIVE_LRP;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.POSITIVE_OFFSET_FLAG_BITMASK;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.VERSION_NUMBER_BITMASK;

import java.util.ArrayList;
import java.util.List;

import org.hhu.c2c.openlr.core.Bearing;
import org.hhu.c2c.openlr.core.Distance;
import org.hhu.c2c.openlr.core.FormOfWay;
import org.hhu.c2c.openlr.core.FunctionalRoadClass;
import org.hhu.c2c.openlr.core.LocationReference;
import org.hhu.c2c.openlr.core.LocationReferenceBuilder;
import org.hhu.c2c.openlr.core.LocationReferencePoint;
import org.hhu.c2c.openlr.core.LocationReferencePointBuilder;
import org.hhu.c2c.openlr.geo.Coordinate;
import org.hhu.c2c.openlr.geo.CoordinateFactory;
import org.hhu.c2c.openlr.geo.CoordinateUtils;
import org.hhu.c2c.openlr.util.ByteArrayFiFo;

public class Decoder {

	
	
	private CoordinateFactory coordinateFactory;
	
	public Decoder () {
		coordinateFactory = CoordinateFactory.getInstance();
	}
	
	/**
	 * Converts the given byte array into a location reference
	 * 
	 * @param bytes
	 *            the byte arra representing a location reference
	 * @return a location reference
	 * @throws IllegalArgumentException
	 *             if the byte array is malformed, containing either less or
	 *             more bytes than required
	 */
	public LocationReference read(byte[] bytes) {
		if (bytes.length < MINIMUM_NUMBER_OF_BYTES)
			throw new IllegalArgumentException(
					"Byte array too small. A valid location reference needs at least "
							+ MINIMUM_NUMBER_OF_BYTES + " bytes.");

		ByteArrayFiFo fifo = new ByteArrayFiFo(bytes);

		// read header byte
		// TODO attribute flag beachten
		// TODO area flag beachten
		// TODO versions feld untersuchen, kompabiltität prüfen
		boolean areaFlag = (fifo.peek() & AREA_FLAG_BITMASK) == AREA_FLAG_BITMASK ? true
				: false;
		boolean attributeFlag = (fifo.peek() & ATTRIBUTE_FLAG_BITMASK) == ATTRIBUTE_FLAG_BITMASK ? true
				: false;
		byte version = (byte) (fifo.pop() & VERSION_NUMBER_BITMASK);

		List<LocationReferencePoint> points = new ArrayList<LocationReferencePoint>();

		// get the first point, ts an absolute one
		points.add(getAbsoluteLRP(fifo.pop(NUMBER_OF_BYTES_FOR_ABSOLUTE_LRP)));

		// get following, relative ones, if there are any
		while (fifo.capacity() >= NUMBER_OF_BYTES_FOR_RELATIVE_LRP
				+ MINIMUM_NUMBER_OF_BYTES_FOR_LAST_LRP) {
			points.add(getLRPfromRelative(points.get(points.size())
					.getCoordinate(), fifo
					.pop(NUMBER_OF_BYTES_FOR_RELATIVE_LRP)));
		}

		// we have to prepare the last one very carfully
		Coordinate c = new Coordinate(
				CoordinateUtils.getDegreeFromRelative(CoordinateUtils
						.getRelativeCoordinateIntValue(fifo
								.pop(NUMBER_OF_BYTES_FOR_RELATIVE_COORDINATE)),
						points.get(points.size()).getCoordinate()
								.getLongitude()),
				CoordinateUtils
						.getDegreeFromRelative(
								CoordinateUtils
										.getRelativeCoordinateIntValue(fifo
												.pop(NUMBER_OF_BYTES_FOR_RELATIVE_COORDINATE)),
								points.get(points.size()).getCoordinate()
										.getLatitude()));
		FunctionalRoadClass fcr = FunctionalRoadClass
				.getFunctionalRoadClass((byte) ((fifo.peek() >> FRC_BITSHIFT) & FUNCTIONAL_ROAD_CLASS_BITMASK));
		FormOfWay fow = FormOfWay
				.getFormOfWay((byte) (fifo.pop() & FORM_OF_WAY_BITMASK));
		boolean positiveOffsetFlag = (fifo.peek() & POSITIVE_OFFSET_FLAG_BITMASK) == POSITIVE_OFFSET_FLAG_BITMASK;
		boolean negativeOffsetFlag = (fifo.peek() & NEGATIVE_OFFSET_FLAG_BITMASK) == NEGATIVE_OFFSET_FLAG_BITMASK;

		Bearing bearing = Bearing.newBearing(fifo.pop());
		Distance positiveOffset = new Distance(0);
		Distance negativeOffset = new Distance(0);

		// if pooffF is set, there should be at least one byte left
		if (positiveOffsetFlag && fifo.capacity() >= 1)
			positiveOffset = Distance.newDistance(fifo.pop());
		else
			throw new IllegalArgumentException(
					"ByteArray is malformed. Was awaiting a byte for positive offset");

		// if noffF is set, the byte for the offset must be there
		if (negativeOffsetFlag && fifo.capacity() == 1)
			negativeOffset = Distance.newDistance(fifo.pop());
		else
			throw new IllegalArgumentException(
					"ByteArray is malformed. Was awaiting a byte for negative offset");

		// if there are somy bytes left, somwthing went wrong
		if (fifo.capacity() != 0) {
			throw new IllegalArgumentException(
					"Somethign went treeribly wrong. You figure it out.");
		}
		points.add(LocationReferencePointBuilder.newLocationReferencePoint(c, fcr, fow, bearing));

		return LocationReferenceBuilder.newLocationReference(areaFlag, attributeFlag, version, points,
				positiveOffset, negativeOffset);
	}

	/**
	 * Returns a {@link LocationReferencePoint} from a given previous
	 * coordinate, and a byte representation of the current relative location
	 * reference point.
	 * 
	 * @param previous
	 *            The absolute coordinate from the prrevious location reference
	 *            point
	 * @param b
	 *            the byte array representation of the current relative location
	 *            reference point
	 * @return the current location reference point with absolut values
	 */
	private LocationReferencePoint getLRPfromRelative(Coordinate previous,
			byte[] b) {

		ByteArrayFiFo fifo = new ByteArrayFiFo(b);

		return LocationReferencePointBuilder.newLocationReferencePoint(
				new Coordinate(
						CoordinateUtils
								.getDegreeFromRelative(
										CoordinateUtils
												.getRelativeCoordinateIntValue(fifo
														.pop(NUMBER_OF_BYTES_FOR_RELATIVE_COORDINATE)),
										previous.getLongitude()),
						CoordinateUtils
								.getDegreeFromRelative(
										CoordinateUtils
												.getRelativeCoordinateIntValue(fifo
														.pop(NUMBER_OF_BYTES_FOR_RELATIVE_COORDINATE)),
										previous.getLatitude())),
				FunctionalRoadClass
						.getFunctionalRoadClass((byte) ((fifo.peek() >> FRC_BITSHIFT) & FUNCTIONAL_ROAD_CLASS_BITMASK)),
				FormOfWay
						.getFormOfWay((byte) (fifo.pop() & FORM_OF_WAY_BITMASK)),
				FunctionalRoadClass
						.getFunctionalRoadClass((byte) ((fifo.peek() >> LFRCNP_BITSHIFT) & FUNCTIONAL_ROAD_CLASS_BITMASK)),
				Bearing.newBearing(fifo.pop()), Distance
						.newDistance(fifo.pop()));
	}

	/**
	 * Returns a location reference point from a given byte array of 9 bytes
	 * representing a location reference point with an absolute coordinate and
	 * three attribute bytes
	 * 
	 * @param b
	 *            the byte representation of a location reference point with an
	 *            absolute coordinate
	 * @return the location reference point
	 */
	private LocationReferencePoint getAbsoluteLRP(byte[] b) {
		ByteArrayFiFo fifo = new ByteArrayFiFo(b);

		return LocationReferencePointBuilder.newLocationReferencePoint(
				coordinateFactory.getCoordinate(fifo
						.pop(NUMBER_OF_BYTES_FOR_ABSOLUTE_COORDINATE)),
				FunctionalRoadClass
						.getFunctionalRoadClass((byte) ((fifo.peek() >> FRC_BITSHIFT) & FUNCTIONAL_ROAD_CLASS_BITMASK)),
				FormOfWay
						.getFormOfWay((byte) (fifo.pop() & FORM_OF_WAY_BITMASK)),
				FunctionalRoadClass
						.getFunctionalRoadClass((byte) ((fifo.peek() >> LFRCNP_BITSHIFT) & FUNCTIONAL_ROAD_CLASS_BITMASK)),
				Bearing.newBearing(fifo.pop()), Distance
						.newDistance(fifo.pop()));
	}
	
}
