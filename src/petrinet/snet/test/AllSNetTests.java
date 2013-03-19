package petrinet.snet.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AnalysisContextTest.class, LabelingTest.class, AnalysisContextTest.class, RegularSNetTransitionTest.class, DeclassificationTransitionTest.class })
public class AllSNetTests {

}
