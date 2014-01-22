/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.contexts.base;

import info.rosetto.models.base.values.RosettoValue;

/**
 * Rosettoの実行中の状態全体を保持するコンテキスト.<br>
 * RosettoPlayerが再生にあたって必要とする情報は全てこのクラスのシングルトンインスタンスから取得される.<br>
 * このクラスのinitializeメソッドが呼び出されることでRosettoが動作を開始する.
 * @author tohhy
 */
public class Contexts {
    /**
     * シングルトンインスタンス.
     */
    private static final Contexts instance = new Contexts();
    
    /**
     * 全ての名前空間.
     */
    private WholeSpace wholeSpace;

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
        
        instance.isInitialized = true;
    }
    
    /**
     * Contextsを破棄して使用不可能な状態にする.
     */
    public static void dispose() {
        instance.wholeSpace = null;
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
     * 現在アクティブな名前空間から指定した変数に保存されている値を取得する.
     * @param key 値を取得する変数名
     * @return 取得した値、変数が存在しなければnull
     */
    public static RosettoValue get(String key) {
        return instance.wholeSpace.getCurrentNameSpace().get(key);
    }
    
    /**
     * 現在アクティブな名前空間の指定した変数に指定した値を設定する.
     * @param key 値を設定する変数名
     * @param value 設定する値
     */
    public static void put(String key, RosettoValue value) {
        instance.wholeSpace.getCurrentNameSpace().put(key, value);
    }
    
    /**
     * このContextが保持する名前空間全体のインスタンスを取得する.
     * @return このContextが保持する名前空間全体のインスタンス
     */
    public static WholeSpace getWholeSpace() {
        return instance.wholeSpace;
    }
    
    /**
     * このContextが保持する名前空間全体のインスタンスを指定したインスタンスに入れ替える.<br>
     * ゲーム中の状態を示す変数は全てWholeSpaceから取り出されるため、
     * シリアライズしたWholeSpaceをこのメソッドでセットするとその時点のゲーム状態をロードできる.
     * @param wholeSpace 新しく指定する名前空間全体のインスタンス
     */
    public static void setWholeSpace(WholeSpace wholeSpace) {
        if(wholeSpace == null)
            throw new IllegalArgumentException("wholespace must not be null");
        instance.wholeSpace = wholeSpace;
    }
    
    /**
     * 現在アクティブな名前空間を取得する.
     * @return 現在アクティブな名前空間
     */
    public static NameSpace getCurrentNameSpace() {
        return instance.wholeSpace.getCurrentNameSpace();
    }

    /**
     * 指定名の名前空間を新しくアクティブにする.
     * @param name 新しくアクティブにする名前空間の名称
     */
    public static void setNameSpaceAsCurrent(String name) {
        instance.wholeSpace.setNameSpaceAsCurrent(name);
    }
}
