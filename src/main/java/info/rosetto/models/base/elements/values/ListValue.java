/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.models.base.elements.values;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.system.Parser;
import info.rosetto.models.system.Scope;
import info.rosetto.system.exceptions.NotConvertibleException;
import info.rosetto.utils.base.TextUtils;
import info.rosetto.utils.base.Values;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

/**
 * リストを拡張し、キーと値の組を格納できるようにしたもの.<br>
 * 以下のようにごちゃ混ぜに値を放り込むことができる.<br>
 * <br>
 * (foo=10 bar=100 2 6 a=1 9 3)<br>
 * <br>
 * Rosetto中では上のリストは以下のように整理される.<br>
 * <br>
 * Map {foo=10, bar=100, a=1}<br>
 * List (2,6,9,3)<br>
 * <br>
 * 先にキーワードを持つものが抽出されてマップとして保持され、その後に残りの要素がリストとして保持される.<br>
 * シーケンス操作はすべてリスト部分のみに適用される.<br>
 * [size (a=1 b=2 c=3)] => 0
 * @author tohhy
 */
public class ListValue implements RosettoValue {
    private static final long serialVersionUID = -5778537199758610111L;
    
    public static final ListValue EMPTY = 
            new ListValue(new LinkedList<RosettoValue>(), 
                                new TreeMap<String, RosettoValue>());
    
    /**
     * 通常引数のリスト.<br>
     * Lispライクな処理を多用するのでLinkedListが必須.
     */
    private final LinkedList<RosettoValue> list;
    
    /**
     * キーワード引数のマップ.<br>
     * 並び順を一意にするためにTreeMapが必須.
     */
    private final TreeMap<String, RosettoValue> map;
    

    /**
     * 文字列形式の要素の連続を受け取ってHashedListオブジェクトを生成する.
     * @param args 文字列形式の引数リスト
     */
    public static ListValue createFromString(String args) {
        if(args == null) throw new IllegalArgumentException("引数がnullです");
        List<RosettoValue> list = new ArrayList<RosettoValue>();
        Map<String, RosettoValue> map = new HashMap<String, RosettoValue>();
        //スペース区切りのキーワード引数を解釈してリストとマップに格納する
        //ダブルクオートで囲まれた値の中にスペースが含まれる場合があるため考慮する
        List<String> splited = Contexts.getParser().splitElements(args);
        Parser parser = Contexts.getParser();
        for(String str : splited) {
            int equalPosition = str.indexOf("=");
            if(equalPosition == -1) {
                list.add(parser.parseElement(TextUtils.removeDoubleQuote(str)));
            } else {
                String key = str.substring(0, equalPosition);
                String value = TextUtils.removeDoubleQuote(str.substring(equalPosition + 1));
                map.put(key, parser.parseElement(value));
            }
        }
        return new ListValue(list, map);
    }
    
    public static ListValue createFromString(String[] args) {
        List<RosettoValue> list = new LinkedList<RosettoValue>();
        Map<String, RosettoValue> map = new HashMap<String, RosettoValue>();
        for(String str : args) {
            int equalPosition = str.indexOf("=");
            if(equalPosition == -1) {
                list.add(Values.create(TextUtils.removeDoubleQuote(str)));
            } else {
                String key = str.substring(0, equalPosition);
                String value = TextUtils.removeDoubleQuote(str.substring(equalPosition + 1));
                map.put(key, Values.create(value));
            }
        }
        return new ListValue(list, map);
    }
    
    public static ListValue createFromString(List<String> elements) {
        List<RosettoValue> list = new ArrayList<RosettoValue>();
        Map<String, RosettoValue> map = new HashMap<String, RosettoValue>();
        for(String element : elements) {
            int equalPosition = element.indexOf("=");
            if(equalPosition == -1) {
                list.add(Values.create(TextUtils.removeDoubleQuote(element)));
            } else {
                String key = element.substring(0, equalPosition);
                String value = TextUtils.removeDoubleQuote(element.substring(equalPosition + 1));
                map.put(key, Values.create(value));
            }
        }
        return new ListValue(list, map);
    }
    
    public static ListValue createFromValue(
            List<RosettoValue> list, Map<String, RosettoValue> map) {
        return new ListValue(
                new LinkedList<RosettoValue>(list), 
                new HashMap<String, RosettoValue>(map));
    }
    
    public static ListValue createFromValue(ListValue list) {
        return new ListValue(
                new LinkedList<RosettoValue>(list.getList()), 
                new HashMap<String, RosettoValue>(list.getMap()));
    }
    
    private ListValue(List<RosettoValue> list, Map<String, RosettoValue> map) {
        this.list = (list instanceof LinkedList) ? 
                (LinkedList<RosettoValue>) list : new LinkedList<RosettoValue>(list);
        this.map = (map instanceof TreeMap) ? 
                (TreeMap<String, RosettoValue>)map : new TreeMap<String, RosettoValue>(map);
    }
    
    public ListValue(List<RosettoValue> list) {
        this((list instanceof LinkedList) ? 
                (LinkedList<RosettoValue>) list : new LinkedList<RosettoValue>(list), 
                new TreeMap<String, RosettoValue>());
    }
    
    public ListValue(RosettoValue...values) {
        this(toList(values));
        
    }
    
    public ListValue(String...values) {
        this(toList(values));
    }
    
    private static List<RosettoValue> toList(RosettoValue...values) {
        LinkedList<RosettoValue> list = new LinkedList<RosettoValue>();
        for(RosettoValue v : values) list.add(v);
        return list;
    }

    private static List<RosettoValue> toList(String...values) {
        Parser parser = Contexts.getParser();
        LinkedList<RosettoValue> list = new LinkedList<RosettoValue>();
        for(String s : values) list.add(parser.parseElement(s));
        return list;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RosettoValue) {
            return ((RosettoValue)obj).asString().equals(this.asString());
        }
        return false;
    }
    
    
    @Override
    public String toString() {
        return asString();
    }
    
    /**
     * 引数リストの文字列表現とキーワード引数マップの文字列表現を結合して返す.<br>
     * 外周の丸括弧はつけない.丸括弧も含める場合はasStringを呼び出す.
     */
    public String toArgsExpression() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.join(list, ' '));
        int mapLen = map.size();
        if(mapLen > 0) sb.append(" ");
        sb.append(StringUtils.join(map.entrySet(), ' '));
        return sb.toString();
    }
    
    @Override
    public RosettoValue evaluate(Scope scope) {
        return this;
    }
    
    /**
     * 含まれるすべての要素を評価し、新しいインスタンスにまとめて返す.
     * @return
     */
    public ListValue evaluateChildren(Scope parentScope) {
        List<RosettoValue> list = new ArrayList<RosettoValue>();
        Map<String, RosettoValue> map = new TreeMap<String, RosettoValue>();
        for(RosettoValue v:this.list) {
            if(v instanceof ActionCall) {
                list.add(((ActionCall)v).evaluate(parentScope));
            } else {
                list.add(v);
            }
        }
        for(Entry<String, RosettoValue> e : this.map.entrySet()) {
            if(e.getValue() instanceof ActionCall) {
                map.put(e.getKey(), ((ActionCall)e.getValue()).evaluate(parentScope));
            } else {
                map.put(e.getKey(), e.getValue());
            }
        }
        return new ListValue(list, map);
    }

    /**
     * 指定した関数とスコープを用いてこの引数リストをパースする.
     * キーワードが指定されていない通常引数は指定関数の引数順等を考慮して指定関数に合わせてマッピングされる.
     * 結果は引数名と値のマップに格納されて返される.
     * すべてのActionCallは評価される.
     * @param func パースに用いる関数
     * @return 引数名と値のマップ
     */
    public Map<String, RosettoValue> bind(RosettoFunction func, Scope currentScope) {
        if(func == null) throw new IllegalArgumentException("渡された関数がnullです");
        
        //結果のマップ
        Map<String, RosettoValue> result = new HashMap<String, RosettoValue>();
        //関数が持つ引数のリスト
        LinkedList<String> funcArgs = new LinkedList<String>();
        
        //関数がいくつ入力必須の引数を持っているか
        int requiredArgsCount = 0;
        
        //関数に定義されている全引数についてイテレーション
        for(String s : func.getArguments()) {
            int eqIndex = s.indexOf("=");
            if(eqIndex == -1) {
                //名前のみの場合は引数リストに追加
                funcArgs.add(s);
                //名前のみなので入力必須
                requiredArgsCount++;
            } else {
                //名前とデフォルト値
                String key = s.substring(0, eqIndex);
                RosettoValue value = Values.create(s.substring(eqIndex+1, s.length()));
                //引数リストに追加
                funcArgs.add(key);
                //結果にデフォルト値を登録してしまう
                result.put(key, value);
            }
        }
        
        //ここから入力とのバインド
        
        //キーワード引数入力を先に処理
        for(Entry<String, RosettoValue> e : getMap().entrySet()) {
            //もしEntryのキーがfuncArgsの値と一致するなら該当のfuncArgを消去
            boolean removed = funcArgs.remove(e.getKey());
            //一致していればカウントを減算
            if(removed) requiredArgsCount--;
            //結果にキーワード引数を追加
            result.put(e.getKey(), e.getValue());
        }
        
        //非キーワード引数入力を処理
        String mutableArg = searchMutableArg(funcArgs);
        if(mutableArg != null) {
            //もし可変長引数が含まれていれば
            
            //引数の要求数が入力の数よりも大きければエラー
            //可変長引数は要求数に含まないので-1
            if(requiredArgsCount - 1 > getList().size()) {
                throw new IllegalArgumentException("関数に必要な引数を満たせません: " + 
                            getList().toString() + "|" + func.getArguments());
            }
            
            //引数に余りがあれば、それを可変長引数として追加していく
            //可変長引数のリスト
            List<RosettoValue> margs = new LinkedList<RosettoValue>();
            for(int i=0; i<getList().size(); i++) {
                RosettoValue v = getList().get(i);
                if(!funcArgs.isEmpty()) {
                    //関数側で定義された引数名が空になるまでpopして取りだしていく
                    String farg = funcArgs.pollFirst();
                    //可変長引数自体は無視し、直接結びつけない
                    if(farg.equals(mutableArg)) {
                        //この時点で空である必要、そうでなければエラー
                        if(!funcArgs.isEmpty()) {
                            throw new IllegalArgumentException("mutable args must be last element");
                        }
                        //可変長引数に追加
                        margs.add(v);
                    }
                    //引数名に値を結びつけて追加
                    result.put(farg, v);
                } else {
                    //可変長引数に追加
                    margs.add(v);
                }
            }
            
            //可変長相当の引数があれば
            if(margs.size() > 0) {
                //可変長引数の名前
                String margName = mutableArg.substring(1);
                //listvalueとして追加
                result.put(margName, new ListValue(margs));
            }
            
        } else {
            //含まれていなければ
            if(requiredArgsCount > getList().size()) {
                throw new IllegalArgumentException("関数に必要な引数を満たせません: " + 
                        getList().toString() + "|" + func.getArguments());
            } else if(funcArgs.size() < getList().size()) {
                throw new IllegalArgumentException("不明な引数が余ります: " + 
                        getList().toString() + "|" + func.getArguments());
            }
            for(RosettoValue value : getList()) {
                //残った非キーワード引数を順に結合してマップへ追加
                if(value.getType() == ValueType.ACTION_CALL) {
                    //ActionCallなら評価しておく
                    result.put(funcArgs.pollFirst(), 
                            ((ActionCall)value).evaluate(currentScope));
                } else {
                    result.put(funcArgs.pollFirst(), value);
                }
            }
        }
        return result;
    }

    public boolean hasMappedValue() {
        return map.size() > 0;
    }
    
    /**
     * オプションマップが指定キーを含んでいるかを返す.
     */
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }
    
    @Override
    public RosettoValue first() {
        return list.getFirst();
    }
    
    @Override
    public RosettoValue rest() {
        if(list.size() == 0 || list.size() == 1) return Values.NULL;
        if(list.size() == 2) return list.get(1);
        return new ListValue(list.subList(1, list.size()), map);
    }
    
    @Override
    public RosettoValue cons(RosettoValue head) {
        return null;
    }
    
    /**
     * List部の指定インデックスに存在する値を取得する.
     */
    public RosettoValue getAt(int listIndex) {
        return list.get(listIndex);
    }
    
    /**
     * List部の要素数を取得する.
     */
    @Override
    public int size() {
        return list.size();
    }
    
    /**
     * オプションマップ中の指定キーに関連づけられた値を取得する.
     */
    public RosettoValue get(String mapKey) {
        return map.get(mapKey);
    }

    public int optionSize() {
        return map.size();
    }

    public List<RosettoValue> getList() {
        return Collections.unmodifiableList(list);
    }

    public Map<String, RosettoValue> getMap() {
        return Collections.unmodifiableMap(map);
    }
    
    /**
     * 引数中から可変長引数を探して返す.
     * @return 可変長引数、引数中に可変長引数がなければnull
     */
    private String searchMutableArg(List<String> args) {
        String result = null;
        for(String s : args) {
            if(s.startsWith("*") && s.length() >= 2) {
                if(result != null)
                    throw new IllegalArgumentException("multiple mutablearg found");
                result = s;
            }
        }
        return result;
    }

    @Override
    public ValueType getType() {
        return ValueType.LIST;
    }
    
    @Override
    public Object getValue() {
        return this;
    }
    
    @Override
    public String asString() throws NotConvertibleException {
        return "(" + toArgsExpression() + ")";
    }
    
    @Override
    public String asString(String defaultValue) {
        return "(" + toArgsExpression() + ")";
    }

    @Override
    public boolean asBool() throws NotConvertibleException {
        throw new NotConvertibleException();
    }

    @Override
    public boolean asBool(boolean defaultValue) {
        return defaultValue;
    }

    @Override
    public int asInt() throws NotConvertibleException {
        throw new NotConvertibleException();
    }

    @Override
    public int asInt(int defaultValue) {
        return defaultValue;
    }

    @Override
    public long asLong() throws NotConvertibleException {
        throw new NotConvertibleException();
    }

    @Override
    public long asLong(long defaultValue) {
        return defaultValue;
    }

    @Override
    public double asDouble() throws NotConvertibleException {
        throw new NotConvertibleException();
    }

    @Override
    public double asDouble(double defaultValue) {
        return defaultValue;
    }
    
    
    
    
}
