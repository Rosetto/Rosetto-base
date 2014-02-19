/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.models.base.values;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.state.parser.Parser;
import info.rosetto.system.exceptions.NotConvertibleException;
import info.rosetto.utils.base.TextUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * リストを拡張し、キーと値の組を格納できるようにしたもの.<br>
 * 以下のようにごちゃ混ぜ(=hash)に値を放り込むことができる.<br>
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
public class HashedList implements RosettoValue {
    private static final long serialVersionUID = -5778537199758610111L;
    
    private final LinkedList<RosettoValue> list;
    
    private final Map<String, RosettoValue> map;
    
    public HashedList(String...values) {
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
    public String toString() {
        return (list.size() > 0 ? list.toString() : "") + (map.size() > 0 ? map.toString() : "");
    }
    
    public boolean hasMappedValue() {
        return map != null && map.size() > 0;
    }
    
    @Override
    public ValueType getType() {
        return ValueType.LIST;
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
}
