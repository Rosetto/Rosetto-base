/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.values;

import info.rosetto.models.base.function.FunctionCall;
import info.rosetto.models.base.values.RosettoValue.VoidValue;

/**
 * Rosetto中の引数や関数の型を定義した列挙子.
 * Rosetto中での値の引き渡しは全て文字列か関数呼び出しによって行われるため、
 * FuncCallとString、Void関数の返り値となるVoidしか定義されない.
 * @author tohhy
 */
public enum ValueType {
    /**
     * 値を返さないことを示す型.
     */
    Void(VoidValue.class), 
    /**
     * 関数呼び出しを示す型.
     */
    FuncCall(FunctionCall.class),
    /**
     * 文字列リテラルを示す型.
     */
    String(String.class),
    ;
    
    /**
     * 型に対応するJavaクラス.
     */
    private final Class<?> valueClass;
    
    /**
     * 型に対応するJavaクラスを指定してValueTypeを初期化する.
     * @param valueClass 型に対応するJavaクラス
     */
    private ValueType(Class<?> valueClass) {
        this.valueClass = valueClass;
    }
    
    /**
     * この型に対応するJavaクラスを取得する.
     * @return 型に対応するJavaクラス
     */
    public Class<?> getValueClass() {
        return valueClass;
    }
}
