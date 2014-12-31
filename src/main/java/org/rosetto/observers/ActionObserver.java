/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.rosetto.observers;

import org.rosetto.models.base.elements.RosettoValue;
import org.rosetto.models.base.elements.values.ListValue;
import org.rosetto.models.base.elements.values.RosettoFunction;
import org.rosetto.models.base.elements.values.ScriptValue;

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
            ListValue args, 
            RosettoValue evaluatedValue);
    
    /**
     * マクロの実行が完了した場合に呼び出される.
     * @param macro 実行されたマクロ
     * @param args 実行時の引数
     * @param evaluatedValue 実行結果の返り値
     */
    public void macroExecuted(ScriptValue macro, 
            ListValue args,
            RosettoValue evaluatedValue);
    
}
