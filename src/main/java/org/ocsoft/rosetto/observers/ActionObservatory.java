/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.observers;

import org.frows.observatories.v0_1_0.Observatory;
import org.ocsoft.rosetto.models.base.elements.RosettoValue;
import org.ocsoft.rosetto.models.base.elements.values.ListValue;
import org.ocsoft.rosetto.models.base.elements.values.RosettoFunction;
import org.ocsoft.rosetto.models.base.elements.values.ScriptValue;

/**
 * アクション実行を監視するObservertory.
 * @author tohhy
 */
public class ActionObservatory extends Observatory<ActionObserver> 
implements ActionObserver {
    
    ActionObservatory() {}
    
    @Override
    public void functionExecuted(RosettoFunction func, ListValue args,
            RosettoValue evaluatedValue) {
        for(ActionObserver o : getObservers()) {
            o.functionExecuted(func, args, evaluatedValue);
        }
    }
    
    @Override
    public void macroExecuted(ScriptValue macro, ListValue args,
            RosettoValue evaluatedValue) {
        for(ActionObserver o : getObservers()) {
            o.macroExecuted(macro, args, evaluatedValue);
        }
    }
    
}
