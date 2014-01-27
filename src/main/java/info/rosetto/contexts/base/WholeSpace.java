/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.contexts.base;

import info.rosetto.functions.base.BaseFunctions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Rosettoが認識している全ての名前空間.<br>
 * 実質的にRosettoが認識する全てのゲーム状態と等しくなる.<br>
 * WholeSpaceをシリアライズすることでゲーム上の状態が完全に保存できるように実装する.
 * @author tohhy
 */
public class WholeSpace implements Serializable {
    private static final long serialVersionUID = -8911679659186174490L;

    /**
     * このインスタンスが保有する全ての名前空間の一覧.
     */
    private final Map<String, NameSpace> nameSpaces = new HashMap<String, NameSpace>();
    
    /**
     * 現在アクティブな名前空間.
     */
    private NameSpace currentNameSpace;
    
    /**
     * 
     */
    public WholeSpace() {
        setCurrentNameSpace(createNameSpace("story"));
    }
    
    /**
     * 指定名の名前空間を取得する.<br>
     * 名前空間のインスタンスが存在しない場合は新しく空の名前空間を生成し、
     * rosetto.baseの関数群をUseした状態にして取得する.
     * @param name 
     * @return
     */
    public NameSpace getNameSpace(String name) {
        if(!nameSpaces.containsKey(name))
            createNameSpace(name);
        return nameSpaces.get(name);
    }
    
    /**
     * 
     * @param name
     * @return
     */
    public NameSpace createNameSpace(String name) {
        NameSpace space = new NameSpace(name);
        nameSpaces.put(name, space);
        return space;
    }
    
    /**
     * 
     * @param name
     * @return
     */
    public boolean contains(String name) {
        return nameSpaces.containsKey(name);
    }
    
    /**
     * このインスタンス中に実体として生成された名前空間の数を返す.
     * @return このインスタンス中に実体として生成された名前空間の数
     */
    public int getCreatedNameSpaceCount() {
        return nameSpaces.size();
    }
    
    /**
     * 現在アクティブな名前空間を取得する.
     * @return 現在アクティブな名前空間
     */
    public NameSpace getCurrentNameSpace() {
        return currentNameSpace;
    }
    
    /**
     * 指定名の名前空間をアクティブな名前空間にする.
     * @param name アクティブにする名前空間の完全名
     */
    public void setCurrentNameSpace(String name) {
        if(name == null || name.length() == 0)
            throw new IllegalArgumentException("name must not be empty");
        setCurrentNameSpace(contains(name) ? 
                getNameSpace(name) : createNameSpace(name));
    }
    
    /**
     * 
     * @param nameSpace
     */
    public void setCurrentNameSpace(NameSpace nameSpace) {
        currentNameSpace = nameSpace;
        BaseFunctions.getInstance().addTo(nameSpace);
    }
}
