/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.system;

import info.rosetto.models.base.elements.values.RosettoFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 名前空間に取り込む関数をまとめたパッケージ.<br>
 * ネイティブ定義の関数を定義し、名前空間に一括追加するために用いる.<br>
 * <br>
 * 具体的な実装例についてはfunctions.baseを参照のこと.<br>
 * パッケージはシングルトンにし、また関数をstatic定義する場合はシングルトンインスタンスを遅延評価する必要がある点に留意.
 * @author tohhy
 */
public class FunctionPackage {
    /**
     * このパッケージが保有する関数の一覧.
     */
    private final List<RosettoFunction> functions = new ArrayList<RosettoFunction>();
    /**
     * このパッケージが保有する関数名の一覧.
     */
    private final List<String> names = new ArrayList<String>();
    
    /**
     * 引数に与えた関数をまとめたパッケージを生成する.
     * @param functions このパッケージに追加する関数
     */
    public FunctionPackage(RosettoFunction...functions) {
        if(functions == null)
            throw new IllegalArgumentException("functions must not be null");
        for(int i=0; i<functions.length; i++) {
            if(functions[i] == null)
                throw new IllegalArgumentException("Functions must not contain null value");
            this.functions.add(functions[i]);
            this.names.add(functions[i].getName());
        }
    }
    
    @Override
    public String toString() {
        return "FunctionPackage" + names.toString();
    }
    
    /**
     * このパッケージが保有する関数のリストを返す.
     * @return このパッケージが保有する関数のリスト
     */
    public List<RosettoFunction> getFunctions() {
        return Collections.unmodifiableList(functions);
    }
    
    /**
     * このパッケージが保有する関数の名前のリストを返す.
     * @return このパッケージが保有する関数の名前のリスト
     */
    public List<String> getFunctionNames() {
        return Collections.unmodifiableList(names);
    }
    
    /**
     * このパッケージが保有する関数の個数を返す.
     * @return このパッケージが保有する関数の個数
     */
    public int getFunctionCount() {
        return functions.size();
    }
    
}
