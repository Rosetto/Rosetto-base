/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.functions.base;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.contexts.base.NameSpace;
import info.rosetto.models.base.function.ExpandedArguments;
import info.rosetto.models.base.function.FunctionPackage;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.utils.base.Values;

/**
 * Rosettoの基本的な関数.<br>
 * 名前空間の生成時に自動的にuseされる.
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
        super(pass, refer, include, namespace);
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
     * 指定したパッケージに含まれる全変数を、指定名のパッケージとして参照できるようにする.<br>
     * 例えば以下のようにした場合は、rosetto.text.brをtext.brのようにして呼び出せるようになる.<br>
     * [refer package=rosetto.text as=text]
     */
    public static final RosettoFunction refer = new RosettoFunction("refer",
            "ns", "as") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(ExpandedArguments args) {
            String packageName = args.get("ns").asString("");
            String refName = args.get("as").asString("");
            if(packageName.length() > 0 && refName.length() > 0) {
                Contexts.refer(packageName, refName);
            }
            return Values.VOID;
        }
    };
    
    /**
     * 指定したパッケージに含まれる全変数を現在のパッケージに取り込む.<br>
     * 取り込んだ変数は全て現在のパッケージ直下に定義した変数と同様に扱われる.<br>
     * 例えば標準のtextパッケージをincludeした場合は、text.brやtext.pは単にbrやpで呼び出せるようになる.<br>
     */
    public static final RosettoFunction include = new RosettoFunction("include",
            "ns") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(ExpandedArguments args) {
            String packageName = args.get("ns").asString("");
            if(packageName.length() > 0) {
                Contexts.include(packageName);
            }
            return Values.VOID;
        }
    };
    
    /**
     * 現在アクティブな名前空間を指定する.
     */
    public static final RosettoFunction namespace = new RosettoFunction("namespace",
            "ns") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(ExpandedArguments args) {
            String packageName = args.get("ns").asString("");
            if(packageName.length() > 0) {
                Contexts.setCurrentNameSpace(packageName);
            }
            return Values.VOID;
        }
    };
    
    /**
     * 現在の名前空間の指定した変数に指定した値をセットする.
     */
    public static final RosettoFunction set = new RosettoFunction("set",
            "key", "value", "ns=") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(ExpandedArguments args) {
            String key = args.get("key").asString("");
            RosettoValue value = args.get("value");
            String packageName = args.get("ns").asString("");
            NameSpace ns = Contexts.getCurrentNameSpace();
            if(packageName.length() > 0) {
                ns = Contexts.getNameSpace(packageName);
            }
            
            if(ns != null) {
                if(key.contains(".")) {
                    Contexts.set(ns.getName() + "." + key, value);
                } else {
                    ns.set(key, value);
                }
            }
            
            return Values.VOID;
        }
    };

}
