package de.uni.freiburg.iig.telematik.sepia.parser.pnml;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.uni.freiburg.iig.telematik.sepia.parser.pnml.cpn.AllPNMLCPNParserTests;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.cwn.AllPNMLCWNParserTests;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.ifnet.AllPNMLIFNetParserTests;
import de.uni.freiburg.iig.telematik.sepia.parser.pnml.pt.AllPNMLPTNetParserTests;

/**
 * Test suit combining all PNML parser tests.
 * 
 * @author Adrian Lange
 */
@RunWith(Suite.class)
@SuiteClasses({ AllPNMLPTNetParserTests.class, AllPNMLCPNParserTests.class, AllPNMLCWNParserTests.class, AllPNMLIFNetParserTests.class })
public class AllPNMLParserTests {
}
