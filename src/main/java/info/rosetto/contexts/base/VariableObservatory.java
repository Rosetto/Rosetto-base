/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.contexts.base;

import info.rosetto.models.base.values.RosettoValue;

import org.frows.observatories.Observatory;

/**
 * Rosetto中の全変数の変更を監視する.
 * @author tohhy
 */
public class VariableObservatory extends Observatory<VariableObserver> 
    implements VariableObserver {
    
    /**
     * シングルトンインスタンス.
     */
    private static final VariableObservatory instance = new VariableObservatory();
    
    /**
     * VariableObservatoryのシングルトンインスタンスを取得する.
     * @return VariableObservatoryのシングルトンインスタンス
     */
    public static VariableObservatory getInstance() {
        return instance;
    }
    
    /**
     * 特定の名前空間の変数変更のみを監視するオブザーバを登録する.
     * @param nameSpace 対象とする名前空間
     * @param observer 追加するオブザーバ
     */
    public void addNameSpaceObserver(String nameSpace, VariableObserver observer) {
        NameSpaceObservatory.getInstance().addObserver(nameSpace, observer);
    }
    
    @Override
    public void valueChanged(String nameSpace, String variableName,
            RosettoValue newValue) {
        for(VariableObserver o : getObservers()) {
            o.valueChanged(nameSpace, variableName, newValue);
        }
        NameSpaceObservatory.getInstance().valueChanged(nameSpace, variableName, newValue);
    }

}
