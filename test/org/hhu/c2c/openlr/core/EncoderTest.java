package org.hhu.c2c.openlr.core;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.hhu.c2c.openlr.io.Encoder;
import org.hhu.c2c.openlr.util.ValidationException;
import org.junit.Test;


/**
 * Tests {@link Encoder}
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
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
			
		} catch (ValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
