/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.values;

import info.rosetto.exceptions.NotConvertibleException;

/**
 * 整数を実体とするRosettoValue.<br>
 * 内部的にはlong精度で値を保持している.
 * @author tohhy
 */
public class IntValue implements RosettoValue {
    private static final long serialVersionUID = -6660103213801013944L;
    
    /**
     * 
     */
    private final long value;
    
    /**
     * 
     * @param value
     */
    public IntValue(int value) {
        this.value = value;
    }
    
    /**
     * 
     * @param value
     */
    public IntValue(long value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RosettoValue) {
            try {
                return (value == ((RosettoValue)obj).asInt());
            } catch (NotConvertibleException e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public ValueType getType() {
        return ValueType.INTEGER;
    }
    
    @Override
    public Object getValue() {
        return value;
    }
    
    @Override
    public String asString() {
        return String.valueOf(value);
    }
    
    @Override
    public String asString(String defaultValue) {
        return String.valueOf(value);
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
     * 値がint範囲を逸脱する場合はNotConvertibleExceptionがスローされる.
     */
    @Override
    public int asInt() throws NotConvertibleException {
        if(value > Integer.MAX_VALUE || value < Integer.MIN_VALUE)
            throw new NotConvertibleException("value out of int range");
        return (int)value;
    }
    
    /**
     * 値がint範囲を逸脱する場合はデフォルト値が返る.
     */
    @Override
    public int asInt(int defaultValue) {
        if(value > Integer.MAX_VALUE || value < Integer.MIN_VALUE)
            return defaultValue;
        return (int)value;
    }
    
    @Override
    public long asLong() throws NotConvertibleException {
        return value;
    }

    @Override
    public long asLong(long defaultValue) {
        return value;
    }

    @Override
    public double asDouble() throws NotConvertibleException {
        return value;
    }

    @Override
    public double asDouble(double defaultValue) {
        return value;
    }
}
