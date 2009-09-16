package org.hhu.c2c.openlr.io;

import static org.hhu.c2c.openlr.io.PhysicalDataFormat.AREA_FLAG_BITMASK;
import static org.hhu.c2c.openlr.io.PhysicalDataFormat.ATTRIBUTE_FLAG_BITMASK;

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

public class Encoder {

	/**
	 * Writes the given location reference to the given output stream.
	 * 
	 * @param out
	 *            the output stream
	 * @param lr
	 *            the location reference
	 * @throws IOException
	 *             if there are problems with the output stream
	 */
	public void write(OutputStream out, LocationReference lr)
			throws IOException {

		LinkedList<LocationReferencePoint> points = new LinkedList<LocationReferencePoint>(
				lr.getLocationReferencePoints());

		// write 1 byte header
		writeHeader(out, lr.hasAreaFlag(), lr.hasAttributeFlag(), lr
				.getVersion());

		LocationReferencePoint previousPoint = null;
		Coordinate c;
		// write absolute starting point
		previousPoint = points.poll();
		c = previousPoint.getCoordinate();

		// write 3 bytes longitude
		out
				.write(CoordinateUtils.getByteArrayRepresentation(c
						.getLongitude()));

		// write 3 byte latitude
		out.write(CoordinateUtils.getByteArrayRepresentation(c.getLatitude()));

		// write 1st attribute (2 empty bit, 3 bit FRC, 3 bit FOW)
		writeFirstAttribute(out, previousPoint.getFunctionalRoadClass(),
				previousPoint.getFormOfWay());

		// write 2nd attribute (3 bit LFRCNP, 5 bit BEAR)
		writeSecondAttribute(out, previousPoint.getLowestFRCToNextPoint(),
				previousPoint.getBearing());

		// write 3rd attribute (8bit DNP)
		writeThirdAttribute(out, previousPoint.getDistanceToNextPoint());

		// write following points
		int i = points.size();
		LocationReferencePoint currentPoint;

		// if there were only two to begin with, we skip this par
		while (i >= 2) {
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
			i--;
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
		writeFourthAttribute(out, lr.hasPositiveOffset(), lr
				.hasNegativeOffset(), currentPoint.getBearing());

		// write positive offset byte IFF pOffF true
		if (lr.hasPositiveOffset()) {
			writeDistance(out, lr.getPositiveOffset());
		}
		// write negative offset byte IFF nOffF true
		if (lr.hasNegativeOffset()) {
			writeDistance(out, lr.getNegativeOffset());
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
		bout.write(toByteArray((byte) (fcr.getByteRepresentation() << 3 | fow
				.getByteRepresentation())));
	}

	private void writeSecondAttribute(OutputStream bout,
			FunctionalRoadClass lowestFRCToNextPoint, Bearing bearing)
			throws IOException {
		bout.write(toByteArray((byte) (lowestFRCToNextPoint.getByteRepresentation() << 5 | bearing
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
	
}
