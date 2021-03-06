/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.models.base.elements;

import org.ocsoft.rosetto.models.base.elements.values.ActionCall;
import org.ocsoft.rosetto.models.base.elements.values.ListValue;
import org.ocsoft.rosetto.models.base.elements.values.NullValue;
import org.ocsoft.rosetto.models.base.elements.values.RosettoFunction;
import org.ocsoft.rosetto.models.base.elements.values.ScriptValue;
import org.ocsoft.rosetto.models.base.elements.values.VoidValue;

/**
 * Rosetto中の引数や関数の型を定義した列挙子.
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
     * 構造体を表す型.
     */
    LIST(ListValue.class),
    /**
     * 関数を示す型.
     */
    FUNCTION(RosettoFunction.class),
    /**
     * スクリプト片を示す型.
     */
    SCRIPT(ScriptValue.class),
    /**
     * 文字列を示す型.
     */
    STRING(String.class),
    /**
     * 真偽値を示す型.
     */
    BOOLEAN(boolean.class),
    /**
     * 整数を示す型.実体はlong.
     */
    INTEGER(long.class),
    /**
     * 浮動小数点数を示す型.
     */
    DOUBLE(double.class),
    ;
    
    /**
     * 型に対応するJavaクラス.
     */
    private final Class<?> valueClass;
    
    /**
     * 型に対応するJavaクラスを指定してValueTypeを定義する.
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
