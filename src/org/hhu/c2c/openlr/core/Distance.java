package org.hhu.c2c.openlr.core;

/**
 * The distance describes can be used to describe the distance between two
 * {@link LocationReferencePoint}s or to describe the
 * {@link LocationReference#getPositiveOffset()} or respectively the
 * {@link LocationReference#getNegativeOffset()}.
 * 
 * The physical data format defines an 8-bit representation. This representation
 * defines 255 intervals and in combination with the first rule of the data
 * format rules (maximum length between two consecutive LR-points is limited by
 * 15000m) each interval will have a length of 58.6 meters.
 * 
 * A distance is constructed by passing a distance value in meters (in
 * compliance with the first and second rule of the data format rules).
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version 1.0, 2009-09-20
 * 
 */
public class Distance {

	/**
	 * Holds the distance.
	 */
	private int distance;

	/**
	 * Creates a new distance by passing an integer value of the real distance
	 * (in compliance with the first and second rule of the data format rules).
	 * 
	 * @param distance
	 *            the distance in meter
	 * @throws IllegalArgumentException
	 *             if the distance violates the first rule of the data format
	 *             rules
	 */
	public Distance(int distance) {
		if (distance > Rules.MAXIMUM_DISTANCE_BETWEEN_TWO_LR_POINTS)
			throw new IllegalArgumentException("Distance too long. Maximum is "
					+ Rules.MAXIMUM_DISTANCE_BETWEEN_TWO_LR_POINTS + "m.");
		if (distance < 0)
			throw new IllegalArgumentException("Distance must be positive.");
		
		this.distance = distance;
	}

	/**
	 * Returns the byte representation of the distance. All 8 bits of a byte are
	 * used to encode a distance.
	 * 
	 * //TODO add tex formula for calculation of the byte value of the distance
	 * 
	 * @return the byte representation using the full 8 bits of a byte
	 */
	public byte getByteRepresentation() {
		return getDistance(distance);
	}

	/**
	 * Returns the distance in meter.
	 * 
	 * @return the distance in meter
	 */
	public int getDistance() {
		return getDistance(distance);
	}

	/**
	 * Returns the byte representation of the distance
	 * 
	 * @see #getByteRepresentation()
	 * 
	 * @param distance
	 *            the distance in meter
	 * @return the byte representation, using all 8 bit of the byte
	 */
	private byte getDistance(int distance) {
		return (byte) (distance / Rules.ONE_BIT_DISTANCE);
	}

	/*
	 * Breaks "Single Responsibility Principle" as the class is now also,
	 * responsible for its own creation. But as this class is very static by
	 * contract (the protocol definition of Open LR won't change that fast), we
	 * can accept that. Also we prohibit casses outside this package to access
	 * the method.
	 */
	/**
	 * Returns a new distance value by passing a byte value. The whole byte is
	 * used to compute the distance. Each bit value represents an intervall of
	 * 56.8 meter in compliance with the data format rules.
	 * 
	 * @param b
	 *            the byte value
	 * @return a new distance, measured in meter
	 */
	public static Distance newDistance(byte b) {
		return new Distance((int) (b * Rules.ONE_BIT_DISTANCE));
	}

}
