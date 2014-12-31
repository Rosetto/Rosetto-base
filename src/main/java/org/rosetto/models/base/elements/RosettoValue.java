/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.rosetto.models.base.elements;

import java.io.Serializable;

import org.rosetto.models.system.Scope;
import org.rosetto.system.exceptions.NotConvertibleException;

/**
 * Rosetto中のあらゆる値を表現するインタフェース.<br>
 * String, boolean, int, doubleのそれぞれのプリミティブな型への解釈手続きと、
 * clojureのsequenceと同様のインタフェースを定義する.<br>
 * あるRosettoValueに変換される文字列と、変換されたRosettoValueのasString()の結果は常に文字列上で一致する.<br>
 * 実装クラスはSerializableであることが必要.<br>
 * <br>
 * ユーザが入力したあらゆるスクリプトからの引数は全てRosettoValueとして渡ってくる.<br>
 * ある種の関数の実装に際しては、そうした文字列状態の引数を数値や真偽値として解釈する必要があるが、
 * その手続きを一貫して行うのがRosettoValueに定義された各種の変換メソッド.<br>
 * 各関数の実装でRosettoValueのインタフェースを使って演算している限りは
 * 大方の関数実装においてRosettoValueの実体が何であるかは気にする必要がなくなる.
 * <br>
 * 特定の型の実体を持った引数に限って受け取りたい関数等の実装では、getTypeで判別することができる.<br>
 * ジェネリクスではなくenumで型情報を保持しているのは、返り値の型を動的かつ高速に判別するため.<br>
 * 実装においてはinstanceofではなくenumの同値で型を判別するようにする.
 * @author tohhy
 */
public interface RosettoValue extends Serializable {
    
    /**
     * このRosettoValueの実体の型種別を返す.
     * @return このRosettoValueの実体の型種別
     */
    public ValueType getType();
    
    /**
     * このRosettoValueの実体となる値を返す.<br>
     * getType()に応じた型を持つJavaクラスのインスタンスが返る.
     * @return このRosettoValueの実体となる値
     */
    public Object getValue();
    
    /**
     * この値が評価可能な値であれば評価結果を返す.<br>
     * そうでなければ自身をそのまま返す.
     * @param scope 評価に利用するローカル変数をもつスコープ
     * @return 評価結果
     */
    public RosettoValue evaluate(Scope scope);
    
    /**
     * この値の最初の要素を返す.<br>
     * この値が単一の要素からなる場合は自身をそのまま返す.<br>
     * この値が要素を持たない場合はValues.NULLを返す.
     * @return この値の最初の要素
     */
    public RosettoValue first();
    
    /**
     * この値の最初の要素を除いた残りの要素を返す.<br>
     * この値が単一の要素からなる場合、この値が要素を持たない場合はValues.NULLを返す.
     * @return この値の最初の要素を除いた残りの要素
     */
    public RosettoValue rest();
    
    /**
     * 引数に与えた値を先頭とし、その後にこの値を付け加えた新しいコレクションを返す.
     * @param head 先頭に結合する値
     * @return 結合後の値
     */
    public RosettoValue cons(RosettoValue head);
    
    /**
     * この値をリストとしてイテレーションしたとき、指定インデックスに存在する値を取得する.<br>
     * 指定インデックスに値が存在しない場合はValues.NULLが返る.
     * @param index 取得する値のインデックス
     * @return 取得した値
     */
    public RosettoValue getAt(int index);
    
    /**
     * この値をリストとしてイテレーションする場合に要素数がいくつになるかを取得する.
     * @return リストの要素数
     */
    public int size();
    
    /**
     * この値を文字列として解釈した場合の値を返す.<br>
     * 文字列として解釈できなかった場合、NotConvertibleExceptionをスローする.
     * @return この値を文字列として解釈した場合の値
     */
    public String asString() throws NotConvertibleException;
    
    /**
     * この値を文字列として解釈した場合の値を返す.<br>
     * 文字列として解釈できなかった場合、defaultValueを返す.
     * @param defaultValue デフォルト値
     * @return この値を文字列として解釈した場合の値
     */
    public String asString(String defaultValue);
    
    /**
     * この値を真偽値として解釈した場合の値を返す.<br>
     * 真偽値として解釈できなかった場合、NotConvertibleExceptionをスローする.
     * @return この値を真偽値として解釈した場合の値
     * @throws NotConvertibleException
     */
    public boolean asBool() throws NotConvertibleException;

    /**
     * この値を真偽値として解釈した場合の値を返す.<br>
     * 真偽値として解釈できなかった場合、defaultValueを返す.
     * @param defaultValue デフォルト値
     * @return この値を真偽値として解釈した場合の値
     */
    public boolean asBool(boolean defaultValue);
    
    /**
     * この値をint値として解釈した場合の値を返す.<br>
     * int値として解釈できなかった場合、NotConvertibleExceptionをスローする.
     * @return この値をint値として解釈した場合の値
     * @throws NotConvertibleException
     */
    public int asInt() throws NotConvertibleException;
    
    /**
     * この値をint値として解釈した場合の値を返す.<br>
     * int値として解釈できなかった場合、defaultValueを返す.
     * @param defaultValue デフォルト値
     * @return この値をint値として解釈した場合の値
     */
    public int asInt(int defaultValue);
    
    /**
     * この値をlong値として解釈した場合の値を返す.<br>
     * long値として解釈できなかった場合、NotConvertibleExceptionをスローする.
     * @return この値をlong値として解釈した場合の値
     * @throws NotConvertibleException
     */
    public long asLong() throws NotConvertibleException;
    
    /**
     * この値をlong値として解釈した場合の値を返す.<br>
     * long値として解釈できなかった場合、defaultValueを返す.
     * @param defaultValue デフォルト値
     * @return この値をlong値として解釈した場合の値
     */
    public long asLong(long defaultValue);
    
    /**
     * この値をdouble値として解釈した場合の値を返す.<br>
     * double値として解釈できなかった場合、NotConvertibleExceptionをスローする.
     * @return この値をdouble値として解釈した場合の値
     * @throws NotConvertibleException
     */
    public double asDouble() throws NotConvertibleException;
    
    /**
     * この値をdouble値として解釈した場合の値を返す.<br>
     * double値として解釈できなかった場合、defaultValueを返す.
     * @param defaultValue デフォルト値
     * @return この値をdouble値として解釈した場合の値
     */
    public double asDouble(double defaultValue);
    
}
