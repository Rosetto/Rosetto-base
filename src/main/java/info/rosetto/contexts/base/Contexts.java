/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.contexts.base;

import info.rosetto.functions.base.BaseFunctions;
import info.rosetto.models.base.function.RosettoFunction;
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
     * 全ての名前空間を保持するインスタンス.
     */
    private WholeSpace wholeSpace;
    
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
        
        instance.wholeSpace = new WholeSpace();
        instance.system = new SystemContext();
        instance.isInitialized = true;
    }
    
    /**
     * Contextsを破棄して使用不可能な状態にする.
     */
    public static void dispose() {
        instance.wholeSpace = null;
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
     * 現在アクティブな名前空間から指定した変数に保存されている値を取得する.
     * @param key 値を取得する変数名
     * @return 取得した値、変数が存在しなければnull
     */
    public static RosettoValue get(String key) {
        initializedCheck();
        if(key == null) return Values.NULL;
        return instance.wholeSpace.getCurrentNameSpace().get(key);
    }
    
    /**
     * 現在アクティブな名前空間から指定した変数に保存されている値を取得する.
     * @param key 値を取得する変数名
     * @return 取得した値、変数が存在しなければnull
     */
    public static RosettoValue getAbsolute(String key) {
        initializedCheck();
        if(key == null) return Values.NULL;
        int lastDotIndex = key.lastIndexOf(".");
        if(lastDotIndex > 0) {
            return instance.wholeSpace.getNameSpace(key.substring(0, lastDotIndex))
                    .get(key.substring(lastDotIndex+1));
        } else {
            return instance.wholeSpace.getCurrentNameSpace().get(key);
        }
    }
    
    /**
     * 現在アクティブな名前空間から指定した変数に保存されている値を探し、それが関数であれば取得する.<br>
     * 値が関数でない場合、存在しない場合はBaseFunctions.passが返る.
     * @param key 値を取得する変数名
     * @return 取得した関数. 値が関数でないか、変数が存在しなければBaseFunctions.pass
     * TODO マクロへの対応
     */
    public static RosettoFunction getAsFunction(String key) {
        RosettoValue v = get(key);
        return (v instanceof RosettoFunction) ? (RosettoFunction)v : BaseFunctions.pass;
    }
    
    /**
     * 現在アクティブな名前空間の指定した変数に指定した値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void set(String key, RosettoValue value) {
        initializedCheck();
        if(key == null || key.length() == 0)
            throw new IllegalArgumentException("key must not be empty");
        if(key.endsWith("."))
            throw new IllegalArgumentException("name must not end with dot");
        int lastDotIndex = key.lastIndexOf(".");
        if(lastDotIndex > 0) {
            String packageName = key.substring(0, lastDotIndex);
            NameSpace ns = instance.wholeSpace.getNameSpace(packageName);
            ns.set(key.substring(lastDotIndex+1), value);
        } else {
            instance.wholeSpace.getCurrentNameSpace().set(key, value);
        }
    }
    
    /**
     * 現在アクティブな名前空間の指定した変数に指定した値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void set(String key, boolean value) {
        set(key, Values.create(value));
    }
    
    /**
     * 現在アクティブな名前空間の指定した変数に指定した値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void set(String key, int value) {
        set(key, Values.create(value));
    }
    
    /**
     * 現在アクティブな名前空間の指定した変数に指定した値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void set(String key, long value) {
        set(key, Values.create(value));
    }
    
    /**
     * 現在アクティブな名前空間の指定した変数に指定した値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void set(String key, double value) {
        set(key, Values.create(value));
    }
    
    /**
     * 現在アクティブな名前空間の指定した変数に指定した値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void set(String key, String value) {
        set(key, Values.create(value));
    }
    
    /**
     * 指定名の名前空間の全変数を現在の名前空間から参照可能にする.
     * @param nameSpace 現在の名前空間から参照可能にする名前空間
     */
    public static void refer(String nameSpace, String referName) {
        instance.wholeSpace.refer(nameSpace, referName);
    }
    
    /**
     * 指定名の名前空間の全変数を現在の名前空間に読み込む.
     * @param nameSpace 現在の名前空間に変数を読み込む名前空間
     */
    public static void include(String nameSpace) {
        getCurrentNameSpace().include(getNameSpace(nameSpace));
    }
    
    /**
     * このContextが保持する名前空間全体のインスタンスを取得する.
     * @return このContextが保持する名前空間全体のインスタンス
     */
    public static WholeSpace getWholeSpace() {
        initializedCheck();
        return instance.wholeSpace;
    }
    
    /**
     * このContextが保持する名前空間全体のインスタンスを指定したインスタンスに入れ替える.<br>
     * ゲーム中の状態を示す変数は全てWholeSpaceから取り出されるため、
     * シリアライズしたWholeSpaceをこのメソッドでセットするとその時点のゲーム状態をロードできる.
     * @param wholeSpace 新しく指定する名前空間全体のインスタンス
     */
    public static void setWholeSpace(WholeSpace wholeSpace) {
        initializedCheck();
        if(wholeSpace == null)
            throw new IllegalArgumentException("wholespace must not be null");
        instance.wholeSpace = wholeSpace;
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
     * 指定名の名前空間を取得する.<br>
     * コンテキスト中に指定名の名前空間がまだ存在しない場合は生成して返す.
     * @param name 取得する名前空間
     * @return 取得した名前空間
     */
    public static NameSpace getNameSpace(String name) {
        return instance.wholeSpace.getNameSpace(name);
    }
    
    /**
     * 現在アクティブな名前空間を取得する.
     * @return 現在アクティブな名前空間
     */
    public static NameSpace getCurrentNameSpace() {
        initializedCheck();
        return instance.wholeSpace.getCurrentNameSpace();
    }
    
    /**
     * 指定名の名前空間を新しくアクティブにする.
     * @param name 新しくアクティブにする名前空間の名称
     */
    public static void setCurrentNameSpace(String name) {
        initializedCheck();
        instance.wholeSpace.setCurrentNameSpace(name);
    }
}
