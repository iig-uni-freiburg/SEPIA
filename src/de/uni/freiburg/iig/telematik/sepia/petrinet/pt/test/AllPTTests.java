package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ PTPlaceTest.class, PTTransitionTest.class, PTFlowRelationTest.class, PTNetTest.class })
public class AllPTTests {
}
