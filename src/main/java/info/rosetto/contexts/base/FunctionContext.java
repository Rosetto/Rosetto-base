/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.contexts.base;

import info.rosetto.functions.base.BaseFunctions;
import info.rosetto.models.base.function.FunctionPackage;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.values.RosettoValue;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 関数やマクロを保持するコンテキスト.
 * @author tohhy
 */
public class FunctionContext implements Serializable {
    private static final long serialVersionUID = -2299445118074812605L;

    /**
     * 関数コンテキストが保有する全ての名前空間の一覧.
     */
    private final Map<String, NameSpace> nameSpaces = new HashMap<String, NameSpace>();
    
    /**
     * 現在ロードされている関数を表す名前空間.
     */
    private final NameSpace currentNameSpace = new NameSpace("functions-current");
    
    /**
     * パッケージ内でのみ生成.
     */
    FunctionContext() {
        importPackage(BaseFunctions.getInstance(), "rosetto.base");
        usePackage("rosetto.base");
    }
    
    /**
     * currentから指定名の関数を取得する.
     * @param functionName
     * @return
     */
    public RosettoFunction get(String functionName) {
        RosettoValue v = currentNameSpace.get(functionName);
        if(v==null || !(v instanceof RosettoFunction)) return BaseFunctions.pass;
        return (RosettoFunction) v;
    }
    
    /**
     * 指定した名前空間の指定したキーに存在する値を取得する.
     * @param nameSpace 指定する名前空間
     * @param key 取得する変数名
     * @return 取得した値
     */
    public RosettoFunction get(String nameSpace, String varName) {
        if(!nameSpaces.containsKey(nameSpace)) return BaseFunctions.pass;
        return (RosettoFunction)nameSpaces.get(nameSpace).get(varName);
    }
    
    public void define(RosettoFunction f) {
        currentNameSpace.set(f.getName(), f);
    }
    
    /**
     * 指定したパッケージを指定名の名前空間上に読み込む.
     * @param p
     */
    public void importPackage(FunctionPackage p, String packageName) {
        NameSpace space = getNameSpace(packageName);
        for(RosettoFunction f : p.getFunctions()) {
            space.set(f.getName(), f);
        }
    }

    /**
     * 指定したパッケージに含まれるすべての関数をcurrentにロードして使用可能にする.
     * @param packageName
     */
    public void usePackage(String packageName) {
        NameSpace pkg = getNameSpace(packageName);
        if(pkg != null) currentNameSpace.include(pkg);
    }

    /**
     * 指定名の名前空間を生成する.
     * @param name 生成する名前空間の完全名
     * @return 生成した名前空間
     */
    private NameSpace createNameSpace(String name) {
        NameSpace space = new NameSpace(name);
        putNameSpace(space);
        return space;
    }

    /**
     * 指定名の名前空間を取得する.
     * @param name 取得する名前空間の完全名
     * @return 取得した、あるいは生成した名前空間
     */
    private NameSpace getNameSpace(String name) {
        if(!nameSpaces.containsKey(name))
            createNameSpace(name);
        return nameSpaces.get(name);
    }
    
    /**
     * WholeSpaceに指定した名前空間を追加する.
     * @param ns 追加する名前空間
     */
    private void putNameSpace(NameSpace ns) {
        nameSpaces.put(ns.getName(), ns);
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
}
