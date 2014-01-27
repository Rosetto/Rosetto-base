/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.utils.base;

import info.rosetto.models.base.values.BoolValue;
import info.rosetto.models.base.values.DoubleValue;
import info.rosetto.models.base.values.IntValue;
import info.rosetto.models.base.values.NullValue;
import info.rosetto.models.base.values.StringValue;
import info.rosetto.models.base.values.VoidValue;

/**
 * Rosetto中の値の生成・操作に関するユーティリティクラス.
 * @author tohhy
 */
public class Values {
    /**
     * VoidValueのシングルトンインスタンスを返す.
     */
    public static final VoidValue VOID = VoidValue.INSTANCE;
    
    /**
     * NullValueのシングルトンインスタンスを返す.
     */
    public static final NullValue NULL = NullValue.INSTANCE;
    
    /**
     * 指定した文字列を受け取ってStringValueを生成する.
     * @param value StringValueとして生成する値
     * @return 生成したStringValue
     */
    public static StringValue create(String value) {
        return new StringValue(value);
    }
    
    /**
     * 指定した整数を受け取ってIntValueを生成する.<br>
     * 値は内部的にはlong精度で保持される.
     * @param value IntValueとして生成する値
     * @return 生成したIntValue
     */
    public static IntValue create(int value) {
        return new IntValue(value);
    }
    
    /**
     * 指定したlong整数を受け取ってIntValueを生成する.<br>
     * 値は内部的にはlong精度で保持される.
     * @param value IntValueとして生成する値
     * @return 生成したIntValue
     */
    public static IntValue create(long value) {
        return new IntValue(value);
    }
    
    /**
     * 指定した倍精度浮動小数点数を受け取ってDoubleValueを生成する.
     * @param value DoubleValueとして生成する値
     * @return 生成したDoubleValue
     */
    public static DoubleValue create(double value) {
        return new DoubleValue(value);
    }
    
    /**
     * 指定した真偽値を受け取ってBoolValueを生成する.
     * @param value BoolValueとして生成する値
     * @return 生成したBoolValue
     */
    public static BoolValue create(boolean value) {
        return new BoolValue(value);
    }
}
