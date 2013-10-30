package de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suit combining all PNML CPN parser tests.
 * 
 * @author Adrian Lange
 */
@RunWith(Suite.class)
@SuiteClasses({ PNMLCPNParserTest.class, PNMLCPNParserComponentTest.class })
public class AllPNMLCPNParserTests {
}
