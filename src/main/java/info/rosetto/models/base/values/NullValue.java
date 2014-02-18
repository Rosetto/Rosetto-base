/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.values;

import info.rosetto.models.base.function.RosettoArguments;
import info.rosetto.models.state.variables.Scope;
import info.rosetto.system.RosettoLogger;
import info.rosetto.system.exceptions.NotConvertibleException;
import info.rosetto.system.messages.SystemMessage;

/**
 * 値ではあるが、評価できない値.<br>
 * 値を返す必要があるが、返すべき値が存在しない場合等に返る.<br>
 * <br>
 * 実行時に動的に値を返すかどうか決まるような関数の返値にはNullを用いる.<br>
 * 常に値を返さないと決まっている関数の場合はVoidを用いる方がよい.<br>
 * <br>
 * どの値にも変換できない. getValueはnullになる.
 * @author tohhy
 */
public class NullValue implements RosettoValue, RosettoAction {
    private static final long serialVersionUID = -5537257042851037526L;
    
    /**
     * NullValueの唯一のインスタンス.
     */
    public static final NullValue INSTANCE = new NullValue();
    
    /**
     * コンストラクタは公開しない.
     */
    private NullValue() {}
    
    @Override
    public String toString() {
        return "NullValue";
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof NullValue) {
            return true;
        }
        return false;
    }
    
    @Override
    public ValueType getType() {
        return ValueType.NULL;
    }
    
    /**
     * nullを返す.
     */
    @Override
    public Object getValue() {
        return null;
    }
    
    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public String asString() throws NotConvertibleException {
        throw new NotConvertibleException();
    }
    
    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public String asString(String defaultValue) {
        return defaultValue;
    }
    
    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public boolean asBool() throws NotConvertibleException {
        return false;
    }
    
    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public boolean asBool(boolean defaultValue) {
        return false;
    }
    
    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public int asInt() throws NotConvertibleException {
        throw new NotConvertibleException();
    }
    
    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public int asInt(int defaultValue) {
        return defaultValue;
    }
    
    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public long asLong() throws NotConvertibleException {
        throw new NotConvertibleException();
    }
    
    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public long asLong(long defaultValue) {
        return defaultValue;
    }
    
    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public double asDouble() throws NotConvertibleException {
        throw new NotConvertibleException();
    }
    
    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public double asDouble(double defaultValue) {
        return defaultValue;
    }

    @Override
    public String getName() {
        return "NULL";
    }
    
    @Override
    public RosettoValue execute(Scope parentScope) {
        RosettoLogger.warning(SystemMessage.E7000_NULL_ACTION_CALLED);
        return this;
    }
    
    @Override
    public RosettoValue execute(RosettoArguments args, Scope parentScope) {
        RosettoLogger.warning(SystemMessage.E7000_NULL_ACTION_CALLED);
        return this;
    }

    @Override
    public RosettoValue execute(String args, Scope parentScope) {
        RosettoLogger.warning(SystemMessage.E7000_NULL_ACTION_CALLED);
        return null;
    }
}
