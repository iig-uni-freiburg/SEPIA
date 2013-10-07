package de.uni.freiburg.iig.telematik.sepia.parser.pnml.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suit combining all PNML parser tests.
 * 
 * @author Adrian Lange
 */
@RunWith(Suite.class)
@SuiteClasses({ PNMLPTNetParserTest.class, PNMLPTNetParserComponentTest.class })
public class AllPNMLParserTests {
}
