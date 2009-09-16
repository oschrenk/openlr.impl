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
 * @version 1.0, 2009-09-20
 * 
 */
public class LocationReferencePoint {

	/**
	 * {@link Coordinate}
	 */
	private Coordinate coordinate;

	/**
	 * {@link Bearing}
	 */
	private Bearing bearing;

	/**
	 * Holds the {@link Distance} to the the next {@link LocationReferencePoint}
	 */
	private Distance distanceToNextPoint;

	/**
	 * {@link FunctionalRoadClass}
	 */
	private FunctionalRoadClass functionalRoadClass;

	/**
	 * {@link FormOfWay}
	 */
	private FormOfWay formOfWay;

	/**
	 * {@link #getLowestFRCToNextPoint()}
	 */
	private FunctionalRoadClass lowestFRCToNextPoint;

	/**
	 * Constructs a new location reference point, wihtout a value for lfrcnp and
	 * distance, indicating that it is used to describe the last point of a
	 * location reference.
	 * 
	 * @param coordinate the coordinate
	 * @param frc the functional road class
	 * @param fow the form of way
	 * @param bearing the bearing
	 */
	protected LocationReferencePoint(Coordinate coordinate,
			FunctionalRoadClass frc, FormOfWay fow, Bearing bearing) {
		// TODO handle last point differently? DNP=0? LFRCNP= ????
		this.coordinate = coordinate;
		this.functionalRoadClass = frc;
		this.formOfWay = fow;
		this.bearing = bearing;
	}

	/**
	 * Constructs a new location reference point with all the neccesary parameters.
	 * 
	 * @param coordinate the coordinate
	 * @param frc the functional road class
	 * @param fow the form of way
	 * @param lfrcnp the lowest functional road class to the next point
	 * @param bearing the bearing
	 * @param distance the distance
	 */
	protected LocationReferencePoint(Coordinate coordinate,
			FunctionalRoadClass frc, FormOfWay fow, FunctionalRoadClass lfrcnp,
			Bearing bearing, Distance distance) {
		this.coordinate = coordinate;
		this.functionalRoadClass = frc;
		this.formOfWay = fow;
		this.lowestFRCToNextPoint = lfrcnp;
		this.bearing = bearing;
		this.distanceToNextPoint = distance;
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
	 * Returns the {@link FunctionalRoadClass} of this
	 * {@link LocationReferencePoint}
	 * 
	 * @return the functional road class of this location reference point
	 */
	public FunctionalRoadClass getFunctionalRoadClass() {
		return functionalRoadClass;
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

	/**
	 * Returns the {@link Bearing} of this {@link LocationReferencePoint}
	 * 
	 * @return the bearing of this location reference point
	 */
	public Bearing getBearing() {
		return bearing;
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

}