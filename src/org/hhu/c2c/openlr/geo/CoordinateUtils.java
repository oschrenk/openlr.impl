package org.hhu.c2c.openlr.geo;

/**
 * <b>CoordinateUtils</b> is a collection of small helper functions for
 * converting coorinates into and out of their respective representations
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
 * 
 */
public class CoordinateUtils {
	// TODO I don't like this class

	/**
	 * The resolution parameter is used to convert the float representation of
	 * the longiitude (or latitude) into the respective long representation
	 */
	private static final byte RESOLUTION_PARAMETER = 24;

	/**
	 * Used to inflate the integer value when converting a relative coordinate
	 * to its integer representation
	 */
	private static final int RELATIVE_FORMAT_INT_MULTIPLIER = 100000;

	/**
	 * Returns the long representation of a float, used for converting
	 * coordinate values like longitude or latitude
	 * 
	 * @param f
	 *            a longitude or latitude
	 * @return the long representation of the longitude (ot latitude)
	 */
	private static long getLongRepresentation(float f) {
		return (long) (Math.signum(f) / 2 + (f / 360)
				* (Math.pow(2, RESOLUTION_PARAMETER)));
	}

	/**
	 * Returns the float represenation of longitude or latitude encoded as a
	 * long value
	 * 
	 * @param l
	 *            the long value of the longitude (or latitude)
	 * @return the float representation
	 */
	protected static float getFloatRepresentation(long l) {
		return (float) ((l - Math.signum(l) / 2) * 360 * (Math.pow(2,
				-RESOLUTION_PARAMETER)));
	}

	/**
	 * Returns a longitude or latitude encoded as a float as a byte array
	 * representation
	 * 
	 * @param degree
	 * @return a byte array representation of a longitude or latitude
	 */
	public static byte[] getByteArrayRepresentation(float degree) {
		byte[] b = new byte[3];
		long longitude = CoordinateUtils.getLongRepresentation(degree);
		b[0] = new Long(longitude >> 16).byteValue();
		b[1] = new Long(longitude >> 8).byteValue();
		b[2] = new Long(longitude >> 0).byteValue();
		return b;
	}

	/**
	 * Returns a byte array representation of a relative coordinate
	 * 
	 * @param current
	 *            the current coordinate
	 * @param previous
	 *            the previous coordinat
	 * @return a byte array representation of a relative coordinate
	 */
	public static byte[] getByteArrayRepresentation(Coordinate current,
			Coordinate previous) {
		int longitude = (int) (RELATIVE_FORMAT_INT_MULTIPLIER * (current
				.getLongitude() - previous.getLongitude()));
		int latitude = (int) (RELATIVE_FORMAT_INT_MULTIPLIER * (current
				.getLatitude() - previous.getLatitude()));

		byte[] b = new byte[4];
		b[0] = new Integer(longitude >> 8).byteValue();
		b[1] = new Integer(longitude >> 0).byteValue();
		b[2] = new Integer(latitude >> 8).byteValue();
		b[3] = new Integer(latitude >> 0).byteValue();
		return b;
	}

	/**
	 * Returns the integer representation of a relative coordinate
	 * 
	 * @param b
	 *            a relative longitude or relative latitude encoded as two bytes
	 * @return the integer representation of a relative coordinate
	 */
	public static int getRelativeCoordinateIntValue(byte[] b) {
		int i = 0;
		i = i | (b[0]) << 8;
		return i | b[1];
	}

	/**
	 * Returns the absolute float value of a longitude (or latitude) from a the
	 * integer encoded value of a longitude (or latitude) and the absolute
	 * longitude (or latitude as a float from the previous point)
	 * 
	 * @param relative the integer representation of a relative longitude (or latitude) from the current point
	 * @param previousPointDegree the float representation of the absolute longitude (or latitude) from the previous point
	 * @return the absolute value of the longitude (or latitude) of the current point
	 */
	public static float getDegreeFromRelative(int relative,
			float previousPointDegree) {
		return relative / RELATIVE_FORMAT_INT_MULTIPLIER + previousPointDegree;
	}

}
