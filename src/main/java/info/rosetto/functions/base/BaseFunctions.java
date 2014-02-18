/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.functions.base;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.function.FunctionPackage;
import info.rosetto.models.base.function.RosettoArguments;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.models.base.values.ValueType;
import info.rosetto.models.state.variables.Scope;
import info.rosetto.utils.base.Values;

/**
 * Rosettoの基本的な関数.<br>
 * 初期状態でimportとuseが自動的に行われる.
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
        super(pass, getglobal, def, getlocal, set, use);
    }
    
    /**
     * 「何もしない」関数.実行すると何もせずにVOIDを返す.
     */
    public static final RosettoFunction pass = new RosettoFunction("pass") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(Scope scope, RosettoArguments rawArgs) {
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
        protected RosettoValue run(Scope scope, RosettoArguments rawArgs) {
            String key = scope.get("key").asString("");
            RosettoValue value = scope.get("value");
            Contexts.define(key, value);
            return Values.VOID;
        }
    };
    
    /**
     * 指定したキーで表されるグローバル変数を取得する.<br>
     * 通常は$記号を使ったエイリアスで呼び出す.
     */
    public static final RosettoFunction getglobal = new RosettoFunction("getglobal",
            "key") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(Scope scope, RosettoArguments rawArgs) {
            RosettoValue key = scope.get("key");
            if(key.getType() == ValueType.NULL) return Values.NULL;
            return Contexts.get(key.asString());
        }
    };
    
    /**
     * ローカル変数に指定した値をセットする.
     */
    public static final RosettoFunction set = new RosettoFunction("set",
            "key", "value") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(Scope scope, RosettoArguments rawArgs) {
            String key = scope.get("key").asString("");
            RosettoValue value = scope.get("value");
            scope.getParent().set(key, value);
            return Values.VOID;
        }
    };
    
    /**
     * 指定したキーで表されるローカル変数を取得する.<br>
     * 通常は@記号を使ったエイリアスで呼び出す.
     */
    public static final RosettoFunction getlocal = new RosettoFunction("getlocal",
            "**key**") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(Scope scope, RosettoArguments rawArgs) {
            RosettoValue key = scope.get("**key**");
            if(key.getType() == ValueType.NULL) return Values.NULL;
            return scope.get(key.asString());
        }
    };
    
    /**
     * 指定したパッケージの関数を直接利用可能にする.
     */
    public static final RosettoFunction use = new RosettoFunction("use",
            "package") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(Scope scope, RosettoArguments rawArgs) {
            RosettoValue pkg = scope.get("package");
            Contexts.usePackage(pkg.asString());
            return Values.VOID;
        }
    };

}
