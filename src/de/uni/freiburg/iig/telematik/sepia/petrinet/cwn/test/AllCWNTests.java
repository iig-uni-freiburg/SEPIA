package de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CWNTest.class, CWNPlaceTest.class, CWNTransitionTest.class, CWNFlowRelationTest.class, AbstractCWNUtilsTest.class })
public class AllCWNTests {
}