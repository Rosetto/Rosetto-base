/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.rosetto.observers;

import org.rosetto.models.base.elements.RosettoValue;

/**
 * 変数の変更を監視するオブザーバー.
 * @author tohhy
 */
public interface VariableObserver {
    
    /**
     * 変数が変更された際に呼び出される.
     * @param nameSpace 変更された変数が所属する名前空間
     * @param variableName 変更された変数の名称
     * @param newValue 変更後の値
     */
    public void valueChanged(String nameSpace, String variableName, RosettoValue newValue);
    
}
