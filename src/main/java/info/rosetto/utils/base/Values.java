/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.utils.base;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.values.BoolValue;
import info.rosetto.models.base.values.DoubleValue;
import info.rosetto.models.base.values.IntValue;
import info.rosetto.models.base.values.NullValue;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.models.base.values.VoidValue;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;


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
     * 指定した文字列を受け取ってRosettoValueを生成する.
     * @param value StringValueとして生成する値
     * @return 生成したStringValue
     */
    public static RosettoValue create(String value) {
        return Contexts.getParser().parseElement(value);
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
        return value ? BoolValue.TRUE : BoolValue.FALSE;
    }
    
    /**
     * 指定したJSON表現からRosettoValueを生成する.
     * @param json
     * @return
     * @throws IOException 
     * @throws JsonParseException 
     */
    public static RosettoValue createFromJSON(String json) 
            throws JsonParseException {
        try {
            JsonParser parser = new JsonFactory().createParser(json);
            
            while(parser.isClosed()) {
                JsonToken token = parser.nextToken();
                if (token == null) break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
