/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.function;

import info.rosetto.contexts.base.NameSpace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 名前空間に取り込む関数をまとめたパッケージ.<br>
 * ネイティブ定義の関数を定義し、名前空間に一括追加するために用いる.<br>
 * <br>
 * 具体的な実装についてはBaseFunctionsを参照のこと.<br>
 * シングルトンにし、関数をstatic定義する場合はシングルトンインスタンスを遅延評価する必要がある点に留意.
 * @author tohhy
 */
public class FunctionPackage {
    /**
     * このパッケージが保有する要素の一覧.
     */
    private final List<RosettoFunction> functions = new ArrayList<RosettoFunction>();
    /**
     * このパッケージが保有する関数名の一覧.
     */
    private final List<String> names = new ArrayList<String>();
    /**
     * このパッケージの名前空間名.
     */
    private final String packageName;
    
    /**
     * 
     * @param packageName
     * @param functions
     */
    public FunctionPackage(String packageName, RosettoFunction...functions) {
        if(packageName == null || packageName.length() == 0)
            throw new IllegalArgumentException("package name must not be empty");
        this.packageName = packageName;
        for(int i=0; i<functions.length; i++) {
            if(functions[i] == null)
                throw new IllegalArgumentException("Functions contains null value");
            this.functions.add(functions[i]);
            this.names.add(functions[i].getName());
        }
    }
    
    @Override
    public String toString() {
        return "FunctionPackage" + names.toString();
    }
    
    /**
     * このパッケージが保持する関数を全て指定した名前空間に追加する.
     * @param space
     */
    public void addTo(NameSpace space) {
        for(RosettoFunction f : functions) {
            space.set(f.getName(), f);
        }
    }
    
    /**
     * このパッケージの名前空間名を返す.
     * @return
     */
    public String getPackageName() {
        return packageName;
    }
    
    /**
     * このパッケージが保有する関数のリストを返す.
     * @return
     */
    public List<RosettoFunction> getFunctions() {
        return Collections.unmodifiableList(functions);
    }
    
    /**
     * このパッケージが保有する関数の名前のリストを返す.
     * @return
     */
    public List<String> getFunctionNames() {
        return Collections.unmodifiableList(names);
    }
    
    /**
     * このパッケージが保有する関数の個数を返す.
     * @return
     */
    public int getFunctionCount() {
        return functions.size();
    }
    
}
