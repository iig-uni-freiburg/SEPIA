package de.uni.freiburg.iig.telematik.sepia.parser.pnml.pt;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.uni.freiburg.iig.telematik.sepia.parser.pnml.pt.PNMLPTNetParserComponentTest;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.pt.PNMLPTNetParserTest;

/**
 * Test suit combining all PNML P/T-net parser tests.
 * 
 * @author Adrian Lange
 */
@RunWith(Suite.class)
@SuiteClasses({ PNMLPTNetParserTest.class, PNMLPTNetParserComponentTest.class })
public class AllPNMLPTNetParserTests {
}
