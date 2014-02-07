/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.contexts.base;

import info.rosetto.models.base.blocks.Macro;
import info.rosetto.models.base.function.RosettoArguments;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.values.RosettoValue;

import org.frows.observatories.Observatory;

/**
 * アクション実行を監視するObservertory.
 * @author tohhy
 */
public class ActionObservatory extends Observatory<ActionObserver> 
implements ActionObserver {
    /**
     * シングルトンインスタンス.
     */
    private static final ActionObservatory instance = new ActionObservatory();
    
    private ActionObservatory() {}
    
    /**
     * ActionObservatoryのシングルトンインスタンスを取得する.
     * @return ActionObservatoryのシングルトンインスタンス
     */
    public static ActionObservatory getInstance() {
        return instance;
    }
    
    @Override
    public void functionExecuted(RosettoFunction func, RosettoArguments args,
            RosettoValue evaluatedValue) {
        for(ActionObserver o : getObservers()) {
            o.functionExecuted(func, args, evaluatedValue);
        }
    }
    
    @Override
    public void macroExecuted(Macro macro) {
        for(ActionObserver o : getObservers()) {
            o.macroExecuted(macro);
        }
    }
    
}
