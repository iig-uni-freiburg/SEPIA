package de.uni.freiburg.iig.telematik.sepia.util;

import java.awt.BorderLayout;
import java.util.concurrent.ExecutionException;

import javax.swing.JPanel;

import de.invation.code.toval.graphic.component.DisplayFrame;
import de.invation.code.toval.graphic.component.ExecutorLabel;
import de.invation.code.toval.thread.AbstractCallable;
import de.invation.code.toval.thread.SingleThreadExecutorService;
import de.invation.code.toval.validate.Validate;
import de.uni.freiburg.iig.telematik.sepia.exception.BoundednessException;
import de.uni.freiburg.iig.telematik.sepia.exception.MarkingGraphException;
import de.uni.freiburg.iig.telematik.sepia.exception.StateSpaceException;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraph;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphRelation;
import de.uni.freiburg.iig.telematik.sepia.mg.abstr.AbstractMarkingGraphState;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractFlowRelation;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPetriNet;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractPlace;
import de.uni.freiburg.iig.telematik.sepia.petrinet.AbstractTransition;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTMarking;
import de.uni.freiburg.iig.telematik.sepia.petrinet.pt.PTNet;

public class ThreadedMGCalculator<	P extends AbstractPlace<F,S>, 
									T extends AbstractTransition<F,S>, 
									F extends AbstractFlowRelation<P,T,S>, 
									M extends AbstractMarking<S>, 
									S extends Object,
									X extends AbstractMarkingGraphState<M,S>,
									Y extends AbstractMarkingGraphRelation<M,X,S>> extends SingleThreadExecutorService<AbstractMarkingGraph<M,S,X,Y>>
																				   implements MGCalculator<P,T,F,M,S,X,Y>{
	
	private AbstractPetriNet<P,T,F,M,S,X,Y> petriNet = null;
	
	public ThreadedMGCalculator(AbstractPetriNet<P,T,F,M,S,X,Y> petriNet){
		super();
		Validate.notNull(petriNet);
		this.petriNet = petriNet;
//		if(acceptOnlyBoundedNet & !petriNet.isBounded())
//			throw new BoundednessException("Cannot determine marking graph for unbounded nets.");
	}
	
	@Override
	public AbstractMarkingGraph<M,S,X,Y> getMarkingGraph() throws MarkingGraphException{
		try {
			return getResult();
		} catch (InterruptedException e) {
			throw new MarkingGraphException("Marking graph construction interrupted.", e);
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if(cause == null){
				throw new MarkingGraphException("Exception during marking graph construction.\n" + e.getMessage(), e);
			}
			if(cause instanceof StateSpaceException){
				throw new MarkingGraphException("Exception during marking graph construction.\nCannot build whole marking graph", cause);
			}
			if(cause instanceof MarkingGraphException){
				throw (MarkingGraphException) cause;
			}
			throw new MarkingGraphException("Exception during marking graph construction.\n" + e.getMessage(), e);
		}
	}
	
	@Override
	protected AbstractCallable<AbstractMarkingGraph<M, S, X, Y>> getCallable() {
		return new MGConstructorCallable<P,T,F,M,S,X,Y>(petriNet);
	}
	
	public static <	PP extends AbstractPlace<FF,SS>, 
					TT extends AbstractTransition<FF,SS>, 
					FF extends AbstractFlowRelation<PP,TT,SS>, 
					MM extends AbstractMarking<SS>, 
					SS extends Object,
					XX extends AbstractMarkingGraphState<MM,SS>,
					YY extends AbstractMarkingGraphRelation<MM,XX,SS>> ThreadedMGCalculator<PP,TT,FF,MM,SS,XX,YY> getCalculator(AbstractPetriNet<PP,TT,FF,MM,SS,XX,YY> petriNet){
		return new ThreadedMGCalculator<PP, TT, FF, MM, SS, XX, YY>(petriNet);
	}
	
	public static void main(String[] args) throws BoundednessException {
		PTNet net = new PTNet();
		net.addPlace("p0");
		net.addTransition("t0");
		net.addFlowRelationPT("p0", "t0");
		net.addFlowRelationTP("t0", "p0", 2);
		PTMarking marking = new PTMarking();
		marking.set("p0", 100);
		net.setInitialMarking(marking);
//		System.out.println(net.getBoundedness());
		ExecutorLabel label = new ExecutorLabel();
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(label, BorderLayout.CENTER);
		label.setExecutor(net.checkBoundedness());
		DisplayFrame frame = new DisplayFrame(panel, true, true);
		System.out.println(net.getBoundedness());
	}

}
