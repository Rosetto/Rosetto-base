/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.rosetto.utils.base;

import org.rosetto.models.base.elements.RosettoValue;
import org.rosetto.models.base.elements.ValueType;
import org.rosetto.models.base.elements.values.ActionCall;
import org.rosetto.models.system.Scope;

/**
 * 
 * @author tohhy
 *
 */
public class FunctionUtils {

    
    /**
     * 引数に与えたスコープを用いて引数に与えたアクションまたはアクションのリストを順に実行する.
     * @param scope
     * @param actions
     * @return
     */
    public static RosettoValue doActions(Scope scope, RosettoValue actions) {
        RosettoValue result = Values.NULL;
        while(true) {
            RosettoValue item = actions.first();
            if(item.getType() == ValueType.ACTION_CALL) {
                ActionCall ac = (ActionCall) item;
                result = ac.evaluate(scope);
            } else {
                result = item;
            }
            if(actions.size() == 1) return result;
            actions = actions.rest();
        }
    }
}
