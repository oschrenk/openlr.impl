package org.hhu.c2c.openlr.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests {@link Distance}
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
 * 
 */
public class DistanceTest {

	/**
	 * Tests various edge cases for constructing
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testConstructing() {
		new Distance(-1);
		new Distance(Rules.MAXIMUM_DISTANCE_BETWEEN_TWO_LR_POINTS + 1);
	}

	/**
	 * Tests if the distance can be converted between different representations.
	 * There should be the same.
	 */
	@Test
	public void testRepresentationsConversions() {
		Distance distance;

		distance = new Distance(0);
		assertEquals("Expected to be equal", distance.getDistance(), Distance
				.newDistance(distance.getByteRepresentation()).getDistance());

		distance = new Distance(1000);
		assertEquals("Expected to be equal", distance.getDistance(), Distance
				.newDistance(distance.getByteRepresentation()).getDistance());

		distance = new Distance(5000);
		assertEquals("Expected to be equal", distance.getDistance(), Distance
				.newDistance(distance.getByteRepresentation()).getDistance());

		distance = new Distance(15000);
		assertEquals("Expected to be equal", distance.getDistance(), Distance
				.newDistance(distance.getByteRepresentation()).getDistance());
	}
}
