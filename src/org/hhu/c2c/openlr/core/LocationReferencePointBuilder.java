package org.hhu.c2c.openlr.core;

import org.hhu.c2c.openlr.geo.Coordinate;

public class LocationReferencePointBuilder {

	public static LocationReferencePoint newLocationReferencePoint (Coordinate coordinate,
			FunctionalRoadClass frc, FormOfWay fow, Bearing bearing) {
		return new LocationReferencePoint(coordinate, frc, fow, bearing);
	}
	
	public static LocationReferencePoint newLocationReferencePoint (Coordinate coordinate,
			FunctionalRoadClass frc, FormOfWay fow, FunctionalRoadClass lfrcnp,
			Bearing bearing, Distance distance) {
		return new LocationReferencePoint(coordinate, frc, fow, lfrcnp, bearing, distance);
	}
	
}
