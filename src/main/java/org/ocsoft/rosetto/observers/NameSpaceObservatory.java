/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.observers;

import java.util.Map.Entry;
import java.util.Set;

import org.frows.observatories.v0_0_6.ObjectObservatory;
import org.ocsoft.rosetto.models.base.elements.RosettoValue;

/**
 * 単一の名前空間以下の変数の変更を観測するObservatory.
 * @author tohhy
 */
public class NameSpaceObservatory extends ObjectObservatory<String, VariableObserver> 
    implements VariableObserver {
    
    NameSpaceObservatory() {}
    
    @Override
    public void valueChanged(String nameSpace, String variableName, RosettoValue newValue) {
        for(Entry<String, Set<VariableObserver>> os : getObjectObservers().entrySet()) {
            if(nameSpace.startsWith(os.getKey()))
                for(VariableObserver o : os.getValue()) {
                    o.valueChanged(nameSpace, variableName, newValue);
                }
        }
    }
}
