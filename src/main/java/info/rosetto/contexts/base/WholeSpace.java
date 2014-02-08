/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.contexts.base;

import info.rosetto.functions.base.BaseFunctions;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.utils.base.Values;

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
     * パッケージ内でのみ生成.
     */
    WholeSpace() {
        setCurrentNameSpace(createNameSpace("story"));
    }
    
    /**
     * 指定した名前空間の指定したキーに存在する値を取得する.
     * @param nameSpace 指定する名前空間
     * @param key 取得する変数名
     * @return 取得した値
     */
    public RosettoValue get(String nameSpace, String varName) {
        if(!nameSpaces.containsKey(nameSpace)) return Values.NULL;
        return nameSpaces.get(nameSpace).get(varName);
    }
    
    /**
     * 指定名の名前空間を取得する.<br>
     * 名前空間のインスタンスが存在しない場合は新しく空の名前空間を生成し、
     * rosetto.baseの関数群をUseした状態にして取得する.
     * @param name 取得する名前空間の完全名
     * @return 取得した、あるいは生成した名前空間
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
    public boolean containsNameSpace(String name) {
        return nameSpaces.containsKey(name);
    }
    
    /**
     * 
     * @param packageName
     * @param referName
     */
    public void refer(String packageName, String referName) {
        nameSpaces.put(referName, nameSpaces.get(packageName));
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
        setCurrentNameSpace(containsNameSpace(name) ? 
                getNameSpace(name) : createNameSpace(name));
    }
    
    /**
     * 現在アクティブな名前空間を指定する.
     * @param nameSpace アクティブにする名前空間
     */
    public void setCurrentNameSpace(NameSpace nameSpace) {
        currentNameSpace = nameSpace;
        BaseFunctions.getInstance().addTo(nameSpace);
    }
    
}
