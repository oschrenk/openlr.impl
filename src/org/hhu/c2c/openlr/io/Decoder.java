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
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.NUMBER_OF_BYTES_FOR_RELATIVE_ANGULAR_MEASUREMENT;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.NUMBER_OF_BYTES_FOR_RELATIVE_LRP;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.POSITIVE_OFFSET_FLAG_BITMASK;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.VERSION_NUMBER_BITMASK;

import org.hhu.c2c.openlr.core.Bearing;
import org.hhu.c2c.openlr.core.Distance;
import org.hhu.c2c.openlr.core.FormOfWay;
import org.hhu.c2c.openlr.core.FunctionalRoadClass;
import org.hhu.c2c.openlr.core.LocationReference;
import org.hhu.c2c.openlr.core.LocationReferenceBuilder;
import org.hhu.c2c.openlr.core.LocationReferencePoint;
import org.hhu.c2c.openlr.core.LocationReferencePointBuilder;
import org.hhu.c2c.openlr.geo.Coordinate;
import org.hhu.c2c.openlr.l10n.Messages;
import org.hhu.c2c.openlr.util.ByteArrayFiFo;
import org.hhu.c2c.openlr.util.ValidationException;

/**
 * Used for unmarshalling a byte representation of a location reference.
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
 * 
 */
public class Decoder {

	/**
	 * Converts the given byte array into a location reference
	 * 
	 * @param bytes
	 *            the byte arra representing a location reference
	 * @return a location reference
	 * @throws ValidationException
	 *             if the location reference trying wasn't valid
	 * @throws IllegalArgumentException
	 *             if the byte array is malformed, containing either less or
	 *             more bytes than required
	 */
	public LocationReference decode(final byte[] bytes)
			throws ValidationException {
		if (bytes.length < MINIMUM_NUMBER_OF_BYTES) {
			throw new ValidationException(
					Messages
							.getString(
									"Decoder.Exception.MINIMUM_NUMBER_OF_BYTES", MINIMUM_NUMBER_OF_BYTES)); //$NON-NLS-1$
		}

		ByteArrayFiFo fifo = new ByteArrayFiFo(bytes);

		LocationReferenceBuilder lrb = new LocationReferenceBuilder();
		lrb.start();

		// read header byte
		lrb
				.setAreaFlag((fifo.peek() & AREA_FLAG_BITMASK) == AREA_FLAG_BITMASK ? true
						: false);
		lrb
				.setAttributeFlag(((fifo.peek() & ATTRIBUTE_FLAG_BITMASK) == ATTRIBUTE_FLAG_BITMASK ? true
						: false));
		lrb.setVersion((byte) (fifo.pop() & VERSION_NUMBER_BITMASK));

		// get the first point, its an absolute one
		LocationReferencePoint point = getAbsoluteLRP(fifo
				.pop(NUMBER_OF_BYTES_FOR_ABSOLUTE_LRP));
		lrb.addLocationReferencePoint(point);

		// get following, relative ones, if there are any
		while (fifo.capacity() >= NUMBER_OF_BYTES_FOR_RELATIVE_LRP
				+ MINIMUM_NUMBER_OF_BYTES_FOR_LAST_LRP) {
			point = getLRPfromRelative(point.getCoordinate(), fifo
					.pop(NUMBER_OF_BYTES_FOR_RELATIVE_LRP));
			lrb.addLocationReferencePoint(point);
		}

		// we have to prepare the last one very carfully as the last attribute
		// has info about the last point and the complete lr
		LocationReferencePointBuilder lrpb = new LocationReferencePointBuilder();
		lrpb.setCoordinate(getCoordinate(point.getCoordinate(), fifo));
		lrpb.setFrc(FunctionalRoadClass.getFunctionalRoadClass((byte) ((fifo
				.peek() >> FRC_BITSHIFT) & FUNCTIONAL_ROAD_CLASS_BITMASK)));
		lrpb.setFow(FormOfWay
				.getFormOfWay((byte) (fifo.pop() & FORM_OF_WAY_BITMASK)));

		boolean positiveOffsetFlag = (fifo.peek() & POSITIVE_OFFSET_FLAG_BITMASK) == POSITIVE_OFFSET_FLAG_BITMASK;
		boolean negativeOffsetFlag = (fifo.peek() & NEGATIVE_OFFSET_FLAG_BITMASK) == NEGATIVE_OFFSET_FLAG_BITMASK;

		lrpb.setBearing(Bearing.newBearing(fifo.pop()));
		lrb.addLocationReferencePoint(lrpb.get());

		// check if poffF is set
		if (positiveOffsetFlag) {
			// there should be one or two byte left
			if (fifo.capacity() > 0) {
				lrb.setPositiveOffset(Distance.newDistance(fifo.pop()));
			} else {
				throw new ValidationException(
						Messages
								.getString("Decoder.Exception.POSITIVE_OFFSET_NOT_FOUND")); //$NON-NLS-1$
			}
		}

		// if noffF is set, the byte for the offset must be there
		if (negativeOffsetFlag) {
			// there should only be one byte left
			if (fifo.capacity() > 0) {
				lrb.setNegativeOffset(Distance.newDistance(fifo.pop()));
			} else {
				throw new ValidationException(
						Messages
								.getString("Decoder.Exception.NEGATIVE_OFFSET_NOT_FOUND")); //$NON-NLS-1$
			}
		}

		// if there are somy bytes left, something went wrong
		if (fifo.capacity() != 0) {
			throw new ValidationException(Messages
					.getString("Decoder.Exception.BYTES_NOT_EXHAUSTED")); //$NON-NLS-1$
		}

		return lrb.get();
	}

	/**
	 * Returns a location reference point from a given byte array of 9 bytes
	 * representing a location reference point with an absolute coordinate and
	 * three attribute bytes
	 * 
	 * @param point
	 *            the byte representation of a location reference point with an
	 *            absolute coordinate
	 * @return the location reference point
	 * @throws ValidationException
	 *             if the location reference point trying to build couldn't be
	 *             validated
	 */
	private LocationReferencePoint getAbsoluteLRP(final byte[] point)
			throws ValidationException {
		LocationReferencePointBuilder lrpb = new LocationReferencePointBuilder();
		ByteArrayFiFo fifo = new ByteArrayFiFo(point);

		lrpb.start();
		lrpb.setCoordinate(CoordinateHelper.getCoordinate(fifo
				.pop(NUMBER_OF_BYTES_FOR_ABSOLUTE_COORDINATE)));
		lrpb.setFrc(FunctionalRoadClass.getFunctionalRoadClass((byte) ((fifo
				.peek() >> FRC_BITSHIFT) & FUNCTIONAL_ROAD_CLASS_BITMASK)));
		lrpb.setFow(FormOfWay
				.getFormOfWay((byte) (fifo.pop() & FORM_OF_WAY_BITMASK)));
		lrpb.setLfrcnp(FunctionalRoadClass.getFunctionalRoadClass((byte) ((fifo
				.peek() >> LFRCNP_BITSHIFT) & FUNCTIONAL_ROAD_CLASS_BITMASK)));
		lrpb.setBearing(Bearing.newBearing(fifo.pop()));
		lrpb.setDnp(Distance.newDistance(fifo.pop()));
		return lrpb.get();
	}

	/**
	 * Returns a new absolute {@link Coordinate} from a given previous
	 * coordinate, and a byte representation of the current relative location
	 * reference point.
	 * 
	 * @param previous
	 *            the coordinate of the previous location reference point
	 * @param fifo
	 *            the {@link ByteArrayFiFo} cholding the current location
	 *            reference point
	 * @return the absolute coordinate of the current location reference point
	 */
	private Coordinate getCoordinate(final Coordinate previous,
			final ByteArrayFiFo fifo) {
		return new Coordinate(
				CoordinateHelper
						.getDegreeFromRelative(
								CoordinateHelper
										.getRelativeCoordinateIntValue(fifo
												.pop(NUMBER_OF_BYTES_FOR_RELATIVE_ANGULAR_MEASUREMENT)),
								previous.getLongitude()),
				CoordinateHelper
						.getDegreeFromRelative(
								CoordinateHelper
										.getRelativeCoordinateIntValue(fifo
												.pop(NUMBER_OF_BYTES_FOR_RELATIVE_ANGULAR_MEASUREMENT)),
								previous.getLatitude()));
	}

	/**
	 * Returns a {@link LocationReferencePoint} from a given previous
	 * coordinate, and a byte representation of the current relative location
	 * reference point.
	 * 
	 * @param previous
	 *            The absolute coordinate from the prrevious location reference
	 *            point
	 * @param currentPoint
	 *            the byte array representation of the current relative location
	 *            reference point
	 * @return the current location reference point with absolut values
	 * @throws ValidationException
	 *             if the location reference point trying to build couldn't be
	 *             validated
	 */
	private LocationReferencePoint getLRPfromRelative(
			final Coordinate previous, final byte[] currentPoint)
			throws ValidationException {
		LocationReferencePointBuilder lrpb = new LocationReferencePointBuilder();
		ByteArrayFiFo fifo = new ByteArrayFiFo(currentPoint);

		lrpb.start();
		lrpb.setCoordinate(getCoordinate(previous, fifo));
		lrpb.setFrc(FunctionalRoadClass.getFunctionalRoadClass((byte) ((fifo
				.peek() >> FRC_BITSHIFT) & FUNCTIONAL_ROAD_CLASS_BITMASK)));
		lrpb.setFow(FormOfWay
				.getFormOfWay((byte) (fifo.pop() & FORM_OF_WAY_BITMASK)));
		lrpb.setLfrcnp(FunctionalRoadClass.getFunctionalRoadClass((byte) ((fifo
				.peek() >> LFRCNP_BITSHIFT) & FUNCTIONAL_ROAD_CLASS_BITMASK)));
		lrpb.setBearing(Bearing.newBearing(fifo.pop()));
		lrpb.setDnp(Distance.newDistance(fifo.pop()));

		return lrpb.get();
	}
}
