package org.hhu.c2c.openlr.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.hhu.c2c.openlr.io.Decoder;
import org.hhu.c2c.openlr.io.Encoder;
import org.hhu.c2c.openlr.util.LocationReferenceException;
import org.junit.Test;

/**
 * Tests {@link Encoder}
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * 
 */
public class DecoderTest {

	/**
	 * Test the {@link Decoder} with the example from the technical report. It
	 * first encodes the exact data, converts the resulting byte array into a
	 * location reference and compares that to the directly decoded example.
	 */
	@Test
	public void testDecoderWithExampleFromTechnicalReport() {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			new Encoder().write(out, Example.asLocationReference());
			LocationReference lr = new Decoder().decode(out.toByteArray());
			assertEquals(new Decoder().decode(Example.asBinaryInputStream()), lr);
		} catch (LocationReferenceException e) {
			fail(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			fail(e.getMessage());
		}
	}

}
