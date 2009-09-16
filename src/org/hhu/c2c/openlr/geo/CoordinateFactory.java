package org.hhu.c2c.openlr.geo;

/**
 * The {@link Coordinate} factory is used to create valid coordinates.
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
 * 
 */
public final class CoordinateFactory {

	/**
	 * Holds the singleton instance
	 */
	private static CoordinateFactory instance = new CoordinateFactory();

	/**
	 * Returns the singleton instance
	 * 
	 * @return the singleton instance
	 */
	public static CoordinateFactory getInstance() {
		return instance;
	}

	/**
	 * Private constructor to prohibit instantiation of this class
	 */
	private CoordinateFactory() {
	}

	/**
	 * Creates a new {@link Coordinate} from the given byte array
	 * 
	 * @param coordinate
	 *            a byte array of the length of six
	 * @return a new coordinate
	 * @throws IllegalArgumentException
	 *             if the array is malformed, not containing six bytes
	 */
	public Coordinate getCoordinate(final byte[] coordinate) {
		if (coordinate.length != 6) {
			throw new IllegalArgumentException(
					"The byte array must have a length of 6");
		}

		return new Coordinate(CoordinateUtils
				.getFloatRepresentation(getLongitude(coordinate)),
				CoordinateUtils.getFloatRepresentation(getLatitude(coordinate)));
	}

	/**
	 * Returns the latitude value with the given byte array
	 * 
	 * @param latitude
	 *            a byte array
	 * @returny the latitude value
	 */
	private long getLatitude(final byte[] latitude) {
		return Long.valueOf(Long.valueOf(latitude[3]) << 16 + Long
				.valueOf(latitude[4]) << 8 + Long.valueOf(latitude[5]));
	}

	/**
	 * Returns the longitude value with the given byte array
	 * 
	 * @param longitude
	 *            a byte array
	 * @returny the longitude value
	 */
	private long getLongitude(final byte[] longitude) {
		return Long.valueOf(Long.valueOf(longitude[0]) << 16 + Long
				.valueOf(longitude[1]) << 8 + Long.valueOf(longitude[2]));
	}
}
