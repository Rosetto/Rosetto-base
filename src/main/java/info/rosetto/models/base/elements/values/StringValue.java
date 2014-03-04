/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.elements.values;

import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.system.Scope;
import info.rosetto.system.exceptions.NotConvertibleException;
import info.rosetto.utils.base.Values;

/**
 * 文字列を実体とするRosettoValue.<br>
 * スクリプト中に入力された引数は全てStringValueとして渡ってくる.
 * @author tohhy
 */
public class StringValue implements RosettoValue {
    private static final long serialVersionUID = 3850215983833671296L;
    /**
     * このStringValueが保持する値.
     */
    private final String value;
    
    /**
     * 指定した文字列でStringValueを初期化する.
     * @param value このStringValueの実体となる値
     * @throws IllegalArgumentException valueがnullの場合
     */
    public StringValue(String value) {
        if(value == null)
            throw new IllegalArgumentException("value must not be null");
        this.value = value;
    }
    
    @Override
    public String toString() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RosettoValue) {
            try {
                return (value.equals(((RosettoValue)obj).asString()));
            } catch (NotConvertibleException e) {
                return false;
            }
        }
        return false;
    }
    
    @Override
    public ValueType getType() {
        return ValueType.STRING;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public RosettoValue evaluate(Scope scope) {
        return this;
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
    public String asString() {
        return value;
    }
    
    @Override
    public String asString(String defaultValue) {
        return value;
    }

    /**
     * valueを全て小文字に変換したときに"true"である場合はtrue.<br>
     * valueを全て小文字に変換したときに"false"である場合はfalse.<br>
     * 長さ0の文字列の場合はfalse.<br>
     * それ以外の場合はNotConvertibleExceptionをスローする.
     */
    @Override
    public boolean asBool() throws NotConvertibleException {
        if(value.toLowerCase().equals("true")) return true;
        if(value.toLowerCase().equals("false")) return false;
        if(value.length() == 0) return false;
        throw new NotConvertibleException();
    }
    
    /**
     * valueを全て小文字に変換したときに"true"である場合はtrue.<br>
     * valueを全て小文字に変換したときに"false"である場合はfalse.<br>
     * 長さ0の文字列の場合はfalse.<br>
     * それ以外の場合はデフォルト値を返す.
     */
    @Override
    public boolean asBool(boolean defaultValue) {
        if(value.toLowerCase().equals("true")) return true;
        if(value.toLowerCase().equals("false")) return false;
        if(value.length() == 0) return false;
        return defaultValue;
    }
    
    @Override
    public int asInt() throws NotConvertibleException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new NotConvertibleException();
        }
    }

    @Override
    public int asInt(int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public long asLong() throws NotConvertibleException {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new NotConvertibleException();
        }
    }

    @Override
    public long asLong(long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public double asDouble() throws NotConvertibleException {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new NotConvertibleException();
        }
    }

    @Override
    public double asDouble(double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

}
