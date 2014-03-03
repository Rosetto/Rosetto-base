/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.functions.base;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.base.elements.values.ActionCall;
import info.rosetto.models.base.elements.values.LambdaFunction;
import info.rosetto.models.base.elements.values.ListValue;
import info.rosetto.models.base.elements.values.RosettoFunction;
import info.rosetto.models.base.elements.values.ScriptValue;
import info.rosetto.models.system.FunctionPackage;
import info.rosetto.models.system.Scope;
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
        super(pass, 
                getglobal, def, getlocal, set, 
                doActions, defn, fn, defmacro, macro, 
                use);
    }
    
    /**
     * 「何もしない」関数.実行すると何もせずにVOIDを返す.
     */
    public static final RosettoFunction pass = new RosettoFunction("pass") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
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
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
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
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
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
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
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
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
            RosettoValue key = scope.get("**key**");
            if(key.getType() == ValueType.NULL) return Values.NULL;
            return scope.get(key.asString());
        }
    };

    public static final RosettoFunction defn = new RosettoFunction("defn", 
            "name", "args", "action") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected Scope createScope(ListValue args, Scope parentScope) {
            RosettoValue name = args.getAt(0);
            RosettoValue list = args.getAt(1);
            RosettoValue actionCall = args.getAt(2);
            Scope scope = new Scope(parentScope);
            scope.set("name", name);
            scope.set("args", list);
            scope.set("action", actionCall);
            return scope;
        }
        
        @Override
        protected RosettoValue run(Scope scope, ListValue args) {
            RosettoValue argsValue = scope.get("args");
            RosettoValue actionValue = scope.get("action");
            if(actionValue.getType() == ValueType.ACTION_CALL) {
                String name = scope.get("name").asString();
                final ActionCall ac = (ActionCall) actionValue;
                RosettoFunction f = new RosettoFunction(name, argsValue) {
                    private static final long serialVersionUID = 1L;
                    @Override
                    protected RosettoValue run(Scope scope, ListValue args) {
                        return ac.evaluate(scope);
                    }
                };
                Contexts.defineFunction(f);
                return f;
            }
            return Values.NULL;
        }
    };

    /**
     * ラムダ関数を生成する.
     */
    public static final RosettoFunction fn = new RosettoFunction("fn", 
            "args", "action") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected Scope createScope(ListValue args, Scope parentScope) {
            //引数のうち最初のものを引数リスト、それ以降を順に実行される実装内容とみなす
            RosettoValue list = args.first();
            RosettoValue actions = args.rest();
            Scope scope = new Scope(parentScope);
            scope.set("args", list);
            scope.set("action", actions);
            return scope;
        }
        
        @Override
        protected RosettoValue run(Scope scope, ListValue args) {
            final RosettoValue argsValue = scope.get("args");
            final RosettoValue actionValue = scope.get("action");
            validateType(argsValue, ValueType.LIST);
            validateType(actionValue, ValueType.ACTION_CALL, ValueType.LIST);
            
            RosettoFunction f = new LambdaFunction(argsValue) {
                private static final long serialVersionUID = 1L;
                @Override
                protected RosettoValue run(Scope scope, ListValue args) {
                    return doActions(scope, actionValue);
                }
            };
            return f;
        }
    };
    
    
    /**
     * 引数に与えたアクションを順に実行する.
     */
    public static final RosettoFunction doActions = new RosettoFunction("do", 
            "action") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected Scope createScope(ListValue args, Scope parentScope) {
            //引数を順に実行される実装内容とみなす
            Scope scope = new Scope(parentScope);
            scope.set("action", args);
            return scope;
        }
        
        @Override
        protected RosettoValue run(Scope scope, ListValue args) {
            RosettoValue actionValue = scope.get("action");
            validateType(actionValue, ValueType.ACTION_CALL, ValueType.LIST);
            return doActions(scope, actionValue);
        }
    };
    
    public static final RosettoFunction defmacro = new RosettoFunction("defmacro", 
            "name", "args", "action") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected Scope createScope(ListValue args, Scope parentScope) {
            RosettoValue name = args.getAt(0);
            RosettoValue list = args.getAt(1);
            RosettoValue script = args.getAt(2);
            if(args.size() == 2) {
                list = null;
                script = args.getAt(2);
            }
            Scope scope = new Scope(parentScope);
            scope.set("name", name);
            if(list != null) scope.set("args", list);
            scope.set("script", script);
            return scope;
        }
        
        @Override
        protected RosettoValue run(Scope scope, ListValue args) {
            RosettoValue nameValue = scope.get("name");
            RosettoValue argsValue = scope.get("args");
            RosettoValue scriptValue = scope.get("script");
            if(scriptValue.getType() == ValueType.SCRIPT) {
                ScriptValue sv = (ScriptValue) scriptValue;
                Contexts.defineMacro(nameValue.asString(), sv);
                return sv;
            }
            return Values.NULL;
        }
    };
    
    public static final RosettoFunction macro = new RosettoFunction("macro", 
            "args", "script") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected Scope createScope(ListValue args, Scope parentScope) {
            RosettoValue list = args.getAt(0);
            RosettoValue script = args.getAt(1);
            Scope scope = new Scope(parentScope);
            scope.set("args", list);
            scope.set("script", script);
            return scope;
        }
        
        @Override
        protected RosettoValue run(Scope scope, ListValue args) {
            RosettoValue argsValue = scope.get("args");
            RosettoValue scriptValue = scope.get("script");
            if(scriptValue.getType() == ValueType.SCRIPT) {
                final ScriptValue script = (ScriptValue) scriptValue;
                RosettoFunction f = new LambdaFunction(argsValue) {
                    private static final long serialVersionUID = 1L;
                    @Override
                    protected RosettoValue run(Scope scope, ListValue args) {
                        return script.evaluate(scope);
                    }
                };
                return f;
            }
            return Values.NULL;
        }
    };
    
    /**
     * 指定したパッケージの関数を直接利用可能にする.
     */
    public static final RosettoFunction use = new RosettoFunction("use",
            "package") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
            RosettoValue pkg = scope.get("package");
            Contexts.usePackage(pkg.asString());
            return Values.VOID;
        }
    };
    
    /**
     * 引数に与えたスコープを用いて引数に与えたアクションまたはアクションのリストを順に実行する.
     * @param scope
     * @param actions
     * @return
     */
    private static RosettoValue doActions(Scope scope, RosettoValue actions) {
        RosettoValue result = Values.NULL;
        while(true) {
            RosettoValue action = actions.first();
            if(action.getType() == ValueType.ACTION_CALL) {
                final ActionCall ac = (ActionCall) action;
                result = ac.evaluate(scope);
            }
            if(actions.size() == 1) return result;
            actions = actions.rest();
        }
    }

}
