package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import petrinet.cpn.test.CPNFlowRelationTest;
import petrinet.cpn.test.CPNPlaceTest;
import petrinet.cpn.test.CPNTest;
import petrinet.cpn.test.CPNTransitionTest;
import petrinet.cpn.test.FiringRuleTest;
import petrinet.cwn.test.AbstractCWNUtilsTest;
import petrinet.cwn.test.CWNTest;
import petrinet.cwn.test.CWNTransitionTest;

@RunWith(Suite.class)
@SuiteClasses({FiringRuleTest.class, CPNFlowRelationTest.class, CPNTransitionTest.class, CPNPlaceTest.class, CPNTest.class, CWNTest.class, CWNTransitionTest.class, AbstractCWNUtilsTest.class})
public class AllTests {

}
