/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.utils.base;

import info.rosetto.models.base.values.StringValue;
import info.rosetto.models.base.values.VoidValue;

/**
 * Rosetto中の値の操作に関するユーティリティクラス.
 * @author tohhy
 */
public class Values {
    /**
     * VoidValueのシングルトンインスタンスを返す.
     */
    public static final VoidValue VOID = VoidValue.INSTANCE;
    
    /**
     * 指定した文字列を受け取ってStringValueを生成する.
     * @param value
     * @return
     */
    public static StringValue create(String value) {
        return new StringValue(value);
    }

}
