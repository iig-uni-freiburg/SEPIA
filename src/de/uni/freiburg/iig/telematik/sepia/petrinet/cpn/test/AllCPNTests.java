package de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.test.CWNTest;


@RunWith(Suite.class)
@SuiteClasses({FiringRuleTest.class, CPNFlowRelationTest.class, CPNTransitionTest.class, CPNPlaceTest.class, CPNTest.class, CWNTest.class })
public class AllCPNTests {

}
