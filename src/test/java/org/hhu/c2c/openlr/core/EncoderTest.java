package org.hhu.c2c.openlr.core;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.hhu.c2c.openlr.io.Encoder;
import org.hhu.c2c.openlr.util.LocationReferenceException;
import org.junit.Test;


/**
 * Tests {@link Encoder}
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * 
 */
public class EncoderTest {

	/**
	 *
	 */
	@Test
	public void testExampleFromTechnicalReport() {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			new Encoder().write(out, Example.asLocationReference());
			byte[] actual = out.toByteArray();
			
			for (int i = 0; i < Example.asBinaryInputStream().length;i++) {
				assertEquals(Example.asBinaryInputStream()[i],actual[i]);
			}
			
		} catch (LocationReferenceException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
