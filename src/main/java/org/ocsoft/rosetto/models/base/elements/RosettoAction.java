/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.models.base.elements;

import org.ocsoft.rosetto.models.base.elements.values.ListValue;
import org.ocsoft.rosetto.models.system.Scope;

/**
 * 関数やマクロなど、返り値を伴う実行が可能なRosettoValueを示すインタフェース.
 * @author tohhy
 */
public interface RosettoAction extends RosettoValue {
    
    /**
     * このアクションを引数なしで実行する.
     * @return 実行した結果の返り値
     */
    public RosettoValue execute(Scope parentScope);
    
    /**
     * このアクションを実行する.
     * @param args 実行時引数
     * @return 実行した結果の返り値
     */
    public RosettoValue execute(ListValue args, Scope parentScope);
    
    /**
     * このアクションを実行する.
     * @param args 文字列形式の実行時引数
     * @return 実行した結果の返り値
     */
    public RosettoValue execute(String args, Scope parentScope);
    
}
