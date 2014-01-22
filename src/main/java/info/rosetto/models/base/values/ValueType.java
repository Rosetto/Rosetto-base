/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.values;

import info.rosetto.models.base.function.RosettoFunction;

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
    VOID(VoidValue.class),
    /**
     * Null値を示す型.
     */
    NULL(NullValue.class),
    /**
     * 関数やマクロの呼び出しを示す型.
     */
    ACTION_CALL(ActionCall.class),
    /**
     * 関数を示す型.
     */
    FUNCTION(RosettoFunction.class),
    /**
     * マクロを示す型.
     */
    MACRO(String.class),
    /**
     * 文字列リテラルを示す型.
     */
    STRING(String.class),
    /**
     * 真偽値を示す型.
     */
    BOOLEAN(boolean.class),
    /**
     * 整数を示す型.
     */
    INTEGER(int.class),
    /**
     * 浮動小数点数を示す型.
     */
    DOUBLE(double.class),
    /**
     * 任意のオブジェクトを示す型.
     */
    OBJECT(Object.class)
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
