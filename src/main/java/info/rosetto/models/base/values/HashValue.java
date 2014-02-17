/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.values;

import info.rosetto.system.exceptions.NotConvertibleException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * リストを拡張し、キーと値の組を格納できるようにしたもの.<br>
 * 以下のようにごちゃ混ぜ(=hash)に値を放り込むことができる.<br>
 * <br>
 * {foo=10 bar=100 2 6 a=1 9 3}<br>
 * <br>
 * Rosetto中ではこのハッシュは以下のように整理される.<br>
 * <br>
 * kwargs {foo=10, bar=100, a=1}<br>
 * args (2,6,9,3)<br>
 * <br>
 * 先にキーワードを持つものが抽出されてマップとして保持され、その後に残りの要素がリストとして保持される.
 * @author tohhy
 */
public class HashValue implements RosettoValue {
    private static final long serialVersionUID = -2799398352971209558L;
    
    private final Map<String, RosettoValue> map = new HashMap<String, RosettoValue>();
    private final List<RosettoValue> list = new LinkedList<RosettoValue>();
    
    
    
    @Override
    public ValueType getType() {
        return ValueType.HASH;
    }

    @Override
    public Object getValue() {
        return this;
    }
    @Override
    public String asString() throws NotConvertibleException {
        return list.toString() + map.toString();
    }
    
    @Override
    public String asString(String defaultValue) {
        return list.toString() + map.toString();
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
