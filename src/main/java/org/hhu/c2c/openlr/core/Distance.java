package org.hhu.c2c.openlr.core;

import org.hhu.c2c.openlr.l10n.Messages;
import org.hhu.c2c.openlr.util.LocationReferenceException;

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
 * 
 */
public class Distance implements Comparable<Distance> {

	/*
	 * Breaks "Single Responsibility Principle" as the class is now also,
	 * responsible for its own creation. But as this class is very static by
	 * contract (the protocol definition of Open LR won't change that fast), we
	 * can accept that.
	 */
	/**
	 * Returns a new distance value by passing a byte value. The whole byte is
	 * used to compute the distance. Each bit value represents an interval of
	 * 56.8 meter in compliance with the data format rules.
	 * 
	 * @param distance
	 *            the byte value
	 * @return a new distance
	 * @throws LocationReferenceException
	 *             if the distance violates the first rule of the data format
	 *             rules
	 */
	public static Distance newDistanceFromByteRepresentation(final int distance)
			throws LocationReferenceException {
		return newDistanceFromMetricRepresentation((int) (distance * Rules.ONE_BIT_DISTANCE));
	}

	/**
	 * Returns a new distance value by passing a value given in meter.
	 * 
	 * @param distance
	 *            the distance in meter
	 * @return a new distance
	 * @throws LocationReferenceException
	 *             if the distance violates the first rule of the data format
	 *             rules
	 */
	public static Distance newDistanceFromMetricRepresentation(
			final int distance) throws LocationReferenceException {
		if (distance > Rules.MAXIMUM_DISTANCE_BETWEEN_TWO_LR_POINTS) {
			throw new LocationReferenceException(
					Messages
							.getString(
									"Distance.Exception.OVER_MAXIMUM", Rules.MAXIMUM_DISTANCE_BETWEEN_TWO_LR_POINTS)); //$NON-NLS-1$
		}

		if (distance < 0) {
			throw new LocationReferenceException(Messages
					.getString("Distance.Exception.ONLY_POSITIVE")); //$NON-NLS-1$
		}

		return new Distance(distance);
	}

	/**
	 * Holds the distance.
	 */
	final private int distance;

	/**
	 * Creates a new distance by passing an integer value of the real distance
	 * (in compliance with the first and second rule of the data format rules).
	 * 
	 * @param distance
	 *            the distance in meter
	 */
	protected Distance(final int distance) {
		this.distance = distance;
	}

	/**
	 * Returns the byte representation of the distance. All 8 bits of a byte are
	 * used to encode a distance.
	 * 
	 * @return the byte representation using the full 8 bits of a byte
	 */
	public int getByteRepresentation() {
		return 0xFF & (int) (distance / Rules.ONE_BIT_DISTANCE);
	}

	/**
	 * Returns the distance in meter.
	 * 
	 * @return the distance in meter
	 */
	public int getDistance() {
		return distance;
	}

	@Override
	public int compareTo(Distance o) {
		if (getDistance() < o.getDistance())
			return -1;
		if (getDistance() > o.getDistance())
			return 1;
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + distance;
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
		Distance other = (Distance) obj;
		if (distance != other.distance)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return distance + " m";
	}

}
