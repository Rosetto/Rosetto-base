/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.observers;

import info.rosetto.models.base.blocks.RosettoMacro;
import info.rosetto.models.base.elements.MixedStore;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.function.RosettoFunction;

import org.frows.observatories.Observatory;

/**
 * アクション実行を監視するObservertory.
 * @author tohhy
 */
public class ActionObservatory extends Observatory<ActionObserver> 
implements ActionObserver {
    
    ActionObservatory() {}
    
    @Override
    public void functionExecuted(RosettoFunction func, MixedStore args,
            RosettoValue evaluatedValue) {
        for(ActionObserver o : getObservers()) {
            o.functionExecuted(func, args, evaluatedValue);
        }
    }
    
    @Override
    public void macroExecuted(RosettoMacro macro) {
        for(ActionObserver o : getObservers()) {
            o.macroExecuted(macro);
        }
    }
    
}
