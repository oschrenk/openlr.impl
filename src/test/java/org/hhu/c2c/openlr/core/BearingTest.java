package org.hhu.c2c.openlr.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test cases for {@link Bearing}
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * 
 */
public class BearingTest {

	/**
	 * Constructs various Bearing, tests normalizing
	 */
	@Test
	public void testBearing() {
		Bearing bearing;
		// test normal cases
		bearing = new Bearing(350.0f);
		assertEquals(350.0, bearing.getBearing(), 0);

		bearing = new Bearing(0.0f);
		assertEquals(0.0, bearing.getBearing(), 0);

		// test edge cases
		bearing = new Bearing(360.0f);
		assertEquals(0, bearing.getBearing(), 0);

		// test negative values
		bearing = new Bearing(-350.0f);
		assertEquals(10.0, bearing.getBearing(), 0);

		bearing = new Bearing(-10.0f);
		assertEquals(350.0, bearing.getBearing(), 0);

		// test big values
		bearing = new Bearing(370.0f);
		assertEquals(10.0, bearing.getBearing(), 0);

		bearing = new Bearing(720.0f);
		assertEquals(0.0, bearing.getBearing(), 0);

	}

	/**
	 * Tests if the byte representations are matching up with the ones given in
	 * the technical paper
	 */
	@Test
	public void testGetByteRepresentation() {
		Bearing bearing;

		// test normal cases
		bearing = new Bearing(0.0f);
		assertEquals("Expected to be equal", 0l, bearing
				.getByteRepresentation());

		bearing = new Bearing(11.25f);
		assertEquals("Expected to be equal", 1l, bearing
				.getByteRepresentation());
		bearing = new Bearing(22.5f);
		assertEquals("Expected to be equal", 2l, bearing
				.getByteRepresentation());
		bearing = new Bearing(33.75f);
		assertEquals("Expected to be equal", 3l, bearing
				.getByteRepresentation());
		bearing = new Bearing(45.00f);
		assertEquals("Expected to be equal", 4l, bearing
				.getByteRepresentation());
		bearing = new Bearing(56.25f);
		assertEquals("Expected to be equal", 5l, bearing
				.getByteRepresentation());
		bearing = new Bearing(67.5f);
		assertEquals("Expected to be equal", 6l, bearing
				.getByteRepresentation());
		bearing = new Bearing(78.75f);
		assertEquals("Expected to be equal", 7l, bearing
				.getByteRepresentation());
		bearing = new Bearing(90.0f);
		assertEquals("Expected to be equal", 8l, bearing
				.getByteRepresentation());
		bearing = new Bearing(101.25f);
		assertEquals("Expected to be equal", 9l, bearing
				.getByteRepresentation());
		bearing = new Bearing(112.5f);
		assertEquals("Expected to be equal", 10l, bearing
				.getByteRepresentation());
		bearing = new Bearing(123.75f);
		assertEquals("Expected to be equal", 11l, bearing
				.getByteRepresentation());
		bearing = new Bearing(135.0f);
		assertEquals("Expected to be equal", 12l, bearing
				.getByteRepresentation());
		bearing = new Bearing(146.25f);
		assertEquals("Expected to be equal", 13l, bearing
				.getByteRepresentation());
		bearing = new Bearing(157.5f);
		assertEquals("Expected to be equal", 14l, bearing
				.getByteRepresentation());
		bearing = new Bearing(168.75f);
		assertEquals("Expected to be equal", 15l, bearing
				.getByteRepresentation());
		bearing = new Bearing(180.0f);
		assertEquals("Expected to be equal", 16l, bearing
				.getByteRepresentation());
		bearing = new Bearing(191.25f);
		assertEquals("Expected to be equal", 17l, bearing
				.getByteRepresentation());
		bearing = new Bearing(202.5f);
		assertEquals("Expected to be equal", 18l, bearing
				.getByteRepresentation());
		bearing = new Bearing(213.75f);
		assertEquals("Expected to be equal", 19l, bearing
				.getByteRepresentation());
		bearing = new Bearing(225.0f);
		assertEquals("Expected to be equal", 20l, bearing
				.getByteRepresentation());
		bearing = new Bearing(236.25f);
		assertEquals("Expected to be equal", 21l, bearing
				.getByteRepresentation());
		bearing = new Bearing(247.5f);
		assertEquals("Expected to be equal", 22l, bearing
				.getByteRepresentation());
		bearing = new Bearing(258.75f);
		assertEquals("Expected to be equal", 23l, bearing
				.getByteRepresentation());
		bearing = new Bearing(270.0f);
		assertEquals("Expected to be equal", 24l, bearing
				.getByteRepresentation());
		bearing = new Bearing(281.25f);
		assertEquals("Expected to be equal", 25l, bearing
				.getByteRepresentation());
		bearing = new Bearing(292.5f);
		assertEquals("Expected to be equal", 26l, bearing
				.getByteRepresentation());
		bearing = new Bearing(303.75f);
		assertEquals("Expected to be equal", 27l, bearing
				.getByteRepresentation());
		bearing = new Bearing(315.0f);
		assertEquals("Expected to be equal", 28l, bearing
				.getByteRepresentation());
		bearing = new Bearing(326.25f);
		assertEquals("Expected to be equal", 29l, bearing
				.getByteRepresentation());
		bearing = new Bearing(337.5f);
		assertEquals("Expected to be equal", 30l, bearing
				.getByteRepresentation());
		bearing = new Bearing(348.75f);
		assertEquals("Expected to be equal", 31l, bearing
				.getByteRepresentation());

		// test edge cases
		bearing = new Bearing(360.0f);
		assertEquals("Expected to be equal", 0l, bearing
				.getByteRepresentation());
	}

}
