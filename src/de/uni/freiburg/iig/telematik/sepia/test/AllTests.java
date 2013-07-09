package de.uni.freiburg.iig.telematik.sepia.test;

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
import petrinet.ifnet.test.AnalysisContextTest;
import petrinet.ifnet.test.DeclassificationTransitionTest;
import petrinet.ifnet.test.LabelingTest;
import petrinet.ifnet.test.RegularIFNetTransitionTest;
import petrinet.ifnet.test.IFNetPlaceTest;

@RunWith(Suite.class)
@SuiteClasses({FiringRuleTest.class, CPNFlowRelationTest.class, CPNTransitionTest.class, CPNPlaceTest.class, CPNTest.class, CWNTest.class, CWNTransitionTest.class, AbstractCWNUtilsTest.class, LabelingTest.class, AnalysisContextTest.class, RegularIFNetTransitionTest.class, DeclassificationTransitionTest.class, IFNetPlaceTest.class })
public class AllTests {

}
