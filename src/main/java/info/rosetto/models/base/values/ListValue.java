/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.models.base.values;

import info.rosetto.exceptions.NotConvertibleException;
import info.rosetto.utils.base.Values;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * RosettoValueの連続した列を表すRosettoValue.
 * @author tohhy
 */
public class ListValue implements RosettoValue {
    private static final long serialVersionUID = -5778537199758610111L;
    /**
     * 
     */
    private final List<RosettoValue> list = new LinkedList<RosettoValue>();
    
    public ListValue(List<RosettoValue> values) {
        list.addAll(values);
    }
    
    public ListValue(RosettoValue...values) {
        for(RosettoValue v : values) 
            list.add(v);
    }
    
    public RosettoValue first() {
        if(list.size() == 0) return Values.NULL;
        return list.get(0);
    }
    
    public RosettoValue rest() {
        if(list.size() == 0 || list.size() == 1) return Values.NULL;
        if(list.size() == 2) return list.get(1);
        return new ListValue(list.subList(1, list.size()));
    }
    
    public int getSize() {
        return list.size();
    }
    
    @Override
    public ValueType getType() {
        return ValueType.LIST;
    }

    @Override
    public Object getValue() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public String asString() throws NotConvertibleException {
        return list.toString();
    }

    @Override
    public String asString(String defaultValue) {
        return list.toString();
    }

    @Override
    public boolean asBool() throws NotConvertibleException {
        throw new NotConvertibleException();
    }

    @Override
    public boolean asBool(boolean defaultValue) {
        return defaultValue;
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