/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.values;

import info.rosetto.exceptions.NotConvertibleException;

/**
 * 返り値がなかったことを表す値.<br>
 * どの値にも変換できない.toStringは"VOID", getValueはnullになる.
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
     * nullを返す.
     * @return null
     */
    @Override
    public Object getValue() {
        return null;
    }
    
    @Override
    public String asString() {
        return "VOID";
    }
    
    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public boolean asBool() throws NotConvertibleException {
        throw new NotConvertibleException();
    }
    
    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public boolean asBool(boolean defaultValue) {
        return defaultValue;
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
    public long asLong() throws NotConvertibleException {
        throw new NotConvertibleException();
    }

    @Override
    public long asLong(long defaultValue) {
        return defaultValue;
    }
}
