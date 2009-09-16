package org.hhu.c2c.openlr.core;

import org.hhu.c2c.openlr.geo.Coordinate;
import org.hhu.c2c.openlr.util.Builder;
import org.hhu.c2c.openlr.util.ValidationException;

/**
 * The {@link LocationReferencePointBuilder} helps building valid
 * {@link LocationReferencePoint}s
 * 
 * @see LocationReferencePoint
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
 * 
 */
public class LocationReferencePointBuilder implements
		Builder<LocationReferencePointBuilder, LocationReferencePoint> {

	/**
	 * Holds the coordinate
	 **/
	private Coordinate coordinate;

	/**
	 * Holds the functional road class
	 */
	private FunctionalRoadClass frc;

	/**
	 * Holds the form of way
	 */
	private FormOfWay fow;

	/**
	 * Holds the lowest functional road class to the next location reference
	 * point. When building the last location reference point of a location
	 * reference this defaults to <code>null</code>
	 */
	private FunctionalRoadClass lfrcnp;
	// TODO default to a something else, null value or so?

	/**
	 * Holds the bearing
	 */
	private Bearing bearing;

	/**
	 * Holds the distance to the next location reference point. When building
	 * the last location reference point of a location reference this defaults
	 * to <code>new Distance(0)</code>.
	 */
	private Distance dnp;

	/**
	 * Constructs a new {@link LocationReferencePointBuilder}
	 */
	public LocationReferencePointBuilder() {
		init();
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
	 * {@link Builder#start()}
	 */
	@Override
	public LocationReferencePointBuilder start() {
		init();
		return this;
	}

	/**
	 * Sets the coordinate
	 * 
	 * @param coordinate
	 *            the new cordinate
	 * @return the same instance of this {@link LocationReferencePointBuilder}
	 *         for use in a fluid interface
	 */
	public LocationReferencePointBuilder setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
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
	public LocationReferencePointBuilder setFrc(FunctionalRoadClass frc) {
		this.frc = frc;
		return this;
	}

	/**
	 * Sets the form of way
	 * 
	 * @param fow
	 *            the new form of way
	 * @return the same instance of this {@link LocationReferencePointBuilder}
	 *         for use in a fluid interface
	 */
	public LocationReferencePointBuilder setFow(FormOfWay fow) {
		this.fow = fow;
		return this;
	}

	/**
	 * Sets the lowest functional road class to the next point
	 * 
	 * @param lfrcnp
	 *            the new lowest functional road class to the next point
	 * @return the same instance of this {@link LocationReferencePointBuilder}
	 *         for use in a fluid interface
	 */
	public LocationReferencePointBuilder setLfrcnp(FunctionalRoadClass lfrcnp) {
		this.lfrcnp = lfrcnp;
		return this;
	}

	// TODO better javadoc for lfrcnp

	/**
	 * Sets the bearing
	 * 
	 * @param bearing
	 *            the new bearing
	 * @return the same instance of this {@link LocationReferencePointBuilder}
	 *         for use in a fluid interface
	 */
	public LocationReferencePointBuilder setBearing(Bearing bearing) {
		this.bearing = bearing;
		return this;
	}

	/**
	 * Sets the distance to the next location reference point
	 * 
	 * @param dnp
	 *            the new distance to the next location reference point
	 * @return the same instance of this {@link LocationReferencePointBuilder}
	 *         for use in a fluid interface
	 */
	public LocationReferencePointBuilder setDnp(Distance dnp) {
		this.dnp = dnp;
		return this;
	}

	/**
	 * {@link Builder#get()}
	 */
	@Override
	public LocationReferencePoint get() throws ValidationException {
		// TODO what if last point
		// TODO validate
		return new LocationReferencePoint(coordinate, frc, fow, lfrcnp,
				bearing, dnp);
	}

	/**
	 * {@link Builder#validates()}
	 */
	@Override
	public boolean validates() {
		// TODO Auto-generated method stub
		return false;
	}
}
