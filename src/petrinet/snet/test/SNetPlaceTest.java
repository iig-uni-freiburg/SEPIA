/**
 * 
 */
package petrinet.snet.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import petrinet.snet.SNetPlace;
import types.Multiset;
import validate.ParameterException;

/**
 * @author boehr
 *
 */
public class SNetPlaceTest {



	/**
	 * Test method for {@link petrinet.snet.SNetPlace#SNetPlace(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testSNetPlaceStringString() {
		
		SNetPlace p = null;
		try {
			p = new SNetPlace("pName", "pLabel");
		} catch (ParameterException e) {
			fail("Cannot create SNetPlace");
		}
		
		assertEquals("pName", p.getName());
		assertEquals("pLabel", p.getLabel());

	}

	/**
	 * Test method for {@link petrinet.snet.SNetPlace#SNetPlace(java.lang.String)}.
	 */
	@Test
	public void testSNetPlaceString() {
		SNetPlace p = null;
		try {
			p = new SNetPlace("pNameAndLabel");
		} catch (ParameterException e) {
			fail("Cannot create SNetPlace");
		}
		
		assertEquals("pNameAndLabel", p.getName());
		assertEquals(p.getName(), p.getLabel());
	}
	
	
	/**
	 * Test method for {@link petrinet.snet.SNetPlace#toPNML()}.
	 */
	@Test
	public void testToPNML() {
		SNetPlace p = null;
		try {
			p = new SNetPlace("pName", "pLabel");
		} catch (ParameterException e) {
			fail("Cannot create SNetPlace");
		}
		Multiset<String> initialPlaceMarking = new Multiset<String>();
		try {
			initialPlaceMarking.add("black");
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNull(p.toPNML(initialPlaceMarking));
	}

}
