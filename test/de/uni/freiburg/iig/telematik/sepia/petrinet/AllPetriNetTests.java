package de.uni.freiburg.iig.telematik.sepia.petrinet;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import de.uni.freiburg.iig.telematik.sepia.petrinet.cpn.AllCPNTests;
import de.uni.freiburg.iig.telematik.sepia.petrinet.cwn.AllCWNTests;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.AllIFNetTests;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.AllPTTests;

@RunWith(Suite.class)
@SuiteClasses({ AllPTTests.class, AllCPNTests.class, AllCWNTests.class, AllIFNetTests.class })
public class AllPetriNetTests {
}
