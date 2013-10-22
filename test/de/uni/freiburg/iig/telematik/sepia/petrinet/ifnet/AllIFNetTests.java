package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AnalysisContextTest.class, LabelingTest.class, AnalysisContextTest.class, RegularIFNetTransitionTest.class, DeclassificationTransitionTest.class, IFNetPlaceTest.class, IFNetFlowRelationTest.class, IFNetTest.class })
public class AllIFNetTests {
}
