package org.hhu.c2c.test;

import org.hhu.c2c.openlr.core.BearingTest;
import org.hhu.c2c.openlr.core.DecoderTest;
import org.hhu.c2c.openlr.core.DistanceTest;
import org.hhu.c2c.openlr.core.EncoderTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs all tests.
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ BearingTest.class, DecoderTest.class, DistanceTest.class,
		EncoderTest.class

})
public class AllTests {
	// intentionally left empty
}
