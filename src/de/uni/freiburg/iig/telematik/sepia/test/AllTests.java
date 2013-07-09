package de.uni.freiburg.iig.telematik.sepia.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.test.CPNFlowRelationTest;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.test.CPNPlaceTest;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.test.CPNTest;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.test.CPNTransitionTest;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.test.FiringRuleTest;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.test.AbstractCWNUtilsTest;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.test.CWNTest;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.test.CWNTransitionTest;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.test.AnalysisContextTest;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.test.DeclassificationTransitionTest;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.test.IFNetPlaceTest;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.test.LabelingTest;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.test.RegularIFNetTransitionTest;


@RunWith(Suite.class)
@SuiteClasses({FiringRuleTest.class, CPNFlowRelationTest.class, CPNTransitionTest.class, CPNPlaceTest.class, CPNTest.class, CWNTest.class, CWNTransitionTest.class, AbstractCWNUtilsTest.class, LabelingTest.class, AnalysisContextTest.class, RegularIFNetTransitionTest.class, DeclassificationTransitionTest.class, IFNetPlaceTest.class })
public class AllTests {

}
