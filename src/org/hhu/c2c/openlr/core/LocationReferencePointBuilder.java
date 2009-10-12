package org.hhu.c2c.openlr.core;

import org.hhu.c2c.openlr.geo.Coordinate;
import org.hhu.c2c.openlr.util.Builder;
import org.hhu.c2c.openlr.util.LocationReferenceException;

/**
 * The {@link LocationReferencePointBuilder} helps building valid
 * {@link LocationReferencePoint}s
 * 
 * @see LocationReferencePoint
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * 
 */
public class LocationReferencePointBuilder implements
		Builder<LocationReferencePointBuilder, LocationReferencePoint> {

	/**
	 * Holds the bearing
	 */
	private Bearing bearing;

	/**
	 * Holds the coordinate
	 **/
	private Coordinate coordinate;

	/**
	 * Holds the distance to the next location reference point. When building
	 * the last location reference point of a location reference this defaults
	 * to <code>new Distance(0)</code>.
	 */
	private Distance dnp;

	/**
	 * Holds the form of way
	 */
	private FormOfWay fow;

	/**
	 * Holds the functional road class
	 */
	private FunctionalRoadClass frc;

	/**
	 * Holds the lowest functional road class to the next location reference
	 * point. When building the last location reference point of a location
	 * reference this defaults to <code>null</code>
	 */
	private FunctionalRoadClass lfrcnp;

	// TODO default to a something else, null value or so?

	/**
	 * Constructs a new {@link LocationReferencePointBuilder}
	 */
	public LocationReferencePointBuilder() {
		init();
	}

	/**
	 * {@link Builder#build()}
	 */
	@Override
	public LocationReferencePoint build() throws LocationReferenceException {
		return new LocationReferencePoint(coordinate, frc, fow, lfrcnp,
				bearing, dnp);
	}

	/**
	 * Resets the location reference point
	 */
	private void init() {
		coordinate = null;
		frc = null;
		fow = null;
		lfrcnp = null;
		bearing = null;
		dnp = null;
	}

	/**
	 * Sets the bearing
	 * 
	 * @param bearing
	 *            the new bearing
	 * @return the same instance of this {@link LocationReferencePointBuilder}
	 *         for use in a fluid interface
	 */
	public LocationReferencePointBuilder setBearing(final Bearing bearing) {
		this.bearing = bearing;
		return this;
	}

	/**
	 * Convenience method to set the bearing
	 * 
	 * @param bearing
	 *            the new bearing
	 * @return the same instance of this {@link LocationReferencePointBuilder}
	 *         for use in a fluid interface
	 */
	public LocationReferencePointBuilder setBearing(float bearing) {
		return setBearing(new Bearing(bearing));
	}

	/**
	 * Sets the coordinate
	 * 
	 * @param coordinate
	 *            the new coordinate
	 * @return the same instance of this {@link LocationReferencePointBuilder}
	 *         for use in a fluid interface
	 */
	public LocationReferencePointBuilder setCoordinate(
			final Coordinate coordinate) {
		this.coordinate = coordinate;
		return this;
	}

	/**
	 * Convenience method to set the coordinate
	 * 
	 * @param longitude
	 *            the longitude
	 * @param latitude
	 *            the latitude
	 * 
	 * @see #setCoordinate(Coordinate)
	 * 
	 * @return the same instance of this {@link LocationReferencePointBuilder}
	 *         for use in a fluid interface
	 * @throws LocationReferenceException
	 *             if the angular measurements for the coordinates are misformed
	 */
	public LocationReferencePointBuilder setCoordinate(final float longitude,
			final float latitude) throws LocationReferenceException {
		return setCoordinate(Coordinate.newCoordinate(longitude, latitude));
	}

	/**
	 * Sets the distance to the next location reference point
	 * 
	 * @param dnp
	 *            the new distance to the next location reference point
	 * @return the same instance of this {@link LocationReferencePointBuilder}
	 *         for use in a fluid interface
	 */
	public LocationReferencePointBuilder setDnp(final Distance dnp) {
		this.dnp = dnp;
		return this;
	}

	/**
	 * Convenience method to set the distance to the next location reference
	 * point
	 * 
	 * @param dnp
	 *            the new distance to the next location reference point in meter
	 * @return the same instance of this {@link LocationReferencePointBuilder}
	 *         for use in a fluid interface
	 * @throws LocationReferenceException
	 *             if distance doesn't follow the data rules
	 */
	public LocationReferencePointBuilder setDnp(int dnp)
			throws LocationReferenceException {
		return setDnp(Distance.newDistance(dnp));
	}

	/**
	 * Sets the form of way
	 * 
	 * @param fow
	 *            the new form of way
	 * @return the same instance of this {@link LocationReferencePointBuilder}
	 *         for use in a fluid interface
	 */
	public LocationReferencePointBuilder setFow(final FormOfWay fow) {
		this.fow = fow;
		return this;
	}

	/**
	 * Sets the functional road class
	 * 
	 * @param frc
	 *            the new functional road class
	 * @return the same instance of this {@link LocationReferencePointBuilder}
	 *         for use in a fluid interface
	 */
	public LocationReferencePointBuilder setFrc(final FunctionalRoadClass frc) {
		this.frc = frc;
		return this;
	}

	/**
	 * The <b>lowest FRC to the next point</b> (<code>LFRCNP</code>) is the
	 * lowest FRC value which appears in the location reference path between two
	 * consecutive LR-points. This information could be used to limit the number
	 * of road classes which need to be scanned during the decoding. The highest
	 * FRC value is 0 and the lowest possible FRC value is valued with 7.
	 * 
	 * @param lfrcnp
	 *            the new lowest functional road class to the next point
	 * @return the same instance of this {@link LocationReferencePointBuilder}
	 *         for use in a fluid interface
	 */
	public LocationReferencePointBuilder setLfrcnp(
			final FunctionalRoadClass lfrcnp) {
		this.lfrcnp = lfrcnp;
		return this;
	}

	/**
	 * {@link Builder#reset()}
	 */
	@Override
	public LocationReferencePointBuilder reset() {
		init();
		return this;
	}

	/**
	 * {@link Builder#validate()}
	 */
	@Override
	public void validate() throws LocationReferenceException {

	}

}
