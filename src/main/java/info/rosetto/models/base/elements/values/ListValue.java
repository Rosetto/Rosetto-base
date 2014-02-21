/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.elements.values;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoList;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.state.parser.Parser;
import info.rosetto.models.state.variables.Scope;
import info.rosetto.system.exceptions.NotConvertibleException;
import info.rosetto.utils.base.Values;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.concurrent.Immutable;

/**
 * Listを表すRosettoValue.
 * @author tohhy
 */
@Immutable
public class ListValue implements RosettoValue, RosettoList {
    private static final long serialVersionUID = -5778537199758610111L;
    
    public static final ListValue EMPTY = new ListValue(new String[0]);
    
    private final LinkedList<RosettoValue> list = new LinkedList<RosettoValue>();
    
    public ListValue(List<RosettoValue> values) {
        list.addAll(values);
    }
    
    public ListValue(RosettoValue...values) {
        for(RosettoValue v : values) 
            list.add(v);
    }
    
    public ListValue(String...values) {
        Parser parser = Contexts.getParser();
        for(String s : values) 
            list.add(parser.parseElement(s));
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RosettoValue) {
            return ((RosettoValue)obj).asString().equals(this.asString());
        }
        return false;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        int len = list.size();
        int i=0;
        for(RosettoValue v : list) {
            sb.append(v.toString());
            if(i != len-1) sb.append(" ");
            i++;
        }
        sb.append(")");
        return sb.toString();
    }
    

    @Override
    public RosettoValue evaluate(Scope scope) {
        return this;
    }

    public int getSize() {
        return list.size();
    }
    
    public List<RosettoValue> getList() {
        return Collections.unmodifiableList(list);
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
    
    public RosettoValue getAt(int index) {
        return list.get(index);
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
        return toString();
    }

    @Override
    public String asString(String defaultValue) {
        return toString();
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
