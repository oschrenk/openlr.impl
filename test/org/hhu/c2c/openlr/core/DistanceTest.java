package org.hhu.c2c.openlr.core;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests {@link Distance}
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version 1.0, 2009-09-20
 * 
 */
public class DistanceTest {

	/**
	 * Tests various edge cases for constructing
	 */
	@Test
	public void testConstructing() {
		try {
			new Distance(-1);
			fail("Should fail. No negative distance allowed");
		} catch (IllegalArgumentException e) {
		}

		try {
			new Distance(Rules.MAXIMUM_DISTANCE_BETWEEN_TWO_LR_POINTS + 1);
			fail("Should fail. Distance should be allowed under maximum distance.");
		} catch (IllegalArgumentException e) {
		}

	}

	/**
	 * Tests if the distance can be converted between different representations.
	 * There should be the same.
	 */
	@Test
	public void testRepresentationsConversions() {
		Distance d;

		d = new Distance(0);
		assertEquals(d.getDistance(), Distance.newDistance(
				d.getByteRepresentation()).getDistance());

		d = new Distance(1000);
		assertEquals(d.getDistance(), Distance.newDistance(
				d.getByteRepresentation()).getDistance());

		d = new Distance(5000);
		assertEquals(d.getDistance(), Distance.newDistance(
				d.getByteRepresentation()).getDistance());

		d = new Distance(15000);
		assertEquals(d.getDistance(), Distance.newDistance(
				d.getByteRepresentation()).getDistance());
	}
}
