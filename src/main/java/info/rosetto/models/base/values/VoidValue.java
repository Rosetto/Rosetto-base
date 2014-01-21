/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.values;

import info.rosetto.exceptions.NotConvertibleException;

/**
 * 返り値がなかったことを表す値.<br>
 * どの値にも変換できない.toStringは"VOID", getValueはnullになる.
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
    public ValueType getType() {
        return ValueType.VOID;
    }
    
    /**
     * nullを返す.
     * @return null
     */
    @Override
    public Object getValue() {
        return null;
    }
    
    @Override
    public String toString() {
        return "VOID";
    }
    
    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public boolean toBool() throws NotConvertibleException {
        throw new NotConvertibleException();
    }
    
    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public boolean toBool(boolean defaultValue) {
        return defaultValue;
    }
    
    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public int toInt() throws NotConvertibleException {
        throw new NotConvertibleException();
    }
    
    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public int toInt(int defaultValue) {
        return defaultValue;
    }

    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public double toDouble() throws NotConvertibleException {
        throw new NotConvertibleException();
    }

    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public double toDouble(double defaultValue) {
        return defaultValue;
    }
}
