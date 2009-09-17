package org.hhu.c2c.openlr.geo;

import org.hhu.c2c.openlr.l10n.Messages;

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
	 * Holds the latitude, for north-south measuring, represented by horizontal
	 * lines
	 */
	private final float latitude;

	/**
	 * Holds the longitude, for east-west measuring, represented by vertical
	 * lines
	 */
	private final float longitude;

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
	public Coordinate(final float longitude, final float latitude) {
		if (longitude < -180 || longitude > 180) {
			throw new IllegalArgumentException(Messages
					.getString("Coordinate.Exeption.LONGITUDE_MISFORMED")); //$NON-NLS-1$
		}

		if (latitude < -180 || latitude > 180) {
			throw new IllegalArgumentException(Messages
					.getString("Coordinate.Exeption.LATITUDE_MISFORMED")); //$NON-NLS-1$

		}

		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Returns the latitude
	 * 
	 * @return the latitude
	 */
	public float getLatitude() {
		return latitude;
	}

	/**
	 * Returns the longitude
	 * 
	 * @return the longitude
	 */
	public float getLongitude() {
		return longitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(latitude);
		result = prime * result + Float.floatToIntBits(longitude);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		if (Float.floatToIntBits(latitude) != Float
				.floatToIntBits(other.latitude))
			return false;
		if (Float.floatToIntBits(longitude) != Float
				.floatToIntBits(other.longitude))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "lon= " + longitude + "\uc2b0, lat= " + latitude + "\uc2b0";
	}
}
