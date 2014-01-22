/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.models.base.values;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.exceptions.NotConvertibleException;
import info.rosetto.models.base.function.FunctionName;
import info.rosetto.models.base.function.RosettoArgument;
import info.rosetto.models.base.function.RosettoArguments;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.utils.base.RosettoLogger;
import info.rosetto.utils.base.Values;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * rosetto中の関数呼び出しやマクロ呼び出しを表すモデル.<br>
 * 実行する対象を表す変数名と実行の際に渡す引数を保持する.
 * @author tohhy
 */
@Immutable
public class ActionCall implements RosettoValue {
    private static final long serialVersionUID = 2814186388967644971L;
    
    /**
     * このアクションが実行する対象の変数名.
     */
    @Nonnull
    private final String functionName;
    
    /**
     * 実行時の引数リスト.
     */
    @Nonnull
    private final RosettoArguments args;
    
    /**
     * 何もしないActionCall.空の関数呼び出しを行う.
     */
    public static final ActionCall EMPTY = 
            new ActionCall(RosettoFunction.pass.getFullName(), RosettoArguments.EMPTY);
    
    /**
     * 引数なしで指定した対象を実行するActionCallを生成する.
     * @param functionName 呼び出す関数名
     */
    public ActionCall(String functionName) {
        this(functionName, RosettoArguments.EMPTY);
    }
    
    /**
     * 引数なしで指定関数を呼び出すActionCallを生成する.
     * @param functionName 呼び出す関数名
     */
    public ActionCall(FunctionName functionName) {
        this(functionName.getFullName(), RosettoArguments.EMPTY);
    }
    
    /**
     * 指定引数で指定関数を呼び出すActionCallを生成する.
     * @param functionName 呼び出す関数名
     * @param args 適用する引数
     */
    public ActionCall(String functionName, String args) {
        this(functionName, new RosettoArguments(args));
    }
    
    /**
     * 指定引数で指定関数を呼び出すFunctionCallを生成する.
     * @param functionName 呼び出す関数名
     * @param args 適用する引数
     */
    public ActionCall(FunctionName functionName, String args) {
        this(functionName.getFullName(), new RosettoArguments(args));
    }
    
    /**
     * 指定引数で指定関数を呼び出すFunctionCallを生成する.
     * @param functionName 呼び出す関数名
     * @param args 適用する引数
     */
    public ActionCall(String functionName, List<RosettoArgument> args) {
        if(functionName == null)
            throw new IllegalArgumentException("関数オブジェクトがnullです");
        this.functionName = functionName;
        this.args = (args != null) ? new RosettoArguments(args) : RosettoArguments.EMPTY;
    }
    
    /**
     * 指定引数で指定関数を呼び出すFunctionCallを生成する.
     * @param functionName 呼び出す関数名
     * @param args 適用する引数
     */
    public ActionCall(String functionName, RosettoArguments args) {
        if(functionName == null)
            throw new IllegalArgumentException("関数オブジェクトがnullです");
        this.functionName = functionName;
        this.args = (args != null) ? args : RosettoArguments.EMPTY;
    }
    
    /**
     * 指定引数で指定関数を呼び出すFunctionCallを生成する.
     * @param functionName 呼び出す関数名
     * @param args 適用する引数
     */
    public ActionCall(FunctionName functionName, RosettoArguments args) {
        this(functionName.getFullName(), args);
    }
    
    /**
     * この関数呼び出しを表現する正規記法のタグに再度変換する.
     */
    public String toTag() {
        if(args.getSize() == 0) return "[" + functionName + "]";
        return "[" + functionName + " " + args.getStringArgs() + "]";
    }
    
    /**
     * このFunctionCallで呼び出される関数名を返す.
     * @return このFunctionCallで呼び出される関数名
     */
    public String getFunctionName() {
        return functionName;
    }
    
    /**
     * この呼び出しにおいて関数に渡される引数リストを返す.
     * @return この呼び出しにおいて関数に渡される引数リスト
     */
    public RosettoArguments getArgs() {
        return args;
    }
    
    @Override
    public ValueType getType() {
        return ValueType.ACTION_CALL;
    }
    
    @Override
    public Object getValue() {
        return this;
    }
    
    

    /**
     * このActionCallを評価する.
     * 関数名から関数を生成し、その関数と引数から実行時引数を生成し、
     * 関数のexecメソッドを呼び出す.
     * 関数が見つからなかった場合はマクロコンテキストを参照し、同様に実行する.
     * 変数が見つからない場合や、実行可能な対象でなかった場合は処理が省略されてVoidが返る.
     * @return 評価した結果
     */
    public RosettoValue evaluate() {
        String functionName = this.getFunctionName();
        RosettoValue v = Contexts.get(functionName);
        
        if(v == null) {
            RosettoLogger.warning("実行可能な対象 " + functionName + "がコンテキスト中に見つかりません");
            return Values.VOID;
        }
        
        if(v.getType() == ValueType.FUNCTION) {
            RosettoFunction f = (RosettoFunction) v;
            RosettoArguments args = this.getArgs();
            RosettoValue result = f.exec(args);
//            Observatories.getAction().functionExecuted(this);
            return result;
        }
        
//        if(v.getType() == ValueType.MACRO) {
//            MacroBlock macro = (MacroBlock) v;
//            Contexts.getProgress().getWhole()
//            .pushScenario(macro.create(args));
//            return true;
//        }
        
        //それでもなければ何もしない
        RosettoLogger.warning("変数 " + functionName + "は実行不可能です");
        return Values.VOID;
    }
    
    @Override
    public String asString() {
        return evaluate().asString();
    }

    @Override
    public boolean asBool() throws NotConvertibleException {
        return evaluate().asBool();
    }

    @Override
    public int asInt() throws NotConvertibleException {
        return evaluate().asInt();
    }
    
    @Override
    public double asDouble() throws NotConvertibleException {
        return evaluate().asDouble();
    }
    
    @Override
    public boolean asBool(boolean defaultValue) {
        return evaluate().asBool(defaultValue);
    }
    
    @Override
    public int asInt(int defaultValue) {
        return evaluate().asInt(defaultValue);
    }
    
    @Override
    public double asDouble(double defaultValue) {
        return evaluate().asDouble(defaultValue);
    }
}
