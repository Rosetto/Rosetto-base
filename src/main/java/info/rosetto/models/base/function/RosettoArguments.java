/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.models.base.function;

import info.rosetto.models.base.parser.ArgumentSyntax;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.utils.base.ArgumentsUtils;
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
     * 生成時に渡された文字列形式の引数リスト.
     */
    private final String stringArgs;

    /**
     * 通常引数のリスト.
     */
    private final ArrayList<String> args = new ArrayList<String>();
    
    /**
     * キーワード引数のマップ.
     */
    private final TreeMap<String, String> kwargs = new TreeMap<String, String>();
    
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
        this.stringArgs = createStringArgs(this.args, this.kwargs);
    }
    
    /**
     * 文字列形式の引数リストを受け取って引数リストオブジェクトを生成する.
     * @param args 文字列形式の引数リスト
     */
    public RosettoArguments(String args) {
        if(args == null) throw new IllegalArgumentException("引数がnullです");
        //文字列形式の引数をキャッシュする
        this.stringArgs = args;
        //スペース区切りのキーワード引数を解釈してリストとマップに格納する
        //ダブルクオートで囲まれた値の中にスペースが含まれる場合があるため考慮する
        String[] splited = ArgumentsUtils.splitStringArgs(args);
        for(String str : splited) {
            int equalPosition = str.indexOf("=");
            if(equalPosition == -1) {
                this.args.add(TextUtils.removeDoubleQuote(str));
            } else {
                String key = str.substring(0, equalPosition);
                String value = TextUtils.removeDoubleQuote(str.substring(equalPosition + 1));
                this.kwargs.put(key, value);
            }
        }
    }
    
    public RosettoArguments(String[] args) {
        //文字列形式の引数をキャッシュする
        StringBuilder sb = new StringBuilder();
        for(String s:args) {sb.append(s).append(" ");}
        this.stringArgs = sb.toString();
        for(String str : args) {
            int equalPosition = str.indexOf("=");
            if(equalPosition == -1) {
                this.args.add(TextUtils.removeDoubleQuote(str));
            } else {
                String key = str.substring(0, equalPosition);
                String value = TextUtils.removeDoubleQuote(str.substring(equalPosition + 1));
                this.kwargs.put(key, value);
            }
        }
    }
    
    
    /**
     * 指定した関数を用いてこの引数リストをパースする.
     * キーワードが指定されていない通常引数は指定関数の引数順等を考慮して指定関数に合わせてマッピングされる.
     * 結果は引数名と値のマップに格納されて返される.
     * RosettoFunction中のRuntimeArgumentsから呼ばれる.
     * @param func パースに用いる関数
     * @return 引数名と値のマップ
     */
    public Map<String, RosettoValue> parse(RosettoFunction func) {
        if(func == null) throw new IllegalArgumentException("渡された関数がnullです");
        
        Map<String, RosettoValue> result = new HashMap<String, RosettoValue>();
        LinkedList<String> funcArgs = new LinkedList<String>();
        int requiredArgsCount = 0;
        for(String s : func.getArguments()) {
            int eqIndex = s.indexOf("=");
            if(eqIndex < 0) {
                //名前のみの場合は引数リストに追加
                funcArgs.add(s);
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
        //キーワード引数を先に処理
        for(Entry<String, String> e : kwargs.entrySet()) {
            //もしEntryのキーがfuncArgsの値と一致するなら該当のfuncArgを消去
            boolean removed = funcArgs.remove(e.getKey());
            //一致していればカウントを減算
            if(removed)
                requiredArgsCount--;
            //結果にキーワード引数を追加
            result.put(e.getKey(), ArgumentsUtils.parseArg(e.getValue()));
        }
        
        //非キーワード引数を処理
        String mutableArg = searchMutableArg(args);
        if(mutableArg != null) {
            //もし可変長引数が含まれていれば
            if(requiredArgsCount > args.size() - 1) {
                throw new IllegalArgumentException("関数に必要な引数を満たせません: " + 
                            args.toString() + "|" + func.getArguments());
            }
            
            for(String s : args) {
                //可変長引数自体は無視
                if(s.equals(mutableArg)) continue;
                
                //残った非キーワード引数のみを順に結合してマップへ追加
                String farg = funcArgs.pollFirst();
                result.put(farg, ArgumentsUtils.parseArg(s));
            }
            
            int mvarCount = 0;
            while(!funcArgs.isEmpty()) {
                String farg = funcArgs.pollFirst();
                //可変分は mutableArg-n という引数名で渡す
                result.put(farg, Values.create(mutableArg + "-" + mvarCount));
                mvarCount++;
            }
            if(funcArgs.size() > 0)
                throw new IllegalArgumentException("不明な引数が余ります: " + 
                        args.toString() + "|" + func.getArguments());
        } else {
            //含まれていなければ
            if(requiredArgsCount > args.size()) {
                throw new IllegalArgumentException("関数に必要な引数を満たせません: " + 
                        args.toString() + "|" + func.getArguments());
            } else if(funcArgs.size() < args.size()) {
                throw new IllegalArgumentException("不明な引数が余ります: " + 
                        args.toString() + "|" + func.getArguments());
            }
            for(String s : args) {
                //残った非キーワード引数を順に結合してマップへ追加
                result.put(funcArgs.pollFirst(), ArgumentsUtils.parseArg(s));
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
     * マクロの実引数によって展開可能なキーを持っているかを返す.
     * @param syntax マクロ引数の文法定義
     * @return 展開可能なキーを持っているか
     */
    public boolean containsExpandableKey(ArgumentSyntax syntax) {
        //全展開引数があればtrue
        if(args.contains(syntax.getMacroAllExpandArg())) return true;
        
        //接頭詞付きの引数が含まれていればtrue
        for(Entry<String, String> e: kwargs.entrySet()) {
            if(e.getValue().matches(syntax.getExpandArgRegex())) return true;
        }
        for(String s: args) {
            if(s.matches(syntax.getExpandArgRegex())) return true;
        }
        
        //いずれもなければfalse
        return false;
    }

    /**
     * 生成時に与えたスペース区切りのタグ内引数を取得する.
     * @return 生成時に与えたスペース区切りのタグ内引数
     */
    public String getStringArgs() {
        return stringArgs;
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
    public String get(int argNum) {
        return args.get(argNum);
    }

    /**
     * キーワード引数中の指定キーに関連づけられた値を取得する.
     */
    public String get(String key) {
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

    private static String createStringArgs(ArrayList<String> args, 
            TreeMap<String, String> kwargs) {
        StringBuilder builder = new StringBuilder();
        for(String s : args) {
            builder.append(s).append(" ");
        }
        for(Entry<String, String> e : kwargs.entrySet()) {
            builder.append(e.getKey()).append("=").append(e.getValue()).append(" ");
        }
        
        return builder.toString().trim();
    }
    
}
