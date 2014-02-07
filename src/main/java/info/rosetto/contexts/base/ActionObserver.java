/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.contexts.base;

import info.rosetto.models.base.blocks.Macro;
import info.rosetto.models.base.function.RosettoArguments;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.values.RosettoValue;

/**
 * Rosetto中の関数呼び出しやマクロ実行等を監視するオブザーバ.
 * @author tohhy
 */
public interface ActionObserver {
    
    /**
     * 関数が実行された場合に呼び出される.
     * @param func 実行された関数
     * @param args 実行時の引数
     * @param evaluatedValue 実行結果の返り値
     */
    public void functionExecuted(RosettoFunction func, 
            RosettoArguments args, 
            RosettoValue evaluatedValue);
    
    
    /**
     * マクロが実行された場合に呼び出される.
     * @param macro 実行されたマクロ
     */
    public void macroExecuted(Macro macro);

}
