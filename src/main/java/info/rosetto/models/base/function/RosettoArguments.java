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
     * 引数リストとキーワード引数マップを直接受け取って引数リストオブジェクトを生成する.
     * @param args 文字列形式の引数リスト
     */
    private RosettoArguments(ArrayList<RosettoValue> args, TreeMap<String, RosettoValue> kwargs) {
        for(int i=0; i<args.size(); i++) {
            this.args.add(args.get(i));
        }
        for(Entry<String, RosettoValue>e : kwargs.entrySet()) {
            this.kwargs.put(e.getKey(), e.getValue());
        }
        this.stringArgs = createStringArgs(this.args, this.kwargs);
    }
    
    /**
     * 文字列形式の引数リストを受け取って引数リストオブジェクトを生成する.
     * @param args 文字列形式の引数リスト
     */
    public RosettoArguments(List<RosettoArgument> args) {
        if(args == null) throw new IllegalArgumentException("引数がnullです");
        for(RosettoArgument a : args) {
            if(a.getKey() == null) {
                this.args.add(a.getValue());
            } else {
                this.kwargs.put(a.getKey(), a.getValue());
            }
        }
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
                this.args.add(Values.create(TextUtils.removeDoubleQuote(str)));
            } else {
                String key = str.substring(0, equalPosition);
                String value = TextUtils.removeDoubleQuote(str.substring(equalPosition + 1));
                this.kwargs.put(key, Values.create(value));
            }
        }
    }
    
    /**
     * 実行時引数から逆算して引数オブジェクトを生成する.
     * @param runtimeArgs 実行時引数
     */
    public RosettoArguments(ExpandedArguments runtimeArgs, ArgumentSyntax syntax) {
        if(runtimeArgs == null) 
            throw new IllegalArgumentException("runtime args must not be null");
        StringBuilder kb = new StringBuilder();
        TreeMap<Integer, RosettoValue> margs = new TreeMap<Integer, RosettoValue>();
        TreeMap<String, RosettoValue> kwargs = new TreeMap<String, RosettoValue>();
        //可変長引数のセパレータを取得
        char sep = syntax.getMutableArgNumSeparator();
        //マップを処理する
        for(Entry<String, RosettoValue> e : runtimeArgs.getMap().entrySet()) {
            int sepIndex = e.getKey().indexOf(sep);
            if(sepIndex >= 1) {
                //可変長引数が存在すれば処理
                int key = Integer.parseInt(e.getKey().substring(sepIndex + 1));
                String value = e.getKey().substring(0, sepIndex);
                margs.put(key, Values.create(value));
            } else {
                kwargs.put(e.getKey(), e.getValue());
                kb.append(e.getKey()).append("=").append(e.getValue()).append(" ");
            }
        }
        StringBuilder argb = new StringBuilder();
        for(RosettoValue arg:margs.values()) {
            argb.append(arg).append(" ");
        }
        this.stringArgs = argb.toString() + kb.toString();
        this.args.addAll(margs.values());
        this.kwargs.putAll(kwargs);
    }
    
    /**
     * この属性リストが展開すべき引数を含んでいれば、この属性リストの複製を作成した上でそれを展開して返す.
     * 展開すべき引数がなければこの属性リスト自身をそのまま返す.
     * @param macroCallAttrs マクロ呼び出し時の属性マップ
     * @param macroArgPrefix マクロ展開引数（マクロの実行時に与えられた引数で置き換える引数）の接頭詞
     */
    public RosettoArguments createExpandedArgs(RosettoArguments contextArgs, ArgumentSyntax syntax) {
        if(containsExpandableKey(syntax)) {
            ArrayList<RosettoValue> expandArgs = new ArrayList<RosettoValue>(this.args);
            TreeMap<String, RosettoValue> expandKwargs = new TreeMap<String, RosettoValue>(this.kwargs);
            expandMacroKeys(expandArgs, expandKwargs, contextArgs, syntax);
            return new RosettoArguments(expandArgs, expandKwargs);
        }
        return this;
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
        for(Entry<String, RosettoValue> e : kwargs.entrySet()) {
            //もしEntryのキーがfuncArgsの値と一致するなら該当のfuncArgを消去
            boolean removed = funcArgs.remove(e.getKey());
            //一致していればカウントを減算
            if(removed)
                requiredArgsCount--;
            //結果にキーワード引数を追加
            result.put(e.getKey(), e.getValue());
        }
        
        //非キーワード引数を処理
        RosettoValue mutableArg = searchMutableArg(args);
        if(mutableArg != null) {
            //もし可変長引数が含まれていれば
            if(requiredArgsCount > args.size() - 1) {
                throw new IllegalArgumentException("関数に必要な引数を満たせません: " + 
                            args.toString() + "|" + func.getArguments());
            }
            
            for(RosettoValue s : args) {
                //可変長引数自体は無視
                if(s.equals(mutableArg)) continue;
                
                //残った非キーワード引数のみを順に結合してマップへ追加
                String farg = funcArgs.pollFirst();
                result.put(farg, s);
            }
            
            int mvarCount = 0;
            while(!funcArgs.isEmpty()) {
                String farg = funcArgs.pollFirst();
                //可変分は mutableArg-n という引数名で渡す
                result.put(farg, Values.create(mutableArg.asString("") + "-" + mvarCount));
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
            for(RosettoValue s : args) {
                //残った非キーワード引数を順に結合してマップへ追加
                result.put(funcArgs.pollFirst(), s);
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
        for(Entry<String, RosettoValue> e: kwargs.entrySet()) {
            if(e.getValue().asString("").matches(syntax.getExpandArgRegex())) return true;
        }
        for(RosettoValue s: args) {
            if(s.asString("").matches(syntax.getExpandArgRegex())) return true;
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
    public RosettoValue get(int argNum) {
        return args.get(argNum);
    }

    /**
     * キーワード引数中の指定キーに関連づけられた値を取得する.
     */
    public RosettoValue get(String key) {
        return kwargs.get(key);
    }

    /**
     * 引数中から可変長引数を探して返す.
     * @return 可変長引数、引数中に可変長引数がなければnull
     */
    private RosettoValue searchMutableArg(List<RosettoValue> args) {
        RosettoValue result = null;
        for(RosettoValue s : args) {
            if(s.asString("").startsWith("*") && s.asString("").length() >= 2) {
                if(result != null)
                    throw new IllegalArgumentException("multiple mutablearg found");
                result = s;
            }
        }
        return result;
    }

    private static String createStringArgs(ArrayList<RosettoValue> args, 
            TreeMap<String, RosettoValue> kwargs) {
        StringBuilder builder = new StringBuilder();
        for(RosettoValue s : args) {
            builder.append(s.asString("")).append(" ");
        }
        for(Entry<String, RosettoValue> e : kwargs.entrySet()) {
            builder.append(e.getKey()).append("=").append(e.getValue().asString("")).append(" ");
        }
        
        return builder.toString().trim();
    }

    /**
     * マクロからの引数渡し属性を含んでいた場合、指定されたマクロから渡された属性を展開する.
     * @param args 展開した引数を追加する通常引数のリスト
     * @param kwargs 展開した引数を追加するキーワード引数のマップ
     * @param macroArgs マクロ実行時の実引数
     * @param syntax マクロ引数の文法定義
     */
    private void expandMacroKeys(List<RosettoValue> args, Map<String, RosettoValue> kwargs, 
            RosettoArguments macroArgs, ArgumentSyntax syntax) {
        expandAsteriskKey(args, kwargs, macroArgs, syntax);
        expandKwargs(kwargs, macroArgs, syntax);
        expandArgs(args, macroArgs, syntax);
    }

    /**
     * 全展開引数を含んでいた場合、指定されたマクロの属性リストをこの属性リストに展開する.
     * @param kwargs 展開した引数を追加するキーワード引数のマップ
     * @param args 展開した引数を追加する通常引数のリスト
     * @param macroArgs マクロ実行時の実引数
     * @param syntax マクロ引数の文法定義
     */
    private void expandAsteriskKey(List<RosettoValue> args, Map<String, RosettoValue> kwargs, 
            RosettoArguments macroArgs, ArgumentSyntax syntax) {
        if(args.contains(syntax.getMacroAllExpandArg())) {
            kwargs.putAll(macroArgs.kwargs);
            args.addAll(macroArgs.args);
            args.remove(syntax.getMacroAllExpandArg());
        }
    }

    /**
     * 通常引数を展開する.
     * @param args 展開して変更を加える通常引数のリスト
     * @param macroArgs マクロ実行時の実引数
     * @param syntax マクロ引数の文法定義
     */
    private static void expandArgs(List<RosettoValue> args, 
            RosettoArguments macroArgs, ArgumentSyntax syntax) {
        //引数のリストを複製
        List<RosettoValue> result = new ArrayList<RosettoValue>(args);
        
        for(int i = 0; i < args.size(); i++) {
            //展開した値で結果のリストを編集
            result.set(i, expandValue(args.get(i), macroArgs, syntax));
        }
        
        //null値は除去しておく
        while(result.remove(null)) {}
        
        //与えられたリストを変更
        args.clear();
        args.addAll(result);
    }

    /**
     * キーワード引数を展開する.
     * @param kwargs 展開して変更を加えるキーワード引数のマップ
     * @param macroArgs マクロ実行時の実引数
     * @param syntax マクロ引数の文法定義
     */
    @SuppressWarnings("unchecked")
    private static void expandKwargs(Map<String, RosettoValue> kwargs, 
            RosettoArguments macroArgs, ArgumentSyntax syntax) {
        for(Entry<String, RosettoValue> entry : kwargs.entrySet().toArray(new Entry[kwargs.entrySet().size()])) {
            //展開した値でentryを編集
            entry.setValue(expandValue(entry.getValue(), macroArgs, syntax));
            
            //編集後の値がnullなら除去しておく
            if(entry.getValue() == null) kwargs.remove(entry.getKey());
        }
        
    }

    /**
     * 指定された値がマクロ引数であれば展開する.
     * @param toExpand
     * @param macroArgs
     * @param syntax
     * @return
     */
    private static RosettoValue expandValue(RosettoValue toExpand, 
            RosettoArguments macroArgs, ArgumentSyntax syntax) {
        //この引数がマクロ展開引数でなければ何もしない
        if(!toExpand.asString("").matches(syntax.getExpandArgRegex())) return toExpand;
        //接頭詞を除いた引数の値を取得
        String value = toExpand.asString("").substring(syntax.getMacroExpandPrefix().length());
        
        //デフォルト値区切りより前を置き換えするキーに、後をデフォルト値にする
        String valueKey = value;
        RosettoValue defaultValue = null;
        int barIndex = value.indexOf(syntax.getMacroDefaultValueChar());
        if(barIndex > 0) {
            valueKey = value.substring(0, barIndex);
            defaultValue = Values.create(value.substring(barIndex + 1));
        }
        //属性リストから値の取得
        RosettoValue macroValue = macroArgs.get(valueKey);
        //取得できなければデフォルト値を使う
        if(macroValue == null) macroValue = defaultValue;
        return macroValue;
    }
}
