package org.hhu.c2c.openlr.core;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.hhu.c2c.openlr.geo.Coordinate;
import org.hhu.c2c.openlr.geo.CoordinateFactory;
import org.hhu.c2c.openlr.geo.CoordinateUtils;
import org.hhu.c2c.openlr.util.ByteArrayFiFo;

/**
 * The <b>location reference factory</b> is used for creating and receiving byte
 * arrays describing location references. It does so by following the physical
 * data structure and not imposing any further logic on it.
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version 1.0, 2009-09-20
 * 
 */
public class LocationReferenceFactory {

	/**
	 * Number of bytes needed for a relative coordinate part, like longitude or
	 * latitude
	 */
	private static final int NUMBER_OF_BYTES_FOR_RELATIVE_COORDINATE = 2;

	/**
	 * Number of bytes for an absolute coordinate, three bytes for the longitude
	 * and three bytes for latitude
	 */
	private static final int NUMBER_OF_BYTES_FOR_ABSOLUTE_COORDINATE = 6;

	/**
	 * The number of bytes used to describe the first location reference point
	 * of a location reference, including an absolute coordinate represewnted by
	 * 6 bytes (3 bytes longitude, 3 bytes latitude), one byte describing
	 * {@link FunctionalRoadClass} (bit 5 to 3) as well as the {@link FormOfWay}
	 * (bit 2 to 0), followed by the second attribute describing the lowest FRC
	 * to the next point (bit 7 to 5) as well as the bearing (bit 4 to 0) and
	 * finally by the {@link Distance} to the next location reference point
	 * described by a complete byte.
	 */
	private static final int NUMBER_OF_BYTES_FOR_ABSOLUTE_LRP = 9;

	/**
	 * The number of bytes used to describe following location reference points
	 * with relative coordinate values. The relative coordinate is described by
	 * the first 4 bytes (2 for longitude, 2 for latitude). The following three
	 * bytes are used as in the first absolute location reference point.
	 */
	private static final int NUMBER_OF_BYTES_FOR_RELATIVE_LRP = 7;

	/**
	 * The number of bytes used to describe the closing location reference
	 * point. It also makes use of the relative coordinate format (4 bytes), but
	 * as it is the last it only describes in one byte its own functional road
	 * class and form of way as the other location reference points, but the
	 * last byte is used to indicate if the location reference uses a positive
	 * offset (bit 6) or a negative offset (bit 5) and the last 5 bits for the
	 * bearing of the incoming line.
	 */
	private static final int MINIMUM_NUMBER_OF_BYTES_FOR_LAST_LRP = 6;

	/**
	 * As the header uses 1 byte and the protcol demands at least two location
	 * reference points, the first one, as an absolute point using 9 bytes and
	 * the last one using 6 bytes.
	 */
	private static final int MINIMUM_NUMBER_OF_BYTES = 16;

	private static final int FRC_BITSHIFT = 3;

	private static final int LFRCNP_BITSHIFT = 5;

	/**
	 * Describes a bitmask, with all bits set to 0: <code>0000 0000</code>
	 */
	private static final byte ZERO_BITMASK = 0;

	/**
	 * The attribute flag in the header byte uses bit 4:<code>0000 1000</code>
	 */
	private static final byte ATTRIBUTE_FLAG_BITMASK = 8;

	/**
	 * The area flag in the header byte uses bit 5:<code>0001 0000</code>
	 */
	private static final byte AREA_FLAG_BITMASK = 16;

	/**
	 * Describes a bitmask, with the three least significant bits set to 1:
	 * "0000 0111"
	 */
	private static final byte THREE_LEAST_SIGNIFICANT_BITS_BITMASK = 1 + 2 + 4;

	/**
	 * The three least significant bits of the header byte represent the version
	 * number
	 * 
	 * @see #VERSION
	 */
	private static final byte VERSION_NUMBER_BITMASK = THREE_LEAST_SIGNIFICANT_BITS_BITMASK;
	// TODO check compbability of version

	/**
	 * The fifth to third least significant bits represent the functional road
	 * class in the first attribute. In order to have the value of the
	 * functional road class in the range of 0 to 7, the bits are shifted to the
	 * three least significant bits.
	 */
	private static final byte FUNCTIONAL_ROAD_CLASS_BITMASK = THREE_LEAST_SIGNIFICANT_BITS_BITMASK;

	/**
	 * The three least significant bits are used in the first attribute to
	 * describe to form of way.
	 */
	private static final byte FORM_OF_WAY_BITMASK = THREE_LEAST_SIGNIFICANT_BITS_BITMASK;

	/**
	 * Holds the singleton instance
	 */
	private static final LocationReferenceFactory instance = new LocationReferenceFactory();

	private static final byte POSITIVE_OFFSET_FLAG_BITMASK = 64;

	private static final byte NEGATIVE_OFFSET_FLAG_BITMASK = 32;
	/**
	 * Holds the coordinate factory
	 */
	private final CoordinateFactory coordinateFactory;

	/**
	 * Private constructor to prohibit instantiation
	 */
	private LocationReferenceFactory() {
		this.coordinateFactory = CoordinateFactory.getInstance();
	}

	/**
	 * Returns the singleton instance of the location reference factory
	 * 
	 * @return an instance of the location reference factory
	 */
	public static LocationReferenceFactory getInstance() {
		return instance;
	}

	/**
	 * Writes the given location reference into the given output stream.
	 * 
	 * @param bout
	 *            the output stream
	 * @param lr
	 *            the location reference
	 * @throws IOException
	 *             if there are problems with the output stream
	 */
	public void write(OutputStream bout, LocationReference lr)
			throws IOException {

		LinkedList<LocationReferencePoint> points = new LinkedList<LocationReferencePoint>(
				lr.getLocationReferencePoints());
		if (points.size() < Rules.MINIMUM_NUMBER_OF_LR_POINTS)
			throw new IllegalArgumentException(
					"There have to be at least two location reference points.");

		// write 1 byte header
		writeHeader(bout, lr.hasAreaFlag(), lr.hasAttributeFlag(), lr
				.getVersion());

		LocationReferencePoint previousPoint = null;
		Coordinate c;
		// write absolute starting point
		previousPoint = points.poll();
		c = previousPoint.getCoordinate();

		// write 3 bytes longitude
		bout
				.write(CoordinateUtils.getByteArrayRepresentation(c
						.getLongitude()));

		// write 3 byte latitude
		bout.write(CoordinateUtils.getByteArrayRepresentation(c.getLatitude()));

		// write 1st attribute (2 empty bit, 3 bit FRC, 3 bit FOW)
		writeFirstAttribute(bout, previousPoint.getFunctionalRoadClass(),
				previousPoint.getFormOfWay());

		// write 2nd attribute (3 bit LFRCNP, 5 bit BEAR)
		writeSecondAttribute(bout, previousPoint.getLowestFRCToNextPoint(),
				previousPoint.getBearing());

		// write 3rd attribute (8bit DNP)
		writeThirdAttribute(bout, previousPoint.getDistanceToNextPoint());

		// write following points
		int i = points.size();
		LocationReferencePoint currentPoint;

		// if there were only two to begin with, we skip this par
		while (i >= 2) {
			// write relative point
			currentPoint = points.poll();

			// write 2 byte longitude, write 2 byte latitude
			bout.write(CoordinateUtils.getByteArrayRepresentation(currentPoint
					.getCoordinate(), previousPoint.getCoordinate()));

			writeFirstAttribute(bout, currentPoint.getFunctionalRoadClass(),
					currentPoint.getFormOfWay());
			writeSecondAttribute(bout, currentPoint.getLowestFRCToNextPoint(),
					currentPoint.getBearing());
			writeThirdAttribute(bout, currentPoint.getDistanceToNextPoint());

			// we need to save the previous one for the next round
			previousPoint = currentPoint;
			i--;
		}

		// only one point should be left, write last point
		currentPoint = points.poll();

		// write relative point
		// write 2 byte longitude, write 2 byte latitude
		bout.write(CoordinateUtils.getByteArrayRepresentation(currentPoint
				.getCoordinate(), previousPoint.getCoordinate()));

		// write 1st attribute
		writeFirstAttribute(bout, currentPoint.getFunctionalRoadClass(),
				currentPoint.getFormOfWay());

		// write 4th attribute (1 empty bit, 1 bit positive offset flag (pOffF),
		// 1 bit negative offset flag (nOffF), 5 bit bearing)
		writeFourthAttribute(bout, lr.hasPositiveOffset(), lr
				.hasNegativeOffset(), currentPoint.getBearing());

		// write positive offset byte IFF pOffF true
		if (lr.hasPositiveOffset()) {
			writeDistance(bout, lr.getPositiveOffset());
		}
		// write negative offset byte IFF nOffF true
		if (lr.hasNegativeOffset()) {
			writeDistance(bout, lr.getNegativeOffset());
		}

	}

	private void writeHeader(OutputStream bout, boolean areaFlag,
			boolean attributeFlag, byte version) throws IOException {
		byte header = version;
		if (areaFlag)
			header = (byte) (header | AREA_FLAG_BITMASK);
		if (attributeFlag)
			header = (byte) (header | ATTRIBUTE_FLAG_BITMASK);
		bout.write(toByteArray(header));
	}

	private void writeFirstAttribute(OutputStream bout,
			FunctionalRoadClass fcr, FormOfWay fow) throws IOException {
		bout.write(toByteArray((byte) (ZERO_BITMASK
				| fcr.getByteRepresentation() << 3 | fow
				.getByteRepresentation())));
	}

	private void writeSecondAttribute(OutputStream bout,
			FunctionalRoadClass lowestFRCToNextPoint, Bearing bearing)
			throws IOException {
		bout.write(toByteArray((byte) (ZERO_BITMASK
				| lowestFRCToNextPoint.getByteRepresentation() << 5 | bearing
				.getByteRepresentation())));
	}

	private void writeThirdAttribute(OutputStream bout,
			Distance distanceToNextPoint) throws IOException {
		writeDistance(bout, distanceToNextPoint);
	}

	private void writeFourthAttribute(OutputStream out,
			boolean positiveOffsetFlag, boolean negativeOffsetFlag,
			Bearing bearing) throws IOException {

	}

	private void writeDistance(OutputStream out, Distance d) throws IOException {
		out.write(toByteArray(d.getByteRepresentation()));
	}

	private byte[] toByteArray(byte b) {
		byte[] bA = new byte[1];
		bA[0] = b;
		return bA;
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
		// TODO versions feld untersuchen
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
		points.add(new LocationReferencePoint(c, fcr, fow, bearing));

		return new LocationReference(areaFlag, attributeFlag, version, points,
				positiveOffset, negativeOffset);
	}

	private LocationReferencePoint getLRPfromRelative(Coordinate previous,
			byte[] b) {

		ByteArrayFiFo fifo = new ByteArrayFiFo(b);

		return new LocationReferencePoint(
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

	private LocationReferencePoint getAbsoluteLRP(byte[] b) {
		ByteArrayFiFo fifo = new ByteArrayFiFo(b);

		return new LocationReferencePoint(
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
