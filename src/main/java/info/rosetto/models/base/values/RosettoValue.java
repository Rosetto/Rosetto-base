/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.values;

import info.rosetto.system.exceptions.NotConvertibleException;

import java.io.Serializable;

/**
 * Rosetto中のあらゆる値を表現するインタフェース.<br>
 * String, boolean, int, doubleのそれぞれのプリミティブな型への解釈手続きを定義する.<br>
 * 実装クラスはSerializableであることが必要.<br>
 * <br>
 * RosettoScriptの記法を実現する都合上、引数には型の情報を持たせることができない.<br>
 * そのため、ユーザが入力したあらゆるスクリプトからの引数は全てRosettoValueを継承したStringValueとして渡ってくる.<br>
 * ある種の関数の実装に際しては、そうした文字列状態の引数を数値や真偽値として解釈する必要があるが、
 * その手続きを一貫して行うのがRosettoValueに定義された各種の変換メソッド.<br>
 * <br>
 * 一方で、関数の返り値については、元々数値を返したい関数であるのに
 * わざわざユーザ入力の形式に合わせて文字列に変換するのは不要なオーバーヘッドになる.<br>
 * このインタフェースを実装したIntValueやDoubleValue等を用いて値を返すことで不要な処理を防げる上、
 * 各関数の実装でRosettoValueのインタフェースを使って演算している限りは
 * 大方の関数実装においてRosettoValueの実体が何であるかは気にする必要がななくなる.
 * <br>
 * 特定の型の実体を持った引数に限って受け取りたい関数等の実装では、getTypeで判別することができる.<br>
 * ジェネリクスではなくenumで型情報を保持しているのは、返り値の型を動的に判別するため.<br>
 * <br>
 * 引数に独自の構造を与えたい場合、RosettoValueを実装し、getTypeでOBJECTを返すようなクラスを作成するようにする.
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
