/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.models.base.function;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;

/**
 * 関数名を格納するクラス.
 * @author tohhy
 */
@Immutable
public class FunctionName implements Serializable {
    private static final long serialVersionUID = -2384840077956483556L;
    
    /**
     * 関数のパッケージ名.
     */
    private final String pkg;
    
    /**
     * 関数名.
     */
    private final String name;
    
    /**
     * 関数名を格納するクラス.
     * @param pkg パッケージ名
     * @param name 関数名
     */
    public FunctionName(String pkg, String name) {
        if(pkg == null || pkg.length() == 0)
            throw new IllegalArgumentException("パッケージが指定されていません");
        if(name == null || name.length() == 0)
            throw new IllegalArgumentException("関数オブジェクトの名前が指定されていません");
        this.pkg = pkg;
        this.name = name;
    }
    
    /**
     * この関数の完全名を返す.
     * @return この関数の完全名
     */
    public String getFullName() {
        return pkg + "." + name;
    }

    /**
     * この関数のパッケージ階層の文字列表現を返す.
     * @return この関数のパッケージ階層の文字列表現
     */
    public String getPackage() {
        return pkg;
    }

    /**
     * 関数名を返す.
     * @return 関数名
     */
    public String getShortName() {
        return name;
    }
    
    /**
     * getFullName()と同じ.
     */
    @Override
    public String toString() {
        return getFullName();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof FunctionName) {
            return ((FunctionName) obj).name.equals(name) && ((FunctionName) obj).pkg.equals(pkg);
        } else {
            throw new IllegalArgumentException("FunctionName型以外の引数が渡されました");
        }
    }
    
    /**
     * 指定文字列がこの関数の関数名と等しいかどうかを返す.
     * @param name 文字列形式の関数名
     * @return この関数の関数名と等しいかどうか
     */
    public boolean equalsName(String name) {
        if(name == null) throw new IllegalArgumentException("name must not be null");
        return name.equals(getShortName()) || name.equals(getFullName());
    }
}