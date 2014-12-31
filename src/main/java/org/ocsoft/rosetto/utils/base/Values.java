/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.utils.base;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.ocsoft.rosetto.contexts.base.Rosetto;
import org.ocsoft.rosetto.models.base.elements.RosettoValue;
import org.ocsoft.rosetto.models.base.elements.values.BoolValue;
import org.ocsoft.rosetto.models.base.elements.values.DoubleValue;
import org.ocsoft.rosetto.models.base.elements.values.IntValue;
import org.ocsoft.rosetto.models.base.elements.values.ListValue;
import org.ocsoft.rosetto.models.base.elements.values.NullValue;
import org.ocsoft.rosetto.models.base.elements.values.VoidValue;

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
     * 指定した文字列を受け取ってRosettoValueを生成する.<br>
     * 現在のparserのparseElementを呼び出す.
     * @param value StringValueとして生成する値
     * @return 生成したStringValue
     */
    public static RosettoValue create(String value) {
        return Rosetto.getParser().parseElement(value);
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
        List<RosettoValue> values = new LinkedList<RosettoValue>();
        try {
            JsonParser parser = new JsonFactory().createParser(json);
            while(!parser.isClosed()) {
                JsonToken token = parser.nextToken();
                if (token == null) break;
                if(token.isScalarValue())
                    values.add(Values.create(parser.getText()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(values.size() == 0)
            throw new IllegalArgumentException("Invalid JSON Value");
        if(values.size() == 1)
            return values.get(0);
        return new ListValue(values);
    }
    
}
