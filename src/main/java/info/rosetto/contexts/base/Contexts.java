/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.contexts.base;

import info.rosetto.models.base.blocks.RosettoMacro;
import info.rosetto.models.base.elements.RosettoAction;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.function.FunctionPackage;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.state.parser.Parser;
import info.rosetto.models.state.variables.NameSpace;
import info.rosetto.observers.Observatories;
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
    private GlobalVariables globalVars;
    
    /**
     * すべての関数を保持するインスタンス.
     */
    private ActionContext actions;
    
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
     * 指定したコンテキストを用いてContextsを初期化し、使用可能な状態にする.<br>
     * nullを指定したコンテキストはデフォルトの設定で初期化される.
     * @throws IllegalStateException すでにContextsが初期化されている場合
     */
    public static void initialize(GlobalVariables global, 
            ActionContext actions, SystemContext system) {
        if(instance.isInitialized)
            throw new IllegalStateException("Contexts already initialized");
        
        instance.globalVars = (global != null) ? global : new GlobalVariables();
        instance.actions = (actions != null) ? actions : new ActionContext();
        instance.system = (system != null) ? system : new SystemContext();
        instance.isInitialized = true;
    }
    
    /**
     * Contextsを初期化して使用可能な状態にする.
     * @throws IllegalStateException すでにContextsが初期化されている場合
     */
    public static void initialize() {
        initialize(null, null, null);
    }
    
    /**
     * Contextsを破棄して使用不可能な状態にする.
     */
    public static void dispose() {
        instance.globalVars = null;
        instance.actions = null;
        instance.system = null;
        instance.isInitialized = false;
        Observatories.clear();
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
     * 指定したグローバル変数に保存されている値を取得する.
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
     * 指定したグローバル変数に値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void define(String key, boolean value) {
        define(key, Values.create(value));
    }
    
    /**
     * 指定したグローバル変数に値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void define(String key, int value) {
        define(key, Values.create(value));
    }
    
    /**
     * 指定したグローバル変数に値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void define(String key, long value) {
        define(key, Values.create(value));
    }
    
    /**
     * 指定したグローバル変数に値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void define(String key, double value) {
        define(key, Values.create(value));
    }
    
    /**
     * 指定したグローバル変数に値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void define(String key, String value) {
        define(key, Values.create(value));
    }
    
    /**
     * アクションコンテキストから指定名のアクションを取得する.
     * 指定名のアクションが存在しない場合はBaseFunctions.passが返る.
     * @param key 値を取得する変数名
     * @return 取得したアクション. アクションが存在しなければBaseFunctions.pass
     */
    public static RosettoAction getAction(String key) {
        return instance.actions.get(key);
    }
    
    /**
     * 指定した関数をアクションコンテキストに追加する.
     * @param func アクションコンテキストに追加する関数
     */
    public static void defineFunction(RosettoFunction func) {
        initializedCheck();
        instance.actions.defineAction(func);
    }
    
    /**
     * 指定したマクロをアクションコンテキストに追加する.
     * @param macro
     */
    public static void defineMacro(RosettoMacro macro) {
        initializedCheck();
        instance.actions.defineAction(macro);
    }
    
    /**
     * 指定した関数パッケージを読み込み、絶対参照で呼び出せるようにする.
     * @param p 読み込む関数パッケージ
     * @param packageName 読み込んだ関数パッケージにつける名称
     */
    public static void importPackage(FunctionPackage p, String packageName) {
        initializedCheck();
        instance.actions.importPackage(p, packageName);
    }
    
    /**
     * 指定した関数パッケージを関数コンテキストに読み込み、含まれる関数をすべて直接参照可能にする.
     * @param packageName 読み込むパッケージの名称
     */
    public static void usePackage(String packageName) {
        initializedCheck();
        instance.actions.usePackage(packageName);
    }
    
    /**
     * このContextが保持する名前空間全体のインスタンスを取得する.
     * @return このContextが保持する名前空間全体のインスタンス
     */
    public static GlobalVariables getVariableContext() {
        initializedCheck();
        return instance.globalVars;
    }
    
    /**
     * このContextが保持する名前空間全体のインスタンスを指定したインスタンスに入れ替える.<br>
     * ゲーム中の状態を示す変数は全てWholeSpaceから取り出されるため、
     * シリアライズしたWholeSpaceをこのメソッドでセットするとその時点のゲーム状態をロードできる.
     * @param variableContext 新しく指定する名前空間全体のインスタンス
     */
    public static void setVariableContext(GlobalVariables variableContext) {
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
    public static Parser getParser() {
        initializedCheck();
        return instance.system.getParser();
    }
    
    /**
     * 現在のコンテキストで利用するパーサーを変更する.
     * @param parser 現在のコンテキストで利用するパーサー
     */
    public static void setParser(Parser parser) {
        initializedCheck();
        instance.system.setParser(parser);
    }
    

}
