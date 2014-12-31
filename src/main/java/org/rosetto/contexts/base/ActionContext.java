/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.rosetto.contexts.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.rosetto.functions.base.ArithmeticFunctions;
import org.rosetto.functions.base.BaseFunctions;
import org.rosetto.functions.base.FunctionalFunctions;
import org.rosetto.functions.base.MathFunctions;
import org.rosetto.models.base.elements.RosettoAction;
import org.rosetto.models.base.elements.RosettoValue;
import org.rosetto.models.base.elements.values.RosettoFunction;
import org.rosetto.models.system.FunctionPackage;
import org.rosetto.models.system.NameSpace;
import org.rosetto.utils.base.Values;

/**
 * 関数やマクロなどの実行可能な対象を保持するコンテキスト.<br>
 * グローバル変数の記憶域とは異なる名前空間を独自に持つ.
 * @author tohhy
 */
public class ActionContext implements Serializable {
    private static final long serialVersionUID = -2299445118074812605L;
    
    /**
     * コンテキストが保有する全ての名前空間の一覧.
     */
    private final Map<String, NameSpace> nameSpaces = new HashMap<String, NameSpace>();
    
    /**
     * 現在ロードされている関数を表す名前空間.
     */
    private final NameSpace current = new NameSpace("functions-current");
    
    /**
     * パッケージ内でのみ生成.<br>
     * 生成時に基本関数パッケージがimportされ、useされる.
     */
    ActionContext() {
        importPackage(BaseFunctions.getInstance(), "base");
        importPackage(ArithmeticFunctions.getInstance(), "arithmetic");
        importPackage(FunctionalFunctions.getInstance(), "functional");
        importPackage(MathFunctions.getInstance(), "math");
        usePackage("base");
        usePackage("arithmetic");
    }
    
    /**
     * 指定名のアクションを取得する.<br>
     * 指定名のアクションが存在しない場合はValues.NULLが返る.
     * @param functionName 関数コンテキストから取得するアクションの名称
     * @return 取得したアクション
     */
    public RosettoAction get(String functionName) {
        int lastDot = functionName.lastIndexOf('.');
        RosettoValue v = (lastDot == -1) ? 
                current.get(functionName) 
                : get(functionName.substring(0, lastDot), functionName.substring(lastDot+1));
        return (RosettoAction) v;
    }
    
    /**
     * 指定した名前空間の指定したキーに存在するアクションを取得する.
     * @param nameSpace 指定する名前空間
     * @param key 取得するアクション名
     * @return 取得した値
     */
    public RosettoAction get(String nameSpace, String varName) {
        if(!nameSpaces.containsKey(nameSpace)) return Values.NULL;
        NameSpace ns = nameSpaces.get(nameSpace);
        return (RosettoAction)ns.get(varName);
    }
    
    /**
     * 指定したアクションをコンテキスト上に定義する.
     * @param action 定義する関数
     */
    public void defineAction(String key, RosettoAction action) {
        current.define(key, action);
    }
    
    /**
     * 指定したアクションを指定名のパッケージ上に定義する.
     * @param action 定義するアクション
     * @param packageName 追加先のパッケージ
     */
    public void defineAction(String key, RosettoAction action, String packageName) {
        getNameSpace(packageName).define(key, action);
    }
    
    /**
     * 指定したパッケージ内の然要素を指定名の名前空間上に読み込む.
     * @param p 読み込むパッケージ
     */
    public void importPackage(FunctionPackage p, String packageName) {
        NameSpace space = getNameSpace(packageName);
        for(RosettoFunction f : p.getFunctions()) {
            space.define(f.getName(), f);
        }
    }
    
    /**
     * 指定したパッケージに含まれるすべてのアクションをcurrentにロードして使用可能にする.
     * @param packageName ロードするパッケージの完全名
     */
    public void usePackage(String packageName) {
        NameSpace pkg = getNameSpace(packageName);
        current.include(pkg);
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
     * 指定名の名前空間を取得する.存在しなければ生成する.
     * @param name 取得する名前空間の完全名
     * @return 取得した、あるいは生成した名前空間
     */
    private NameSpace getNameSpace(String name) {
        if(!nameSpaces.containsKey(name))
            putNameSpace(new NameSpace(name));
        return nameSpaces.get(name);
    }
    
    /**
     * 指定した名前空間を追加する.
     * @param ns 追加する名前空間
     */
    private void putNameSpace(NameSpace ns) {
        nameSpaces.put(ns.getName(), ns);
    }
}
