package org.hhu.c2c.openlr.geo;

/**
 * A <b>Coordinate pair</b> (<code>COORD</code>) stands for a pair of WGS84
 * longitude (lon) and latitude (lat) values. This coordinate pair specifies a
 * geometric point in a digital map. The lon and lat values are stored in a
 * decamicrodegrees resolution ( {pow(10, -5)} , five decimals).
 * 
 * @see <a href="http://en.wikipedia.org/wiki/World_Geodetic_System_1984">WGS84
 *      (World Geodetic System 1984)</a>
 * 
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
 * 
 */
public class Coordinate {
	// TODO make sure of five decimal places

	/**
	 * Holds the longitude, for east-west measuring, represented by vertical
	 * lines
	 */
	private float longitude;

	/**
	 * Holds the latitude, for north-south measuring, represented by horizontal
	 * lines
	 */
	private float latitude;

	/**
	 * Creates a new coordinate by passing longitude and a latitude parameter.
	 * 
	 * @param longitude
	 *            the longitude, for east-west measuring, represented by
	 *            vertical lines
	 * @param latitude
	 *            the latitude, for north-south measuring, represented by
	 *            horizontal lines
	 * @throws IllegalArgumentException
	 *             If either the longitude or the latitude a misformed. (eg.
	 *             being lower than -180 degree or above 180 degrees)
	 */
	public Coordinate(float longitude, float latitude) {
		if (longitude < -180 || longitude > 180)
			throw new IllegalArgumentException(
					"The longitude isn't properly formatted.");
		if (latitude < -180 || latitude > 180)
			throw new IllegalArgumentException(
					"The longitude isn't properly formatted.");

		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Returns the longitude
	 * @return the longitude
	 */
	public float getLongitude() {
		return longitude;
	}

	/**
	 * Returns the latitude
	 * @return the latitude
	 */
	public float getLatitude() {
		return latitude;
	}
}
