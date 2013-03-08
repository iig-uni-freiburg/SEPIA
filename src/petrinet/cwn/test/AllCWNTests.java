package petrinet.cwn.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ CWNTest.class, CWNTransitionTest.class, AbstractCWNUtilsTest.class })
public class AllCWNTests {

}
