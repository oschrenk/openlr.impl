package org.hhu.c2c.openlr.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests {@link Distance}
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
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
	 * As the interval length is 58.6 there should be at max 59 apart, as it is
	 * not round but just the decimal places cut off.
	 */
	@Test
	public void testRepresentationsConversions() {
		Distance distance;

		for (int i = 0; i < 15000; i++) {
			distance = new Distance(i);
			assertEquals("Expected to be equal", distance.getDistance(),
					Distance.newDistance(distance.getByteRepresentation())
							.getDistance(), 59f);
		}
	}
}
