/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.contexts.base;

import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.system.NameSpace;
import info.rosetto.utils.base.Values;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Rosetto中の全てのグローバル変数を格納するコンテキスト.<br>
 * シリアライズすることでゲーム上の状態が完全に保存できるように実装する.
 * @author tohhy
 */
public class GlobalVariables implements Serializable {
    private static final long serialVersionUID = -8911679659186174490L;
    
    /**
     * このインスタンスが保有する全ての名前空間の一覧.
     */
    private final Map<String, NameSpace> nameSpaces = new HashMap<String, NameSpace>();
    
    /**
     * ルートの名前空間.
     */
    private final NameSpace rootSpace = NameSpace.createRootSpace();
    
    /**
     * パッケージ内でのみ生成.
     */
    GlobalVariables() {}
    
    /**
     * 指定したキーに存在する値を取得する.
     * @param key 絶対参照のキー
     * @return 指定したキーに存在する値
     */
    public RosettoValue get(String key) {
        if(key == null || key.length() == 0)
            throw new IllegalArgumentException("key must not be empty");
        int lastDot = key.lastIndexOf('.');
        if(lastDot == -1) return rootSpace.get(key);
        String nameSpace = key.substring(0, lastDot);
        String varName = key.substring(lastDot + 1);
        return get(nameSpace, varName);
    }
    
    /**
     * 指定した名前空間の指定したキーに存在する値を取得する.
     * @param nameSpace 指定する名前空間
     * @param key 取得する変数名
     * @return 取得した値
     */
    public RosettoValue get(String nameSpace, String varName) {
        if(nameSpace == null || nameSpace.length() == 0)
            throw new IllegalArgumentException("nameSpace must not be empty");
        if(varName == null || varName.length() == 0)
            throw new IllegalArgumentException("varName must not be empty");
        if(!nameSpaces.containsKey(nameSpace)) return Values.NULL;
        return nameSpaces.get(nameSpace).get(varName);
    }
    
    /**
     * 指定したキーに指定した値を設定する.
     * @param key 絶対参照のキー
     * @param value 設定する値
     */
    public void define(String key, RosettoValue value) {
        if(key == null || key.length() == 0)
            throw new IllegalArgumentException("key must not be empty");
        int lastDot = key.lastIndexOf('.');
        if(lastDot == -1) {
            rootSpace.define(key, value);
            return;
        }
        String nameSpace = key.substring(0, lastDot);
        String varName = key.substring(lastDot + 1);
        getNameSpace(nameSpace).define(varName, value);
    }

    /**
     * 指定名の名前空間を取得する.<br>
     * 名前空間のインスタンスが存在しない場合は新しく空の名前空間を生成する.
     * @param name 取得する名前空間の完全名
     * @return 取得した、あるいは生成した名前空間
     */
    public NameSpace getNameSpace(String name) {
        if(!nameSpaces.containsKey(name))
            createNameSpace(name);
        return nameSpaces.get(name);
    }
    
    /**
     * 指定名の名前空間を生成する.
     * @param name 生成する名前空間の完全名
     * @return 生成した名前空間
     */
    public NameSpace createNameSpace(String name) {
        NameSpace space = new NameSpace(name);
        putNameSpace(space);
        return space;
    }
    
    /**
     * 指定名のパッケージが含まれているかどうかを返す.
     * @param name 対象とするパッケージ
     * @return 指定名のパッケージが含まれているかどうか
     */
    public boolean containsNameSpace(String name) {
        return nameSpaces.containsKey(name);
    }

    /**
     * 指定した名前空間を追加する.
     * @param ns 追加する名前空間
     */
    private void putNameSpace(NameSpace ns) {
        nameSpaces.put(ns.getName(), ns);
    }
    
}
