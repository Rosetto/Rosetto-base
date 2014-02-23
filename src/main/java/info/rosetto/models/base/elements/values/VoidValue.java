/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.elements.values;

import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.state.variables.Scope;
import info.rosetto.system.exceptions.NotConvertibleException;

/**
 * 「なにもない」「値ではない」ことを表す仮想的な値.<br>
 * 関数の返り値がなかった場合などに返される.<br>
 * <br>
 * Voidを返す関数は必ずVoidを返す.実行時に動的に返り値がVoidかどうか決まるような関数は定義してはいけない.
 * そうした場合はNullを用いる.<br>
 * <br>
 * どの値にも変換できない. getValueはUnsupportedOperationExceptionをスローする.
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
    public String toString() {
        return "VoidValue";
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof VoidValue) {
            return true;
        }
        return false;
    }
    

    @Override
    public RosettoValue evaluate(Scope scope) {
        return this;
    }
    
    
    @Override
    public RosettoValue first() {
        throw new UnsupportedOperationException("this value is void");
    }
    
    @Override
    public RosettoValue rest() {
        throw new UnsupportedOperationException("this value is void");
    }
    
    @Override
    public RosettoValue cons(RosettoValue head) {
        throw new UnsupportedOperationException("this value is void");
    }
    
    @Override
    public int size() {
        return 0;
    }
    
    @Override
    public RosettoValue getAt(int index) {
        throw new UnsupportedOperationException("this value is void");
    }
    
    @Override
    public ValueType getType() {
        return ValueType.VOID;
    }
    
    /**
     * UnsupportedOperationExceptionをスローする.
     */
    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("this value is void");
    }
    
    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public String asString() throws NotConvertibleException {
        throw new NotConvertibleException();
    }

    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public String asString(String defaultValue) {
        return defaultValue;
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
    
    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public long asLong() throws NotConvertibleException {
        throw new NotConvertibleException();
    }
    
    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public long asLong(long defaultValue) {
        return defaultValue;
    }

}
