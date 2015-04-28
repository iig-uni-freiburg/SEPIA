package de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suit combining all PNML IF-net parser tests.
 * 
 * @author Adrian Lange
 */
@RunWith(Suite.class)
@SuiteClasses({ PNMLIFNetParserTest.class, PNMLIFNetParserComponentTest.class, PNMLIFNetAnalysisContextParserComponentTest.class, PNMLIFNetLabelingParserTest.class, PNMLIFNetLabelingParserComponentTest.class })
public class AllPNMLIFNetParserTests {
}
