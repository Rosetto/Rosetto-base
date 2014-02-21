/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.models.base.elements;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.values.ListValue;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.state.parser.Parser;
import info.rosetto.models.state.variables.Scope;
import info.rosetto.utils.base.TextUtils;
import info.rosetto.utils.base.Values;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.concurrent.Immutable;

/**
 * RosettoValueの連続を標準引数とキーワード引数に分けて順に格納する.<br>
 * イミュータブル.
 * @author tohhy
 */
@Immutable
public class MixedStore implements Serializable {
    private static final long serialVersionUID = 7674155534111041469L;
    
    /**
     * 通常引数のリスト.
     */
    private final List<RosettoValue> list;
    
    /**
     * キーワード引数のマップ.<br>
     * キーをソートするのでTreeMapが必須.
     */
    private final TreeMap<String, RosettoValue> map;
    
    /**
     * 空の引数リスト.
     */
    public static final MixedStore EMPTY = new MixedStore(
            new ArrayList<RosettoValue>(), new TreeMap<String, RosettoValue>());
    
    /**
     * 文字列形式の要素の連続を受け取ってHashedListオブジェクトを生成する.
     * @param args 文字列形式の引数リスト
     */
    public static MixedStore createFromString(String args) {
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
        return new MixedStore(list, map);
    }
    
    public static MixedStore createFromString(String[] args) {
        List<RosettoValue> list = new ArrayList<RosettoValue>();
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
        return new MixedStore(list, map);
    }
    
    public static MixedStore createFromString(List<String> elements) {
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
        return new MixedStore(list, map);
    }
    
    public static MixedStore createFromValue(List<RosettoValue> values) {
        List<RosettoValue> list = new ArrayList<RosettoValue>(values);
        Map<String, RosettoValue> map = new HashMap<String, RosettoValue>();
        return new MixedStore(list, map);
    }
    
    private MixedStore(List<RosettoValue> list, Map<String, RosettoValue> map) {
        this.list = list;
        this.map = (map instanceof TreeMap) ? 
                (TreeMap<String, RosettoValue>)map : new TreeMap<String, RosettoValue>(map);
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
    
    /**
     * 要素中に含まれるすべてのActionCallを評価し、新しいインスタンスにまとめて返す.
     * @return
     */
    public MixedStore evaluate(Scope parentScope) {
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
        return new MixedStore(list, map);
    }
    
    /**
     * 引数リストの文字列表現とキーワード引数マップの文字列表現を結合して返す.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int listLen = list.size();
        int mapLen = map.size();
        for(int i=0; i<listLen; i++) {
            sb.append(list.get(i).asString());
            if(i == listLen-1) {
                if(mapLen > 0) sb.append(" ");
            } else {
                sb.append(" ");
            }
        }
        Entry<String, RosettoValue> e = map.firstEntry();
        for(int i=0; i<mapLen; i++) {
            sb.append(e.getKey()).append("=").append(e.getValue());
            if(i != mapLen-1) {
                sb.append(" ");
                e = map.higherEntry(e.getKey());
            }
        }
        return sb.toString();
    }
    
    /**
     * キーワード引数が指定キーを含んでいるかを返す.
     */
    public boolean containsKey(String key) {
        return getMap().containsKey(key);
    }

    /**
     * この属性リストに含まれる属性の数を返す.
     */
    public int getSize() {
        return getMap().size() + getList().size();
    }

    /**
     * 通常引数中の指定インデックスに存在する値を取得する.
     */
    public RosettoValue get(int argNum) {
        return getList().get(argNum);
    }
    
    /**
     * キーワード引数中の指定キーに関連づけられた値を取得する.
     */
    public RosettoValue get(String key) {
        return getMap().get(key);
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
    
    public List<RosettoValue> getList() {
        return list;
    }
    
    public Map<String, RosettoValue> getMap() {
        return map;
    }
    
    public boolean hasMappedValue() {
        return !map.isEmpty();
    }
}
