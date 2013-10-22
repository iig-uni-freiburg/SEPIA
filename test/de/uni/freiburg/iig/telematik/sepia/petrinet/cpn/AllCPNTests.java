package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ FiringRuleTest.class, CPNFlowRelationTest.class, CPNTransitionTest.class, CPNPlaceTest.class, CPNTest.class })
public class AllCPNTests {
}