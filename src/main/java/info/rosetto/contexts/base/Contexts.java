/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.contexts.base;

import info.rosetto.functions.base.BaseFunctions;
import info.rosetto.models.base.engine.EngineModel;
import info.rosetto.models.base.function.FunctionPackage;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.namespace.NameSpace;
import info.rosetto.models.base.parser.RosettoParser;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.utils.base.Values;

/**
 * Rosettoの実行中の状態全体を保持するコンテキスト.<br>
 * RosettoPlayerが再生にあたって必要とする情報は全てこのクラスのシングルトンインスタンスから取得される.<br>
 * このクラスのinitializeメソッドが呼び出されることでRosettoが動作可能な状態になる.
 * @author tohhy
 */
public class Contexts {
    /**
     * シングルトンインスタンス.
     */
    private static final Contexts instance = new Contexts();
    
    /**
     * 全てのグローバル変数を保持するインスタンス.
     */
    private VariableContext globalVars;
    
    /**
     * すべての関数を保持するインスタンス.
     */
    private ActionContext functions;
    
    /**
     * Rosettoのシステム状態を保持するインスタンス.
     */
    private SystemContext system;
    
    /**
     * 初期化が済んでいるかどうか.
     */
    private boolean isInitialized = false;
    
    /**
     * コンストラクタは非公開.
     */
    private Contexts() {}
    
    /**
     * Contextsを初期化して使用可能な状態にする.<br>
     * @throws IllegalStateException すでにContextsが初期化されている場合
     */
    public static void initialize() {
        if(instance.isInitialized)
            throw new IllegalStateException("Contexts already initialized");
        
        instance.globalVars = new VariableContext();
        instance.functions = new ActionContext();
        instance.system = new SystemContext();
        instance.isInitialized = true;
    }
    
    /**
     * Contextsを破棄して使用不可能な状態にする.
     */
    public static void dispose() {
        instance.globalVars = null;
        instance.functions = null;
        instance.system = null;
        instance.isInitialized = false;
    }
    
    /**
     * Contextsが初期化されて使用可能な状態にあるかどうかを返す.
     * @return Contextsが初期化されて使用可能な状態にあるかどうか
     */
    public static boolean isInitialized() {
        return instance.isInitialized;
    }
    
    /**
     * 初期化済みでない場合にIllegalStateExceptionを投げる.
     */
    private static void initializedCheck() {
        if(!instance.isInitialized)
            throw new IllegalStateException("Contexts not initialized yet");
    }
    
    /**
     * 指定したグローバル変数に保存されている値を取得する.<br>
     * @param key 値を取得する変数名
     * @return 取得した値、変数が存在しなければnull
     */
    public static RosettoValue get(String key) {
        initializedCheck();
        if(key == null) return Values.NULL;
        return instance.globalVars.get(key);
    }
    
    /**
     * 指定したグローバル変数に指定した値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void define(String key, RosettoValue value) {
        initializedCheck();
        if(key == null || key.length() == 0)
            throw new IllegalArgumentException("key must not be empty");
        if(key.endsWith("."))
            throw new IllegalArgumentException("name must not end with dot");
        
        int lastDotIndex = key.lastIndexOf(".");
        if(lastDotIndex > 0) {
            String packageName = key.substring(0, lastDotIndex);
            NameSpace ns = instance.globalVars.getNameSpace(packageName);
            ns.set(key.substring(lastDotIndex+1), value);
        } else {
            instance.globalVars.define(key, value);
        }
    }
    
    /**
     * 指定したグローバル変数に指定した値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void define(String key, boolean value) {
        define(key, Values.create(value));
    }
    
    /**
     * 指定したグローバル変数に指定した値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void define(String key, int value) {
        define(key, Values.create(value));
    }
    
    /**
     * 指定したグローバル変数に指定した値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void define(String key, long value) {
        define(key, Values.create(value));
    }
    
    /**
     * 指定したグローバル変数に指定した値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void define(String key, double value) {
        define(key, Values.create(value));
    }
    
    /**
     * 指定したグローバル変数に指定した値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void define(String key, String value) {
        define(key, Values.create(value));
    }
    
    /**
     * 関数コンテキストから指定名の関数を取得する.
     * 値が関数でない場合、存在しない場合はBaseFunctions.passが返る.
     * @param key 値を取得する変数名
     * @return 取得した関数. 値が関数でないか、変数が存在しなければBaseFunctions.pass
     * TODO マクロへの対応
     */
    public static RosettoFunction getFunction(String key) {
        RosettoValue v = instance.functions.get(key);
        return (v instanceof RosettoFunction) ? (RosettoFunction)v : BaseFunctions.pass;
    }
    
    /**
     * 指定した関数を関数コンテキストに追加する.
     * @param func 関数コンテキストに追加する関数
     */
    public static void defineFunction(RosettoFunction func) {
        initializedCheck();
        instance.functions.defineAction(func);
    }
    
    /**
     * 指定した関数パッケージを読み込み、絶対参照で呼び出せるようにする.
     * @param p 読み込む関数パッケージ
     * @param packageName 読み込んだ関数パッケージにつける名称
     */
    public static void importPackage(FunctionPackage p, String packageName) {
        initializedCheck();
        instance.functions.importPackage(p, packageName);
    }
    
    /**
     * 指定した関数パッケージを関数コンテキストに読み込み、含まれる関数をすべて直接参照可能にする.
     * @param packageName 読み込むパッケージの名称
     */
    public static void usePackage(String packageName) {
        initializedCheck();
        instance.functions.usePackage(packageName);
    }
    
    /**
     * このContextが保持する名前空間全体のインスタンスを取得する.
     * @return このContextが保持する名前空間全体のインスタンス
     */
    public static VariableContext getVariableContext() {
        initializedCheck();
        return instance.globalVars;
    }
    
    /**
     * このContextが保持する名前空間全体のインスタンスを指定したインスタンスに入れ替える.<br>
     * ゲーム中の状態を示す変数は全てWholeSpaceから取り出されるため、
     * シリアライズしたWholeSpaceをこのメソッドでセットするとその時点のゲーム状態をロードできる.
     * @param variableContext 新しく指定する名前空間全体のインスタンス
     */
    public static void setVariableContext(VariableContext variableContext) {
        initializedCheck();
        if(variableContext == null)
            throw new IllegalArgumentException("wholespace must not be null");
        instance.globalVars = variableContext;
    }
    
    /**
     * 指定名の名前空間を取得する.<br>
     * コンテキスト中に指定名の名前空間がまだ存在しない場合は生成して返す.
     * @param name 取得する名前空間
     * @return 取得した名前空間
     */
    public static NameSpace getNameSpace(String name) {
        return instance.globalVars.getNameSpace(name);
    }

    /**
     * 現在のコンテキストで利用するパーサーを取得する.
     * @return 現在のコンテキストで利用するパーサー
     */
    public static RosettoParser getParser() {
        initializedCheck();
        return instance.system.getParser();
    }
    
    /**
     * 現在のコンテキストで利用するパーサーを変更する.
     * @param parser 現在のコンテキストで利用するパーサー
     */
    public static void setParser(RosettoParser parser) {
        initializedCheck();
        instance.system.setParser(parser);
    }
    
    /**
     * 現在のコンテキストで利用するパーサーを取得する.
     * @return 現在のコンテキストで利用するパーサー
     */
    public static EngineModel getEngine() {
        initializedCheck();
        return instance.system.getEngine();
    }
    
    /**
     * 現在のコンテキストで利用するパーサーを変更する.
     * @param parser 現在のコンテキストで利用するパーサー
     */
    public static void setParser(EngineModel parser) {
        initializedCheck();
        instance.system.setEngine(parser);
    }

}
