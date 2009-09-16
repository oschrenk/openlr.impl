package org.hhu.c2c.openlr.core;

import org.hhu.c2c.openlr.geo.Coordinate;

/**
 * The basis of a {@link LocationReference} is a sequence of location reference
 * points (<code>LR-points</code>). Such a LR- point contains a coordinate pair,
 * specified in WGS84 longitude and latitude values and additionally several
 * attributes.
 * 
 * The {@link Coordinate}s represent geographical positions within a map/network
 * and is mandatory for a LR-point. A coordinate belongs to a “real” node within
 * a network (in most cases these nodes represent junctions in the real world).
 * 
 * The location reference points are stored in a topological order so that a
 * point <code>A</code> will directly follow a point <code>B</code> if
 * <code>B</code> also comes after <code>A</code> in the location reference path
 * and there is no other LR-point in between. This topological order defines a
 * “next point”-relationship of successive LR-points. The last point in this
 * order will have no next point in this relationship.
 * 
 * @see <a href="http://en.wikipedia.org/wiki/World_Geodetic_System_1984">WGS84
 *      (World Geodetic System 1984)</a>
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
 * 
 */
public class LocationReferencePoint {

	/**
	 * Holds the coordinate
	 */
	private final Coordinate coordinate;

	/**
	 * Holds the bearing
	 */
	private final Bearing bearing;

	/**
	 * Holds the distance to the the next location reference point. When this
	 * location reference point is the last one of a location reference this
	 * defaults to <code>new Distance(0)</code>
	 */
	private final Distance distanceToNextPoint;

	/**
	 * Holds the functional road class
	 */
	private final FunctionalRoadClass functionalRoadClass;

	/**
	 * Holda the form of way
	 */
	private final FormOfWay formOfWay;

	/**
	 * Holds the lowest functional road class to the next location reference
	 * point. When this location reference point is the last one of a location
	 * reference this is <code>null</code>
	 */
	private FunctionalRoadClass lowestFRCToNextPoint;

	// TODO set to a default value like -1 or so when last point?

	/**
	 * Constructs a new location reference point, wihtout a value for lfrcnp and
	 * distance, indicating that it is used to describe the last point of a
	 * location reference.
	 * 
	 * @param coordinate
	 *            the coordinate
	 * @param frc
	 *            the functional road class
	 * @param fow
	 *            the form of way
	 * @param bearing
	 *            the bearing
	 */
	protected LocationReferencePoint(final Coordinate coordinate,
			final FunctionalRoadClass frc, final FormOfWay fow,
			final Bearing bearing) {
		// TODO handle last point differently? DNP=0? LFRCNP= ????
		this.coordinate = coordinate;
		this.functionalRoadClass = frc;
		this.formOfWay = fow;
		this.bearing = bearing;
		this.distanceToNextPoint = new Distance(0);
	}

	/**
	 * Constructs a new location reference point with all the neccesary
	 * parameters.
	 * 
	 * @param coordinate
	 *            the coordinate
	 * @param frc
	 *            the functional road class
	 * @param fow
	 *            the form of way
	 * @param lfrcnp
	 *            the lowest functional road class to the next point
	 * @param bearing
	 *            the bearing
	 * @param distance
	 *            the distance
	 */
	protected LocationReferencePoint(final Coordinate coordinate,
			final FunctionalRoadClass frc, final FormOfWay fow,
			final FunctionalRoadClass lfrcnp, final Bearing bearing,
			final Distance distance) {
		this.coordinate = coordinate;
		this.functionalRoadClass = frc;
		this.formOfWay = fow;
		this.lowestFRCToNextPoint = lfrcnp;
		this.bearing = bearing;
		this.distanceToNextPoint = distance;
	}

	/**
	 * Returns the {@link Bearing} of this {@link LocationReferencePoint}
	 * 
	 * @return the bearing of this location reference point
	 */
	public Bearing getBearing() {
		return bearing;
	}

	/**
	 * Returns the coordinate of this location reference point.
	 * 
	 * @return the coordinate of this location reference point
	 */
	public Coordinate getCoordinate() {
		return coordinate;
	}

	/**
	 * This <code>DNP</code> (<b>distance to next location reference point</b>)
	 * field describes the distance to the next {@link LocationReferencePoint}
	 * in the topological connection of the LR- points. The distance is measured
	 * in meters and is calculated along the location reference path. The last
	 * LR-point will have the distance value 0.
	 * 
	 * @return the distance to the next location reference point
	 */
	public Distance getDistanceToNextPoint() {
		// TODO the last lr point has distance = 0?
		return distanceToNextPoint;
	}

	/**
	 * Returns the {@link FormOfWay} of this {@link LocationReferencePoint}
	 * 
	 * @return the form of way of this location reference point
	 */
	public FormOfWay getFormOfWay() {
		return formOfWay;
	}

	/**
	 * Returns the {@link FunctionalRoadClass} of this
	 * {@link LocationReferencePoint}
	 * 
	 * @return the functional road class of this location reference point
	 */
	public FunctionalRoadClass getFunctionalRoadClass() {
		return functionalRoadClass;
	}

	/**
	 * The <code>LFRCNP</code> (<b>lowest functional road class</b>) is the
	 * lowest FRC value which appears in the location reference path between two
	 * consecutive LR-points. This information could be used to limit the number
	 * of road classes which need to be scanned during the decoding. The highest
	 * FRC value is 0 and the lowest possible FRC value is valued with 7.
	 * 
	 * @see FunctionalRoadClass
	 * 
	 * @return the lowest functional road class used between this and the
	 *         consecutive location reference point
	 */
	public FunctionalRoadClass getLowestFRCToNextPoint() {
		return lowestFRCToNextPoint;
	}

}
