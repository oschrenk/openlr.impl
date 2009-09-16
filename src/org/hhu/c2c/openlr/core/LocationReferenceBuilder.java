package org.hhu.c2c.openlr.core;

import java.util.List;

import org.hhu.c2c.openlr.geo.CoordinateFactory;

/**
 * The <b>location reference factory</b> is used for creating and receiving byte
 * arrays describing location references. It does so by following the physical
 * data structure and not imposing any further logic on it.
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version 1.0, 2009-09-20
 * 
 */
public class LocationReferenceBuilder {

	/**
	 * Holds the singleton instance
	 */
	private static final LocationReferenceBuilder instance = new LocationReferenceBuilder();

	/**
	 * Holds the coordinate factory
	 */
	private final CoordinateFactory coordinateFactory;

	/**
	 * Private constructor to prohibit instantiation
	 */
	private LocationReferenceBuilder() {
		this.coordinateFactory = CoordinateFactory.getInstance();
	}

	/**
	 * Returns the singleton instance of the location reference factory
	 * 
	 * @return an instance of the location reference factory
	 */
	public static LocationReferenceBuilder getInstance() {
		return instance;
	}

	public static LocationReference newLocationReference (boolean areaFlag, boolean attributeFlag,
			byte version, List<LocationReferencePoint> points,
			Distance positiveOffset, Distance negativeOffset) {
		return new LocationReference(areaFlag, attributeFlag, version, points, positiveOffset, negativeOffset);
	}

}
