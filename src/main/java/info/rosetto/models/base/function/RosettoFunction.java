/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.models.base.function;

import info.rosetto.models.base.scenario.Unit;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.utils.base.ArgumentsUtils;
import info.rosetto.utils.base.RosettoLogger;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.concurrent.Immutable;


/**
 * Rosettoのスクリプト中の関数を表すオブジェクト.イミュータブル.
 * 処理を定義してインスタンス化し、Functionsクラスのコンテキストに追加することで
 * スクリプト上で関数として呼び出せるようになる.
 * @author tohhy
 */
@Immutable
public abstract class RosettoFunction {
    /**
     * 何もしない関数.テキストのみのユニット等で用いられる.
     */
    public static final RosettoFunction pass = new RosettoFunction(
            new FunctionName("core", "pass")) {
        @Override
        protected RosettoValue run(ExpandedArguments args) {
            return RosettoValue.VOID;
        }
    };
    
    private final FunctionName name;
    private final String[] args;
    private final String functionInfo;
    
    /**
     * 関数オブジェクトを生成する.<br>
     * 生成したオブジェクトはFunctionsクラスのコンテキストに追加することで
     * スクリプト上で関数として呼び出せる.<br>
     * 
     * 抽象クラスになっているため、継承およびインスタンス化の際に
     * コンストラクタ引数と処理関数の実装が要求される.<br>
     * 
     * argsは可変長引数になっており、0個以上の文字列かString配列を取る.<br>
     * 例：<br>
     * <code>
     * RosettoFunction lf = new RosettoFunction(new FunctionName("text.lf")) {...};<br>
     * RosettoFunction title = new RosettoFunction(new FunctionName("system.title"),
     *  "text", "option=none") {...};<br>
     * </code>
     * @param name 関数名オブジェクト
     * @param args 引数(0個以上)
     */
    public RosettoFunction(FunctionName name, String... args) {
        this.name = name;
        if(args == null) args = new String[0];
        if(args.length == 1)
            args = ArgumentsUtils.splitStringArgs(args[0]);
        validateArgs(args);
        this.args = args;
        this.functionInfo = createFunctionInfo();
    }
    
    /**
     * この抽象メソッドに関数の実行内容を定義する.
     * argsは非nullが保証される.空引数の際にはRuntimeArguments.EMPTYが渡される.
     * RuntimeArgumentsはTreeMapで、展開済みのキーワード引数になっている.
     * そのため、個々の引数も非nullになる.
     * execメソッドが実行される際にこのメソッドが呼び出される.
     */
    protected abstract RosettoValue run(ExpandedArguments args);
    
    
    protected Unit runOnParse(Unit u, ExpandedArguments args) {
        return u;
    }
    
    
    /**
     * この関数を実行する.
     * @param args 実行時引数
     */
    public RosettoValue exec(RosettoArguments args) {
        if(args == null)
            args = RosettoArguments.EMPTY;
        RosettoValue result = run(new ExpandedArguments(args, this));
        logExecutedFunction(this);
        return result;
    }
    
    /**
     * この関数を実行する.
     * @param args 実行時引数
     */
    public RosettoValue exec(String args) {
        RosettoValue result = null;
        if(args == null) {
            result = run(new ExpandedArguments(RosettoArguments.EMPTY, this));
        } else {
            result = run(new ExpandedArguments(new RosettoArguments(args), this));
        }
        logExecutedFunction(this);
        return result;
    }
    
    /**
     * 引数なしでこの関数を実行する.
     * exec(RuntimeArguments.EMPTY)と同じ.
     */
    public RosettoValue exec() {
        return exec(RosettoArguments.EMPTY);
    }
    
    /**
     * パース時に実行する処理を指定する.
     * デフォルトではユニットをそのまま返すだけの実装.
     * ラベル等、プリプロセス段階でユニット以外に変換する必要がある要素等に対応するために用いる.
     */
    public final Unit execOnParse(Unit u) {
        return runOnParse(u, new ExpandedArguments(u.getAction().getArgs(), this));
    }
    
    /**
     * この関数の関数名オブジェクトを取得する.
     * @return この関数の関数名オブジェクト
     */
    public FunctionName getNameObject() {
        return name;
    }
    
    /**
     * この関数の正式名を取得する.
     * getNameObject().getFullName()と同じ
     * @return この関数の正式名
     */
    public String getFullName() {
        return name.getFullName();
    }
    
    /**
     * この関数の短縮名を取得する.
     * getNameObject().getShortName()と同じ.
     * @return この関数の短縮名
     */
    public String getShortName() {
        return name.getShortName();
    }
    
    /**
     * この関数の所属パッケージを取得する.
     * getNameObject().getPackage()と同じ.
     * @return この関数の短縮名
     */
    public String getPackage() {
        return name.getPackage();
    }

    /**
     * この関数が取る引数の配列を返す.
     * 関数がoption(o1, o2=10, o3="hoge")なら
     * {"o1", "o2=10", "o3=hoge"}が返る.
     * @return この関数が取る引数の配列
     */
    public String[] getArguments() {
        return args;
    }
    
    /**
     * この関数の情報を返す.  
     * function testfunc(arg1, arg2=10) <example.test.testfunc>  
     * のような形式で関数の情報が返される.
     * @return この関数の情報
     */
    public String getFunctionInfo() {
        return functionInfo;
    }
    
    /**
     * getFunctionInfo()と同値.
     */
    @Override
    public String toString() {
        return getFunctionInfo();
    }
    
    /**
     * この関数を引数なしで呼び出した際のアクションオブジェクトを生成する.
     * @return この関数を引数なしで呼び出した際のアクションオブジェクト
     */
    public FunctionCall toAction() {
        return new FunctionCall(getNameObject());
    }
    
    /**
     * この関数を指定引数で呼び出した際のアクションオブジェクトを生成する.
     * @param args 呼び出し時に与える引数
     * @return この関数を指定引数で呼び出した際のアクションオブジェクト
     */
    public FunctionCall toAction(RosettoArguments args) {
        return new FunctionCall(getNameObject(), args);
    }
    
    /**
     * この関数を指定引数で呼び出した際のアクションオブジェクトを生成する.
     * @param args 呼び出し時に与える引数
     * @return この関数を指定引数で呼び出した際のアクションオブジェクト
     */
    public FunctionCall toAction(String args) {
        return new FunctionCall(getNameObject(), args);
    }
    
    /**
     * 引数定義の形式が正当かをチェックする.文法エラーが見つかると例外をスローする.
     * Ruby同様、一旦引数のデフォルト値を定義したら
     * それ以降の引数には全てデフォルト値が定義されていなければいけない
     */
    private void validateArgs(String[] args) {
        boolean isDefaultDefined = false;
        for(String s:args) {
            if(s.indexOf("=") < 0) {
                //非キーワード引数なら
                if(isDefaultDefined)
                    throw new IllegalArgumentException("キーワード引数を定義した後に非キーワード引数は定義できません");
            } else {
                //キーワード引数なら
                isDefaultDefined = true;
            }
        }
    }

    /**
     * この関数の情報を表す文字列を生成する.
     * @return この関数の情報を表す文字列
     */
    private String createFunctionInfo() {
        StringBuilder result = new StringBuilder();
        result.append("function ").append(getShortName()).append("(");
        String[] args = getArguments();
        for(int i=0; i<args.length; i++) {
            result.append(args[i]);
            if(i != args.length-1)
                result.append(", ");
        }
        result.append(") <").append(getFullName()).append(">");
        return result.toString();
    }

    /**
     * 関数実行のログを取る. finerで出力される.
     * @param func 実行された関数
     */
    private static void logExecutedFunction(RosettoFunction func) {
        RosettoLogger.finer("function executed: " + func.toString());
    }

    /**
     * 関数の実行時に用いるキーワード引数のリスト.
     * インスタンス化のためには引数として与える先の関数が必要になる.
     * 関数の持つ引数リストと照らし合わせ、適切に引数を割り振ることで生成される.
     * @author tohhy
     */
    public class ExpandedArguments {
        private final Map<String, String> kwargs;
        
        /**
         * 未評価の引数と関数を取り、実行時引数の実体を生成する.
         * @param args 未評価の引数
         * @param func 実行する関数
         */
        protected ExpandedArguments(RosettoArguments args, RosettoFunction func) {
            Map<String, String> kwargs = new TreeMap<String, String>();
            kwargs.putAll(args.parse(func));
            this.kwargs = Collections.unmodifiableMap(kwargs);
        }
        
        /**
         * 指定引数名にマッピングされた引数値を取得する.
         * 指定引数が存在しない場合はnull
         * @param key 引数名
         * @return 指定引数名にマッピングされた引数値
         */
        public String get(String key) {
            return kwargs.get(key);
        }
        
        /**
         * 指定キーがこの引数マップに含まれているかを取得する.
         * @param key 引数名
         * @return キーが存在するかどうか
         */
        public boolean containsKey(String key) {
            return kwargs.containsKey(key);
        }

        /**
         * キーワード引数のマップを取得する.読み込み専用.
         * @return
         */
        public Map<String, String> getMap() {
            return kwargs;
        }
    }
    
    /**
     * 与えられた引数がデフォルト（変更なし）引数であるかを返す.
     * @param arg
     * @return
     */
    protected boolean isDefault(String arg) {
        return arg.equals("default");
    }
    
    /**
     * 与えられた引数がnone（指定なし）引数であるかを返す.
     * @param arg
     * @return
     */
    protected boolean isNone(String arg) {
        return arg.equals("none");
    }
}

