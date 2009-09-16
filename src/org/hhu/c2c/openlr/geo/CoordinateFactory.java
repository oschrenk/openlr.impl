package org.hhu.c2c.openlr.geo;

/**
 * The {@link Coordinate} factory is used to create valid coordinates.
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
 * 
 */
public class CoordinateFactory {
	
	/**
	 * Holds the singleton instance
	 */
	private static CoordinateFactory instance = new CoordinateFactory();

	/**
	 * Private constructor to prohibit instantiation of this class
	 */
	private CoordinateFactory() {
	}

	/**
	 * Returns the singleton instance
	 * 
	 * @return the singleton instance
	 */
	public static CoordinateFactory getInstance() {
		return instance;
	}

	/**
	 * Creates a new {@link Coordinate} from the given byte array
	 * 
	 * @param b
	 *            a byte array of the length of six
	 * @return a new coordinate
	 * @throws IllegalArgumentException
	 *             if the array is malformed, not containing six bytes
	 */
	public Coordinate getCoordinate(byte[] b) {
		if (b.length != 6)
			throw new IllegalArgumentException(
					"The byte array must have a length of 6");
		return new Coordinate(CoordinateUtils
				.getFloatRepresentation(getLongitude(b)), CoordinateUtils
				.getFloatRepresentation(getLatitude(b)));

		// TODO move getFloatRepresentation to this class
	}

	/**
	 * Returns the longitude value with the given byte array
	 * 
	 * @param b
	 *            a byte array
	 * @returny the longitude value
	 */
	private long getLongitude(byte[] b) {
		return new Long((new Long(b[0]).longValue() << 16)
				+ (new Long(b[1]).longValue() << 8)
				+ (new Long(b[2]).longValue()));
	}

	/**
	 * Returns the latitude value with the given byte array
	 * 
	 * @param b
	 *            a byte array
	 * @returny the latitude value
	 */
	private long getLatitude(byte[] b) {
		return new Long((new Long(b[3]).longValue() << 16)
				+ (new Long(b[4]).longValue() << 8)
				+ (new Long(b[5]).longValue()));
	}
}
