/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.models.base.elements.values;

import org.ocsoft.rosetto.models.base.elements.RosettoValue;
import org.ocsoft.rosetto.models.base.elements.ValueType;
import org.ocsoft.rosetto.models.system.Scope;
import org.ocsoft.rosetto.system.RosettoLogger;
import org.ocsoft.rosetto.system.exceptions.NotConvertibleException;
import org.ocsoft.rosetto.system.messages.SystemMessage;

/**
 * 「なにもない」「値ではない」ことを表す仮想的な値.<br>
 * 関数の返り値がない場合などに返される.<br>
 * <br>
 * Voidを返す関数は必ずVoidを返す.<br>
 * 実行時に動的に返り値がVoidかどうかが決定されるような関数は定義してはいけない.
 * そうした場合はNullを用いる.<br>
 * <br>
 * どの値にも変換できない. getValueはUnsupportedOperationExceptionをスローする.
 * @author tohhy
 */
public class VoidValue implements RosettoValue {
    private static final long serialVersionUID = -2008039579157732704L;
    /**
     * VoidValueの唯一のインスタンス.
     */
    public static VoidValue INSTANCE = new VoidValue();
    
    /**
     * コンストラクタは公開しない.
     */
    private VoidValue() {}
    
    @Override
    public String toString() {
        return "VoidValue";
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof VoidValue) {
            return true;
        }
        return false;
    }
    
    @Override
    public ValueType getType() {
        return ValueType.VOID;
    }

    /**
     * UnsupportedOperationExceptionをスローする.
     */
    @Override
    public Object getValue() {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        throw new UnsupportedOperationException("this value is void");
    }

    @Override
    public RosettoValue evaluate(Scope scope) {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        return this;
    }
    
    @Override
    public RosettoValue first() {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        throw new UnsupportedOperationException("this value is void");
    }
    
    @Override
    public RosettoValue rest() {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        throw new UnsupportedOperationException("this value is void");
    }
    
    @Override
    public RosettoValue cons(RosettoValue head) {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        throw new UnsupportedOperationException("this value is void");
    }
    
    @Override
    public RosettoValue getAt(int index) {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        throw new UnsupportedOperationException("this value is void");
    }
    
    @Override
    public int size() {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        return 0;
    }

    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public String asString() throws NotConvertibleException {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        throw new NotConvertibleException();
    }

    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public String asString(String defaultValue) {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        return defaultValue;
    }

    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public boolean asBool() throws NotConvertibleException {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        throw new NotConvertibleException();
    }
    
    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public boolean asBool(boolean defaultValue) {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        return defaultValue;
    }
    
    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public int asInt() throws NotConvertibleException {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        throw new NotConvertibleException();
    }
    
    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public int asInt(int defaultValue) {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        return defaultValue;
    }
    
    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public long asLong() throws NotConvertibleException {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        throw new NotConvertibleException();
    }

    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public long asLong(long defaultValue) {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        return defaultValue;
    }

    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public double asDouble() throws NotConvertibleException {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        throw new NotConvertibleException();
    }

    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public double asDouble(double defaultValue) {
        RosettoLogger.warning(SystemMessage.E7100_VOID_ACTION_CALLED);
        return defaultValue;
    }
}
