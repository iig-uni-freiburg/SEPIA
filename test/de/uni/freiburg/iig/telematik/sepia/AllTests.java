package de.uni.freiburg.iig.telematik.sepia;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.uni.freiburg.iig.telematik.sepia.parser.AllParserTests;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AllPetriNetTests;

@RunWith(Suite.class)
@SuiteClasses({ AllPetriNetTests.class, AllParserTests.class })
public class AllTests {
}
