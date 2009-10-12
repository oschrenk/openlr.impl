package org.hhu.c2c.openlr.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.hhu.c2c.openlr.util.LocationReferenceException;
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
	 * 
	 * @throws LocationReferenceException
	 */
	@Test(expected = LocationReferenceException.class)
	public void testConstructing() throws LocationReferenceException {
		Distance.newDistanceFromByteRepresentation(-1);
		Distance.newDistanceFromByteRepresentation(Rules.MAXIMUM_DISTANCE_BETWEEN_TWO_LR_POINTS + 1);
	}

	/**
	 * Tests if the distance can be converted between different representations.
	 * As the interval length is 58.6 there should be at max 59 apart, as it is
	 * not round but just the decimal places cut off.
	 */
	@Test
	public void testRepresentationsConversions() {
		Distance distance;

		for (int i = 67; i < 15000; i++) {
			distance = new Distance(i);
			try {
				assertEquals("Expected to be equal for distance " + i, distance
						.getDistance(), Distance.newDistanceFromByteRepresentation(
						distance.getByteRepresentation()).getDistance(), 59f);
			} catch (LocationReferenceException e) {
				fail("Should never happen");
				e.printStackTrace();
			}
		}
	}
}
