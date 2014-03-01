/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.function;

import org.apache.commons.lang3.StringUtils;

import info.rosetto.models.base.elements.RosettoValue;

/**
 * 無名関数を示すクラス.
 * @author tohhy
 */
public abstract class LambdaFunction extends RosettoFunction {
    private static final long serialVersionUID = 1198484922087018955L;
    
    /**
     * 指定した引数名をとるラムダ関数を生成する.<br>
     * リスト値が渡ってきた場合はそれぞれを引数名とみなし、単一の値が渡ってきた場合はそれを引数名とみなす.
     * @param args このラムダ関数が盗る引数名
     */
    public LambdaFunction(RosettoValue args) {
        super("", args);
    }
    
    /**
     * 指定した引数名をとるラムダ関数を生成する.
     * @param args このラムダ関数が盗る引数名
     */
    public LambdaFunction(String...args) {
        super("", args);
    }
    
    @Override
    public String toString() {
        return "[fn (" + StringUtils.join(getArguments(), " ") + ")]";
    }
    
}
