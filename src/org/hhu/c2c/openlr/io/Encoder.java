package org.hhu.c2c.openlr.io;

import static org.hhu.c2c.openlr.io.PhysicalDataFormat.AREA_FLAG_BITMASK;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.ATTRIBUTE_FLAG_BITMASK;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.NEGATIVE_OFFSET_FLAG_BITMASK;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.POSITIVE_OFFSET_FLAG_BITMASK;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

import org.hhu.c2c.openlr.core.Bearing;
import org.hhu.c2c.openlr.core.Distance;
import org.hhu.c2c.openlr.core.FormOfWay;
import org.hhu.c2c.openlr.core.FunctionalRoadClass;
import org.hhu.c2c.openlr.core.LocationReference;
import org.hhu.c2c.openlr.core.LocationReferencePoint;
import org.hhu.c2c.openlr.geo.Coordinate;
import org.hhu.c2c.openlr.geo.CoordinateUtils;

/**
 * Used for marshalling a {@link LocationReference} into a byte stream
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
 * 
 */
public class Encoder {

	// TODO complete reference on how to encode a location reference

	/**
	 * Writes the given location reference to the given output stream.
	 * 
	 * @param out
	 *            the output stream
	 * @param locationReference
	 *            the location reference
	 * @throws IOException
	 *             if there are problems with the output stream
	 */
	public void write(final OutputStream out,
			final LocationReference locationReference) throws IOException {

		final LinkedList<LocationReferencePoint> points = new LinkedList<LocationReferencePoint>(
				locationReference.getLocationReferencePoints());

		// write 1 byte header
		writeHeader(out, locationReference.hasAreaFlag(), locationReference
				.hasAttributeFlag(), locationReference.getVersion());

		LocationReferencePoint previousPoint;
		Coordinate coordinate;
		// write absolute starting point
		previousPoint = points.poll();
		coordinate = previousPoint.getCoordinate();

		// write 3 bytes longitude
		out.write(CoordinateUtils.getByteArrayRepresentation(coordinate
				.getLongitude()));

		// write 3 byte latitude
		out.write(CoordinateUtils.getByteArrayRepresentation(coordinate
				.getLatitude()));

		// write 1st attribute (2 empty bit, 3 bit FRC, 3 bit FOW)
		writeFirstAttribute(out, previousPoint.getFunctionalRoadClass(),
				previousPoint.getFormOfWay());

		// write 2nd attribute (3 bit LFRCNP, 5 bit BEAR)
		writeSecondAttribute(out, previousPoint.getLowestFRCToNextPoint(),
				previousPoint.getBearing());

		// write 3rd attribute (8bit DNP)
		writeThirdAttribute(out, previousPoint.getDistanceToNextPoint());

		// write following points
		int size = points.size();
		LocationReferencePoint currentPoint;

		// if there were only two to begin with, we skip this par
		while (size >= 2) {
			// write relative point
			currentPoint = points.poll();

			// write 2 byte longitude, write 2 byte latitude
			out.write(CoordinateUtils.getByteArrayRepresentation(currentPoint
					.getCoordinate(), previousPoint.getCoordinate()));

			writeFirstAttribute(out, currentPoint.getFunctionalRoadClass(),
					currentPoint.getFormOfWay());
			writeSecondAttribute(out, currentPoint.getLowestFRCToNextPoint(),
					currentPoint.getBearing());
			writeThirdAttribute(out, currentPoint.getDistanceToNextPoint());

			// we need to save the previous one for the next round
			previousPoint = currentPoint;
			size--;
		}

		// only one point should be left, write last point
		currentPoint = points.poll();

		// write relative point
		// write 2 byte longitude, write 2 byte latitude
		out.write(CoordinateUtils.getByteArrayRepresentation(currentPoint
				.getCoordinate(), previousPoint.getCoordinate()));

		// write 1st attribute
		writeFirstAttribute(out, currentPoint.getFunctionalRoadClass(),
				currentPoint.getFormOfWay());

		// write 4th attribute (1 empty bit, 1 bit positive offset flag (pOffF),
		// 1 bit negative offset flag (nOffF), 5 bit bearing)
		writeFourthAttribute(out, locationReference.hasPositiveOffset(),
				locationReference.hasNegativeOffset(), currentPoint
						.getBearing());

		// write positive offset byte IFF pOffF true
		if (locationReference.hasPositiveOffset()) {
			writeDistance(out, locationReference.getPositiveOffset());
		}
		// write negative offset byte IFF nOffF true
		if (locationReference.hasNegativeOffset()) {
			writeDistance(out, locationReference.getNegativeOffset());
		}

	}

	/**
	 * Writes the distance as a byte array representation into the output
	 * stream.
	 * 
	 * @param out
	 *            the output stream
	 * @param distance
	 *            the distance
	 * @throws IOException
	 */
	private void writeDistance(final OutputStream out, final Distance distance)
			throws IOException {
		out.write(distance.getByteRepresentation());
	}

	/**
	 * Writes the first attribute, the functional road class and the form of way
	 * as a byte array representation into the output stream.
	 * 
	 * @param out
	 *            the output stream
	 * @param fcr
	 *            the functional road class
	 * @param fow
	 *            the form of way
	 * @throws IOException
	 */
	private void writeFirstAttribute(final OutputStream out,
			final FunctionalRoadClass fcr, final FormOfWay fow)
			throws IOException {
		out.write(fcr.getByteRepresentation() << 3
				| fow.getByteRepresentation());
	}

	/**
	 * Writes the fourth attribute, the positive as well as the negative offset
	 * flag and the bearing, as a byte array representation into the output
	 * stream.
	 * 
	 * @param out
	 *            the output stream
	 * @param positiveOffsetFlag
	 *            the positive offset flag
	 * @param negativeOffsetFlag
	 *            the negative offset flag
	 * @param bearing
	 *            the bearing
	 * @throws IOException
	 */
	private void writeFourthAttribute(final OutputStream out,
			final boolean positiveOffsetFlag, final boolean negativeOffsetFlag,
			final Bearing bearing) throws IOException {
		int attribute = 0;
		if (positiveOffsetFlag) {
			attribute = attribute | POSITIVE_OFFSET_FLAG_BITMASK;
		}

		if (negativeOffsetFlag) {
			attribute = attribute | NEGATIVE_OFFSET_FLAG_BITMASK;
		}
		out.write(attribute | bearing.getByteRepresentation());

	}

	/**
	 * Writes the header information, the area flag, the attribute flag and the
	 * version number to the output stream as a byte presentation
	 * 
	 * @param out
	 *            the output stream
	 * @param areaFlag
	 *            the area flag
	 * @param attributeFlag
	 *            the attribute flag
	 * @param version
	 *            the version number
	 * @throws IOException
	 */
	private void writeHeader(final OutputStream out, final boolean areaFlag,
			final boolean attributeFlag, final byte version) throws IOException {
		int header = version;
		if (areaFlag) {
			header = header | AREA_FLAG_BITMASK;
		}

		if (attributeFlag) {
			header = header | ATTRIBUTE_FLAG_BITMASK;
		}

		out.write(header);
	}

	/**
	 * Writes the second attribute, the lowest functional road class to the next
	 * point and the bearing, as a byte array representation into the output
	 * stream.
	 * 
	 * @param out
	 *            the output stream
	 * @param lowestFRCToNextPoint
	 * @param bearing
	 *            the bearing
	 * @throws IOException
	 */
	private void writeSecondAttribute(final OutputStream out,
			final FunctionalRoadClass lowestFRCToNextPoint,
			final Bearing bearing) throws IOException {
		out.write(lowestFRCToNextPoint.getByteRepresentation() << 5
				| bearing.getByteRepresentation());
	}

	/**
	 * Writes the third attribute, the distance to the next point, as a byte
	 * array representation into the output stream.
	 * 
	 * @param out
	 *            the output stream
	 * @param distanceToNextPoint
	 *            the distance to next location reference point
	 * @throws IOException
	 */
	private void writeThirdAttribute(final OutputStream out,
			final Distance distanceToNextPoint) throws IOException {
		writeDistance(out, distanceToNextPoint);
	}

}
