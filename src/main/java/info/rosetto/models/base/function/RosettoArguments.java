/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.models.base.function;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.values.ActionCall;
import info.rosetto.models.base.values.ListValue;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.models.base.values.ValueType;
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
 * RosettoActionの引数リスト.
 * String形式で渡された引数を標準引数とキーワード引数に分けて順に格納する.
 * イミュータブル.
 * @author tohhy
 */
@Immutable
public class RosettoArguments implements Serializable {
    private static final long serialVersionUID = 7674155534111041469L;
    
    /**
     * 通常引数のリスト.
     */
    private final ArrayList<RosettoValue> args = new ArrayList<RosettoValue>();
    
    /**
     * キーワード引数のマップ.
     */
    private final TreeMap<String, RosettoValue> kwargs = new TreeMap<String, RosettoValue>();
    
    /**
     * 空の引数リスト.
     */
    public static final RosettoArguments EMPTY = new RosettoArguments("");
    
    /**
     * 引数のリストを受け取って引数リストオブジェクトを生成する.
     * @param args 引数のリスト
     */
    public RosettoArguments(List<RosettoArgument> args) {
        if(args == null) throw new IllegalArgumentException("引数がnullです");
        initializeArgs(args);
    }
    
    /**
     * 文字列形式の引数リストを受け取って引数リストオブジェクトを生成する.
     * @param args 文字列形式の引数リスト
     */
    public RosettoArguments(String args) {
        if(args == null) throw new IllegalArgumentException("引数がnullです");
        //スペース区切りのキーワード引数を解釈してリストとマップに格納する
        //ダブルクオートで囲まれた値の中にスペースが含まれる場合があるため考慮する
        List<String> splited = Contexts.getParser().splitElements(args);
        Parser parser = Contexts.getParser();
        for(String str : splited) {
            int equalPosition = str.indexOf("=");
            if(equalPosition == -1) {
                this.args.add(parser.parseElement(TextUtils.removeDoubleQuote(str)));
            } else {
                String key = str.substring(0, equalPosition);
                String value = TextUtils.removeDoubleQuote(str.substring(equalPosition + 1));
                this.kwargs.put(key, parser.parseElement(value));
            }
        }
    }
    
    public RosettoArguments(String[] args) {
        //文字列形式の引数をキャッシュする
        StringBuilder sb = new StringBuilder();
        for(String s:args) {sb.append(s).append(" ");}
        for(String str : args) {
            int equalPosition = str.indexOf("=");
            if(equalPosition == -1) {
                this.args.add(Values.create(TextUtils.removeDoubleQuote(str)));
            } else {
                String key = str.substring(0, equalPosition);
                String value = TextUtils.removeDoubleQuote(str.substring(equalPosition + 1));
                this.kwargs.put(key, Values.create(value));
            }
        }
    }
    
    
    /**
     * 指定した関数とスコープを用いてこの引数リストをパースする.
     * キーワードが指定されていない通常引数は指定関数の引数順等を考慮して指定関数に合わせてマッピングされる.
     * 結果は引数名と値のマップに格納されて返される.
     * すべてのActionCallは評価される.
     * @param func パースに用いる関数
     * @return 引数名と値のマップ
     */
    public Map<String, RosettoValue> parse(RosettoFunction func, Scope currentScope) {
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
        for(Entry<String, RosettoValue> e : kwargs.entrySet()) {
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
            if(requiredArgsCount - 1 > args.size()) {
                throw new IllegalArgumentException("関数に必要な引数を満たせません: " + 
                            args.toString() + "|" + func.getArguments());
            }
            
            //引数に余りがあれば、それを可変長引数として追加していく
            //可変長引数のリスト
            List<RosettoValue> margs = new LinkedList<RosettoValue>();
            for(int i=0; i<args.size(); i++) {
                RosettoValue v = args.get(i);
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
            if(requiredArgsCount > args.size()) {
                throw new IllegalArgumentException("関数に必要な引数を満たせません: " + 
                        args.toString() + "|" + func.getArguments());
            } else if(funcArgs.size() < args.size()) {
                throw new IllegalArgumentException("不明な引数が余ります: " + 
                        args.toString() + "|" + func.getArguments());
            }
            for(RosettoValue value : args) {
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
     * 引数リストの文字列表現とキーワード引数マップの文字列表現を結合して返す.
     */
    @Override
    public String toString() {
        return args.toString() + kwargs.toString();
    }
    
    /**
     * キーワード引数が指定キーを含んでいるかを返す.
     */
    public boolean containsKey(String key) {
        return kwargs.containsKey(key);
    }

    /**
     * この属性リストに含まれる属性の数を返す.
     */
    public int getSize() {
        return kwargs.size() + args.size();
    }

    /**
     * 通常引数中の指定インデックスに存在する値を取得する.
     */
    public RosettoValue get(int argNum) {
        return args.get(argNum);
    }

    /**
     * キーワード引数中の指定キーに関連づけられた値を取得する.
     */
    public RosettoValue get(String key) {
        return kwargs.get(key);
    }

    private void initializeArgs(List<RosettoArgument> args) {
        for(RosettoArgument a : args) {
            if(a.getKey() == null) {
                this.args.add(a.getValue());
            } else {
                this.kwargs.put(a.getKey(), a.getValue());
            }
        }
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
}
