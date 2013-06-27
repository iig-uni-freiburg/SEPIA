package petrinet.cpn.test;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.invation.code.toval.types.Multiset;
import de.invation.code.toval.validate.ParameterException;

import petrinet.cpn.CPN;
import petrinet.cpn.CPNFlowRelation;
import petrinet.cpn.CPNMarking;
import petrinet.cpn.CPNPlace;
import petrinet.cpn.CPNTransition;

public class CPNPlaceTest {
	
	
	/**
	 * This are the test cases for the class CPNPlace and AbstractCPNPlace.
	 * @author boehr
	 *
	 */ 

	
	//Test the constructors of CPNPlace
	@Test
	public void testCPNPlaceConstructor() throws ParameterException {
		
		//create some places
		CPNPlace p0 = null;
		CPNPlace p1 = null;
		try {
			p0 = new CPNPlace("name_p0");
			p1 = new CPNPlace("name_p1", "label_p1");
		} catch (ParameterException e) {
			fail("Constructor failed!");
			e.printStackTrace();
		}
		
		//Check that the name an label are set correctly at first
		assertEquals(p0.getName(),"name_p0");
		assertEquals(p1.getName(),"name_p1");
		assertEquals(p1.getLabel(),"label_p1");
	}
	
	
	
	//Add tokens to a place and check the place contains the tokens
	@Test
	public void testCPNPlaceAddTokens() throws ParameterException{
				
		
	    //Create Place	
		 CPNPlace p1 = new CPNPlace("name_p1", "label_p1");		 
		 boolean yes = p1.hasEmptyState(); 
		 assertTrue(yes);
		
		Multiset<String> origPlaceMarking = p1.getState(); 
		
		//The marking of a new place should be empty!
		assertTrue(origPlaceMarking.isEmpty());
		
		//Add some tokens to the placeMarking
		//3 red
		//1 green
		//2 orange
		//1 black 
		//and add 5 of each color
		Multiset<String> newPlaceMarking = new Multiset<String>();
		newPlaceMarking.add("red");
		newPlaceMarking.incMultiplicity("red");
		newPlaceMarking.incMultiplicity("red");
		newPlaceMarking.addAll("green", "orange", "black");
		newPlaceMarking.incMultiplicity("orange");
		newPlaceMarking.addScalar(5);		
		p1.setState(newPlaceMarking);
		
		//The amount of tokens should be 27
		assertEquals(27, p1.getTokenCount());
		
		//check whether the amount of tokens can be doubled
		boolean canConsume = p1.canConsume(newPlaceMarking);
		assertTrue(canConsume);
		 
		//set the capacity of red to 15
		p1.setColorCapacity("red", 15);
		//check whether the amount of tokens can still be doubled
		
		try{
			p1.canConsume(newPlaceMarking);
			fail("An unconsumable amount of tokens is not detected");
		}		 catch (ParameterException e) {		
		}

		//check the state is not empty 
		boolean no = p1.hasEmptyState(); 
		assertFalse(no);
		
		
		//Reset the place to be empty
		p1.setEmptyState();		 
		assertTrue(p1.hasEmptyState());
	}
	
	//Set some color capacities and see whether the place knows about them
	@Test
	public void testCPNPlaceSetCapacities() throws ParameterException {
		
	    //Create Place	
		CPNPlace p1 = new CPNPlace("name_p1", "label_p1");
		
		//set a color capacity
		p1.setColorCapacity("red", 2);		
		//The capacity should be 2 now 
		assertEquals(2, p1.getCapacity());
		//set to the same capacity again
		p1.setColorCapacity("red", 2);		
		//The capacity should be 2 now 
		assertEquals(2, p1.getCapacity());
		//change the capacity of red
		p1.setColorCapacity("red", 3);
		assertEquals(3, p1.getCapacity());
	
		//set a further color capacity
		p1.setColorCapacity("green", 3);				 
		//The capacity should be 6 now
		assertEquals(6, p1.getCapacity());
			
	} 
		
	//Remove some capacities
	@Test
	public void testCPNPlaceRemoveCapacities() throws ParameterException {
		
	    //Create Place	
		CPNPlace p1 = new CPNPlace("name_p1", "label_p1");
		
		//remove a non exiting color 
		p1.removeColorCapacity("unknownColor");
		//The capacity should still be -1 i.e. unbounded
		assertEquals(-1, p1.getCapacity());	 
		
		//Check whether there is still no capacity ristriction
		boolean no = p1.hasCapacityRestriction("unknownColor");
		assertFalse(no);
		 
		//set a capacity
		p1.setColorCapacity("green", 2);
		p1.setColorCapacity("red", 1);
		assertEquals(3, p1.getCapacity());	
		
		//Check whether there is A capacity ristriction
		boolean yes = p1.hasCapacityRestriction("green");
		assertTrue(yes);
		
		//remove capacity of green
		p1.removeColorCapacity("green");
		assertEquals(0, p1.getColorCapacity("green"));
		
		//remove all the capacities and check whether the place gets unbounded
		p1.removeColorCapacity("red");		
		assertEquals(-1, p1.getCapacity());
	}
	 
	
		//check setting a place to an empty state
		@Test
		public void testSetEmptyState() throws ParameterException {
			//Create place
			CPNPlace p1 = new CPNPlace("name_p1");
			Multiset<String> placeMarking = new Multiset<String>();
			
			//Set the capacity to 2 and add two tokens
			p1.setColorCapacity("green", 2);
			placeMarking.addAll("green", "green");
			p1.setState(placeMarking);
			assertTrue(p1.getState().contains("green"));
			
			
			//set the place to an empty state
			p1.setEmptyState();
			assertFalse(p1.getState().contains("green"));
			assertTrue(p1.getState().isEmpty());
			
		}
	
	
	//check the capacity of unused colors is zero
	@Test
	public void testCPNPlaceEmptyCapacities() throws ParameterException {

	    //Create Place	
		CPNPlace p1 = new CPNPlace("name_p1", "label_p1");
		
		//set a capacity
		p1.setColorCapacity("green", 1);
		
		//Check color capacity values
		assertEquals(0, p1.getColorCapacity("pink"));
								
	}
	
	
	//Test what happens if the capacity is set to a value lower than the tokens
	//actually contained in the place
	@Test
	public void testCPNPlaceSetLowCpacity() throws ParameterException{
		
		//Create place
		CPNPlace p1 = new CPNPlace("name_p1");
		
		//Get the place marking
		Multiset<String> newPlaceMarking = new Multiset<String>();
		//Change it to contain 15 Tokens
		newPlaceMarking.addAll("green", "orange", "black");
		newPlaceMarking.addScalar(4);
		p1.setState(newPlaceMarking);
				
		//Check that the amount of tokens is correct i.e. 15
		assertEquals(15, p1.getTokenCount());
					
		//set a color capacity of less than 5 tokens for green
		try{
		p1.setColorCapacity("green", 2);
			fail("A too low capacity got accepted by the place!");
		} catch (ParameterException e) {		
		}
		
		//What should happen if the new set capacity is smaller than the 
		//actual amount of tokens already in the place?
		assertEquals(-1, p1.getCapacity());		
	}
		
	
	
	//Try to add more tokens than the capacity allows
	@Test
	public void testCPNPlaceAddToManyTokens() throws ParameterException {
		

		//Create place
		CPNPlace p1 = new CPNPlace("name_p1");
		Multiset<String> placeMarking = new Multiset<String>();
		
		//Set the capacity to 2 and add two tokens
		p1.setColorCapacity("green", 2);
		placeMarking.addAll("green", "green");
		p1.setState(placeMarking);
		assertEquals(2, p1.getTokenCount());
		assertEquals(2, p1.getColorCapacity("green"));
						
		
		//Add more tokens than the capacity allows
		//placeMarking.addAll("green", "green");
		Multiset<String> placeMarking2 = new Multiset<String>();
		placeMarking2.addAll("green", "green", "green");
		try {
			p1.setState(placeMarking2);
			fail("Invalid state accepted by place!");
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		assertEquals(2, p1.getTokenCount());
					
								
	}
	
	
	
	    //test method canConsume
		@Test
		public void testcanConsume() throws ParameterException {
			
			//Create place
			CPNPlace p1 = new CPNPlace("name_p1");
			Multiset<String> placeMarking = new Multiset<String>();
			
			//Set the capacity of green to 2 and add two green tokens
			p1.setColorCapacity("green", 2);
			placeMarking.addAll("green", "green");			
			p1.setState(placeMarking);
		
			//The place cannot consume one more tokens
			assertFalse(p1.canConsume(new Multiset<String>("green")));
			assertFalse(p1.canConsume(new Multiset<String>("black")));
			
			//Set the color capacity to three
			p1.setColorCapacity("green", 3);
			//The place can now consume one more green tokens
			assertTrue(p1.canConsume(new Multiset<String>("green")));
			assertFalse(p1.canConsume(new Multiset<String>("black")));
		}
	
	
			
	

}
