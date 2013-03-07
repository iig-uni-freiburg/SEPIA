package petrinet.cwn.test;

import java.util.HashSet;
import java.util.Set;

import petrinet.cwn.CWN;
import petrinet.cwn.CWNFlowRelation;
import petrinet.cwn.CWNMarking;
import types.Multiset;
import validate.ParameterException;

public class CWNTestUtils {
	
    //Creates the standard cwn.
	//The standard cpn has four places and four transitions.
	//                	 -> t1
	//              	/       \
	//    p0 -> T0-> p1|         p2 -> t3 -> p3
	//              	\       /
	//               	 <- t2<-
	// 
	//p0: black
	static CWN createValidCWN(){
		
		CWN cwn = null;
		
		
		try {
		//Create places	
		Set<String>  places = new HashSet<String>();
		places.add("p0");
		places.add("p1");
		places.add("p2");
		places.add("p3");
		
		
		//create transitions
		Set<String>  transitions = new HashSet<String>();
		transitions.add("t0");
		transitions.add("t1");
		transitions.add("t2");
		transitions.add("t3");
				
		//create the the token colors used in the initial marking		
		Multiset<String> mset = new Multiset<String>();							
		mset.add("black");
		CWNMarking marking = new CWNMarking();
		marking.set("p0", mset);
											
		
		//create the cwn with one black token in P0
		cwn = new CWN(places, transitions, marking);
		
		//Set bounds for all places 
		//p0 contains only black			
		cwn.getPlace("p0").setColorCapacity("black", 2);
		//p1 contains black and green
		cwn.getPlace("p1").setColorCapacity("black", 2);
		cwn.getPlace("p1").setColorCapacity("green", 2);
		//p2 contains black and red
		cwn.getPlace("p2").setColorCapacity("black", 2);
		cwn.getPlace("p2").setColorCapacity("red", 2);
		//p3 contains black
		cwn.getPlace("p3").setColorCapacity("black", 2);
		
		
		
		//Add the flow relation					
		CWNFlowRelation f1 = cwn.addFlowRelationPT("p0", "t0", true);
		CWNFlowRelation f2 = cwn.addFlowRelationTP("t0", "p1", true);
		CWNFlowRelation f3 = cwn.addFlowRelationPT("p1", "t1", true);
		CWNFlowRelation f4 = cwn.addFlowRelationTP("t1", "p2", true);
		CWNFlowRelation f5 = cwn.addFlowRelationPT("p2", "t2", true);
		CWNFlowRelation f6 = cwn.addFlowRelationTP("t2", "p1", true);
		CWNFlowRelation f7 = cwn.addFlowRelationPT("p2", "t3", true);
		CWNFlowRelation f8 = cwn.addFlowRelationTP("t3", "p3", true);
			
		//configure flow reltions
		f2.addConstraint("green", 1);
		f3.addConstraint("green", 1);
		f4.addConstraint("red", 1);
		f5.addConstraint("red", 1);
		f6.addConstraint("green", 1);
		f7.addConstraint("red", 1);
			
			
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					
		return cwn;
	}
	

	
	
	//Creates a cwn with two output places
	//by adding a place to the standard cwn
	//                	 -> t1
	//              	/       \
	//    p0 -> T0-> p1|         p2 -> t3 -> p3
	//              	\       /       \
	//              	 <- t2<-          ->p5
	// 
	//p0: black
	static void addSecondOutputPlaceP5(CWN origCWN){
											
		
		try {
		//Add place			    
		origCWN.addPlace("p5");
		
		//Add flow relation								
		CWNFlowRelation f10 = origCWN.addFlowRelationTP("t3", "p5", true);
	
		
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
								
	}

	
	
	
	
    //Creates a cwn with two input places
	//by adding a place to the standard cwn
	//                	 -> t1
	//              	/       \
	//    p0 -> T0-> p1|         p2 -> t3 -> p3
	//        /     	\       /       
	//     p4       	 <- t2<-          
	// 
	//p0: black
	static void addSecondInputPlaceP4(CWN origCWN){
		
											
		try {
		//Add place	
	    origCWN.addPlace("p4");		    
		
		//Add two flow relation					
		CWNFlowRelation f9 = origCWN.addFlowRelationPT("p4", "t0", true);			
	
		//put a black token in p4
		Multiset<String> mp4 =  new Multiset<String>();
		mp4.add("black");
		origCWN.getMarking().set("p4", mp4);
		
		} catch (ParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}									
	}
	
	
	//Creates a petri net which does not create a token in the sink place
	static void removeBlackFromRelationT3P3(CWN origCWN) throws ParameterException{
		
		for( CWNFlowRelation f : origCWN.getFlowRelations()){
			if(f.getSource().getName().equals("t3") && f.getTarget().getName().equals("p3")){
				Multiset<String> constraint = f.getConstraint();
				constraint.remove("black");
				constraint.add("pink");
				f.setConstraint(constraint);										
				
			}
		}
		
	}


	//Creates a petri net which contains a livelock.
	static void addLiveLock(CWN origCWN) throws ParameterException{
		
		//add the places
		origCWN.addPlace("pl1");
		origCWN.addPlace("pl2");
		
		//add transitions
		origCWN.addTransition("tl1");
		origCWN.addTransition("tl2");
		
		//add the flow relations connecting the nre
		//places and transitions
		origCWN.addFlowRelationPT("pl1", "tl2");
		origCWN.addFlowRelationPT("pl2", "tl1");
		origCWN.addFlowRelationTP("tl2", "pl2");
		origCWN.addFlowRelationTP("tl1", "pl1");
		
		//connect the livelock to the rest of the net
		CWNFlowRelation f = origCWN.addFlowRelationPT("p2", "tl1");
		Multiset<String> constraint = f.getConstraint();
		constraint.add("red");	
		f.setConstraint(constraint);
	}
		
	
	
	
	

}
