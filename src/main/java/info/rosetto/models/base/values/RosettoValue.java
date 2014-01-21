package info.rosetto.models.base.values;

import info.rosetto.exceptions.NotConvertibleException;

import java.io.Serializable;

/**
 * Rosetto中のあらゆる値を表現するインタフェース.<br>
 * String, boolean, int, doubleのそれぞれのプリミティブな型への解釈手続きを定義する.<br>
 * <br>
 * RosettoScriptの記法を実現する都合上、引数には型の情報を持たせることができない.<br>
 * そのため、ユーザが入力したあらゆるスクリプトからの引数は全てRosettoValueを継承したStringValueとして渡ってくる.<br>
 * 関数の実装に際しては、そうした文字列状態の引数を数値や真偽値として解釈する必要があるが、
 * その手続きを一貫して行うのがRosettoValueに定義された各種の変換メソッド.<br>
 * 一方で、関数の返り値については、数値を返したい関数であるのに
 * わざわざユーザ入力の形式に合わせて文字列に変換するのは不要なオーバーヘッドになる.<br>
 * その点、このインタフェースに則って変換している限りは
 * 大方のメソッド実装においてRosettoValueの実体が何であるかは気にする必要がない.<br>
 * そのため、単純にIntValueでラップして返すことができる.<br>
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
     * 
     * @return
     */
    public Object getValue();
    
    /**
     * この値を文字列として解釈した場合の値を返す.
     * @return
     */
    public String toString();
    
    public boolean toBool(boolean defaultValue);
    
    public boolean toBool() throws NotConvertibleException;
    
    public int toInt(int defaultValue);
    
    public int toInt() throws NotConvertibleException;
    
    public double toDouble(double defaultValue);
    
    public double toDouble() throws NotConvertibleException;
    
}
