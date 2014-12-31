/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.models.base.elements.values;

import org.ocsoft.rosetto.models.base.elements.RosettoValue;
import org.ocsoft.rosetto.models.base.elements.ValueType;
import org.ocsoft.rosetto.models.system.Scope;
import org.ocsoft.rosetto.system.exceptions.NotConvertibleException;
import org.ocsoft.rosetto.utils.base.Values;

/**
 * 
 * @author tohhy
 */
public class DoubleValue implements RosettoValue {
    private static final long serialVersionUID = -3880822918826025412L;
    
    /**
     * 
     */
    private final double value;
    
    /**
     * 
     * @param value
     */
    public DoubleValue(double value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RosettoValue) {
            try {
                return (value == ((RosettoValue)obj).asDouble());
            } catch (NotConvertibleException e) {
                return false;
            }
        }
        return false;
    }
    
    @Override
    public RosettoValue first() {
        return this;
    }

    @Override
    public RosettoValue rest() {
        return Values.NULL;
    }
    
    @Override
    public RosettoValue cons(RosettoValue head) {
        return new ListValue(head, this);
    }

    @Override
    public RosettoValue getAt(int index) {
        if(index == 0) return this;
        return Values.NULL;
    }
    
    @Override
    public int size() {
        return 1;
    }
    
    @Override
    public RosettoValue evaluate(Scope scope) {
        return this;
    }
    
    @Override
    public ValueType getType() {
        return ValueType.DOUBLE;
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
    
    @Override
    public int asInt() throws NotConvertibleException {
        return (int)value;
    }
    
    @Override
    public int asInt(int defaultValue) {
        return (int)value;
    }
    
    @Override
    public double asDouble() throws NotConvertibleException {
        return value;
    }
    
    @Override
    public double asDouble(double defaultValue) {
        return value;
    }
    
    @Override
    public long asLong() throws NotConvertibleException {
        return (long)value;
    }
    
    @Override
    public long asLong(long defaultValue) {
        return (long)value;
    }
}
