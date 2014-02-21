/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.elements.values;

import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.state.variables.Scope;
import info.rosetto.system.exceptions.NotConvertibleException;
import info.rosetto.utils.base.Values;

/**
 * 
 * @author tohhy
 *
 */
public class BoolValue implements RosettoValue {
    private static final long serialVersionUID = 1419103880664446476L;
    
    public static final BoolValue TRUE = new BoolValue(true);
    
    public static final BoolValue FALSE = new BoolValue(false);
    
    /**
     * 
     */
    private final boolean value;
    
    /**
     * 
     * @param value
     */
    private BoolValue(boolean value) {
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
                return (value == ((RosettoValue)obj).asBool());
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
        return ValueType.BOOLEAN;
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
    
    @Override
    public boolean asBool() throws NotConvertibleException {
        return value;
    }
    
    @Override
    public boolean asBool(boolean defaultValue) {
        return value;
    }
    
    @Override
    public int asInt() throws NotConvertibleException {
        throw new NotConvertibleException();
    }
    
    @Override
    public int asInt(int defaultValue) {
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

    @Override
    public double asDouble() throws NotConvertibleException {
        throw new NotConvertibleException();
    }
    
    @Override
    public double asDouble(double defaultValue) {
        return defaultValue;
    }

}
