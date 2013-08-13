/**
 * 
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import de.invation.code.toval.validate.ParameterException;
import de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace;


/**
 * @author boehr
 *
 */
public class IFNetPlaceTest {



	/**
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace#SNetPlace(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testSNetPlaceStringString() {
		
		IFNetPlace p = null;
		try {
			p = new IFNetPlace("pName", "pLabel");
		} catch (ParameterException e) {
			fail("Cannot create SNetPlace");
		}
		
		assertEquals("pName", p.getName());
		assertEquals("pLabel", p.getLabel());

	}

	/**
	 * Test method for {@link de.uni.freiburg.iig.telematik.sepia.petrinet.ifnet.IFNetPlace#SNetPlace(java.lang.String)}.
	 */
	@Test
	public void testSNetPlaceString() {
		IFNetPlace p = null;
		try {
			p = new IFNetPlace("pNameAndLabel");
		} catch (ParameterException e) {
			fail("Cannot create SNetPlace");
		}
		
		assertEquals("pNameAndLabel", p.getName());
		assertEquals(p.getName(), p.getLabel());
	}

}
