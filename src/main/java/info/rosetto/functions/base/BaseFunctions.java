/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.functions.base;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.function.ExpandedArguments;
import info.rosetto.models.base.function.FunctionPackage;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.models.base.values.ValueType;
import info.rosetto.utils.base.Values;

/**
 * Rosettoの基本的な関数.
 * @author tohhy
 */
public class BaseFunctions extends FunctionPackage {
    
    /**
     * シングルトンインスタンス.
     */
    private static BaseFunctions instance;
    
    /**
     * BaseFunctionsのインスタンスを取得する.
     * @return BaseFunctionsのインスタンス
     */
    public static BaseFunctions getInstance() {
        if(instance == null) {
            instance = new BaseFunctions();
        }
        return instance;
    }
    
    /**
     * コンストラクタは公開しない.
     */
    private BaseFunctions() {
        super(pass, getglobal, def);
    }
    
    /**
     * 「何もしない」関数.実行すると何もせずにVOIDを返す.
     */
    public static final RosettoFunction pass = new RosettoFunction("pass") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(ExpandedArguments args) {
            return Values.VOID;
        }
    };
    
    /**
     * 指定したグローバル変数に指定した値をセットする.
     */
    public static final RosettoFunction def = new RosettoFunction("def",
            "key", "value") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(ExpandedArguments args) {
            String key = args.get("key").asString("");
            RosettoValue value = args.get("value");
            Contexts.define(key, value);
            return Values.VOID;
        }
    };
    
    /**
     * 指定したグローバル変数に指定した値をセットする.
     */
    public static final RosettoFunction getglobal = new RosettoFunction("getglobal",
            "key") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(ExpandedArguments args) {
            RosettoValue key = args.get("key");
            if(key.getType() == ValueType.NULL) return Values.NULL;
            return Contexts.get(key.asString());
        }
    };

}
