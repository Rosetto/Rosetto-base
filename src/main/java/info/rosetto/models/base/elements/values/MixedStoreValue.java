/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.models.base.elements.values;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.MixedStore;
import info.rosetto.models.base.elements.RosettoList;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.state.parser.Parser;
import info.rosetto.models.state.variables.Scope;
import info.rosetto.system.exceptions.NotConvertibleException;
import info.rosetto.utils.base.TextUtils;
import info.rosetto.utils.base.Values;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * リストを拡張し、キーと値の組を格納できるようにしたもの.<br>
 * 以下のようにごちゃ混ぜに値を放り込むことができる.<br>
 * <br>
 * {foo=10 bar=100 2 6 a=1 9 3}<br>
 * <br>
 * Rosetto中ではこのハッシュドリストは以下のように整理される.<br>
 * <br>
 * Map {foo=10, bar=100, a=1}<br>
 * List (2,6,9,3)<br>
 * <br>
 * 先にキーワードを持つものが抽出されてマップとして保持され、その後に残りの要素がリストとして保持される.
 * @author tohhy
 */
public class MixedStoreValue implements RosettoList {
    private static final long serialVersionUID = -5778537199758610111L;
    
    private final LinkedList<RosettoValue> list;
    
    private final Map<String, RosettoValue> map;
    
    public MixedStoreValue(MixedStore store) {
        this.list = new LinkedList<RosettoValue>(store.getList());
        this.map = new HashMap<String, RosettoValue>(store.getMap());
    }
    
    public MixedStoreValue(String...values) {
        this.list = new LinkedList<RosettoValue>();
        this.map = new HashMap<String, RosettoValue>();
        Parser parser = Contexts.getParser();
        for(String str : values) {
            int equalPosition = str.indexOf("=");
            if(equalPosition == -1) {
                list.add(parser.parseElement(TextUtils.removeDoubleQuote(str)));
            } else {
                String key = str.substring(0, equalPosition);
                String value = TextUtils.removeDoubleQuote(str.substring(equalPosition + 1));
                map.put(key, parser.parseElement(value));
            }
        }
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
        return (list.size() > 0 ? list.toString() : "") + (map.size() > 0 ? map.toString() : "");
    }
    

    @Override
    public RosettoValue evaluate(Scope scope) {
        return this;
    }
    
    public boolean hasMappedValue() {
        return map != null && map.size() > 0;
    }
    
    public RosettoValue get(String mapKey) {
        return map.get(mapKey);
    }
    
    @Override
    public RosettoValue first() {
        return list.get(0);
    }

    @Override
    public RosettoValue rest() {
        if(list.size() == 0 || list.size() == 1) return Values.NULL;
        if(list.size() == 2) return list.get(1);
        return new ListValue(list.subList(1, list.size()));
    }

    public RosettoValue getAt(int listIndex) {
        return list.get(listIndex);
    }
    
    @Override
    public ValueType getType() {
        return ValueType.HASHED_LIST;
    }
    
    @Override
    public Object getValue() {
        return this;
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

    public LinkedList<RosettoValue> getList() {
        return list;
    }

    public Map<String, RosettoValue> getMap() {
        return map;
    }
}
