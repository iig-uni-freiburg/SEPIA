/* 
 * Copyright (c) 2015, IIG Telematics, Uni Freiburg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * * Neither the name of IIG Telematics, Uni Freiburg nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BELIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.uni.freiburg.iig.telematik.sepia.petrinet.pt.abstr;

import de.uni.freiburg.iig.telematik.sepia.event.TransitionEvent;
import de.uni.freiburg.iig.telematik.sepia.petrinet.abstr.AbstractTransition;

public abstract class AbstractPTTransition<E extends AbstractPTFlowRelation<? extends AbstractPTPlace<E>, ? extends AbstractPTTransition<E>>> extends AbstractTransition<E,Integer>{
	
	private static final long serialVersionUID = 1253534975141710023L;

//	protected AbstractPTTransition(){
//		super();
//	}

	public AbstractPTTransition(String name) {
		super(name);
	}
	
	public AbstractPTTransition(String name, String label) {
		super(name, label);
	}
	
	public AbstractPTTransition(String name, boolean isSilent) {
		super(name, isSilent);
	}
	
	public AbstractPTTransition(String name, String label, boolean isSilent) {
		super(name, label, isSilent);
	}

	@Override
	public void checkState() {
		boolean oldEnabledState = enabled;
		enabled = true;
		for(E r: incomingRelations.values()) {
			if(r.getPlace().getState() < r.getWeight()){
				enabled = false;
				break;
			}
		}
		
		if(enabled && !oldEnabledState){
			listenerSupport.notifyEnabling(new TransitionEvent<>(this));
		} else if(!enabled && oldEnabledState){
			listenerSupport.notifyDisabling(new TransitionEvent<>(this));
		}
	}
	
	@Override
	public boolean enoughTokensInInputPlaces() {
		for(E r: incomingRelations.values()) {
			if(r.getPlace().getState() < r.getWeight()){
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean enoughSpaceInOutputPlaces() {
		for(E r: outgoingRelations.values()) {
			if(!r.getPlace().isBounded()){
				continue;
			}
			if((r.getPlace().getCapacity() - r.getPlace().getState()) < r.getWeight()){
				return false;
			}
		}
		return true;
	}
	
	@Override
	public AbstractPTTransition<E> clone() {
		return (AbstractPTTransition<E>) super.clone();
	}
}
