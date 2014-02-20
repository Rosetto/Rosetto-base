/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.elements;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.functions.base.BaseFunctions;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.state.variables.Scope;
import info.rosetto.observers.ActionObservatory;
import info.rosetto.system.RosettoLogger;
import info.rosetto.system.exceptions.NotConvertibleException;
import info.rosetto.utils.base.Values;

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
    private final String callName;
    
    /**
     * 実行時の引数リスト.
     */
    @Nonnull
    private final MixedStore args;
    
    /**
     * 何もしないActionCall.空の関数呼び出しを行う.
     */
    public static final ActionCall EMPTY = 
            new ActionCall(BaseFunctions.pass.getName(), MixedStore.EMPTY);
    
    /**
     * 引数なしで指定した対象を実行するActionCallを生成する.
     * @param functionName 呼び出す関数名
     */
    public ActionCall(String functionName) {
        this(functionName, MixedStore.EMPTY);
    }
    
    /**
     * 指定引数で指定関数を呼び出すActionCallを生成する.
     * @param functionName 呼び出す関数名
     * @param args 適用する引数
     */
    public ActionCall(String functionName, String args) {
        this(functionName, MixedStore.createFromString(args));
    }
    
    /**
     * 指定引数で指定関数を呼び出すActionCallを生成する.
     * @param functionName 呼び出す関数名
     * @param args 適用する引数
     */
    public ActionCall(String functionName, String[] args) {
        this(functionName, MixedStore.createFromString(args));
    }
    
    /**
     * 指定引数で指定関数を呼び出すFunctionCallを生成する.
     * @param functionName 呼び出す関数名
     * @param args 適用する引数
     */
    public ActionCall(String functionName, MixedStore args) {
        if(functionName == null)
            throw new IllegalArgumentException("関数オブジェクトがnullです");
        this.callName = functionName;
        this.args = (args != null) ? args : MixedStore.EMPTY;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ActionCall) {
            return ((ActionCall)obj).toString().equals(this.toString());
        }
        return false;
    }
    
    @Override
    public String toString() {
        if(args.getSize() == 0) return "[" + callName + "]";
        return "[" + callName + " " + args.toString() + "]";
    }
    
    
    /**
     * このFunctionCallで呼び出される関数名を返す.
     * @return このFunctionCallで呼び出される関数名
     */
    public String getFunctionName() {
        return callName;
    }
    
    /**
     * この呼び出しにおいて関数に渡される引数リストを返す.
     * @return この呼び出しにおいて関数に渡される引数リスト
     */
    public MixedStore getArgs() {
        return args;
    }
    
    /**
     * このActionCallを評価する.
     * 関数名から関数を生成し、その関数と引数から実行時引数を生成し、
     * 関数のexecuteメソッドを呼び出す.
     * 関数が見つからなかった場合はマクロコンテキストを参照し、同様に実行する.
     * 変数が見つからない場合や、実行可能な対象でなかった場合は処理が省略されてVoidが返る.
     * @return 評価した結果
     */
    public RosettoValue evaluate(Scope parentScope) {
        String varName = this.getFunctionName();
        RosettoValue v = Contexts.getAction(varName);
        
        if(v == Values.NULL || v == BaseFunctions.pass) {
            RosettoLogger.warning("実行可能な対象 " + varName + "がコンテキスト中に見つかりません");
            return Values.VOID;
        }
        
        if(v.getType() == ValueType.FUNCTION) {
            RosettoFunction f = (RosettoFunction) v;
            MixedStore args = this.getArgs();
            RosettoValue result = f.execute(args, parentScope);
            ActionObservatory.getInstance().functionExecuted(f, args, result);
            return result;
        }
        
        if(v.getType() == ValueType.MACRO) {
            //TODO
        }
        
        //それでもなければ何もしない
        RosettoLogger.warning("変数 " + varName + "は実行不可能です");
        return Values.VOID;
    }

    @Override
    public ValueType getType() {
        return ValueType.ACTION_CALL;
    }
    
    @Override
    public Object getValue() {
        return this;
    }
    
    public String evalAsString(Scope parentScope) throws NotConvertibleException {
        return evaluate(parentScope).asString();
    }
    
    public String evalAsString(String defaultValue, Scope parentScope) {
        return evaluate(parentScope).asString(defaultValue);
    }
    
    public boolean evalAsBool(Scope parentScope) throws NotConvertibleException {
        return evaluate(parentScope).asBool();
    }
    
    public boolean evalAsBool(boolean defaultValue, Scope parentScope) {
        return evaluate(parentScope).asBool(defaultValue);
    }
    
    public int evalAsInt(Scope parentScope) throws NotConvertibleException {
        return evaluate(parentScope).asInt();
    }
    
    public int evalAsInt(int defaultValue, Scope parentScope) {
        return evaluate(parentScope).asInt(defaultValue);
    }
    
    public long evalAsLong(Scope parentScope) throws NotConvertibleException {
        return evaluate(parentScope).asLong();
    }
    
    public long evalAsLong(long defaultValue, Scope parentScope) {
        return evaluate(parentScope).asLong(defaultValue);
    }
    
    public double evalAsDouble(Scope parentScope) throws NotConvertibleException {
        return evaluate(parentScope).asDouble();
    }
    
    public double evalAsDouble(double defaultValue, Scope parentScope) {
        return evaluate(parentScope).asDouble(defaultValue);
    }
    
    
    @Override
    public String asString() throws NotConvertibleException {
        throw new NotConvertibleException();
    }

    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public String asString(String defaultValue) {
        return defaultValue;
    }
    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public boolean asBool() throws NotConvertibleException {
        return false;
    }
    
    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public boolean asBool(boolean defaultValue) {
        return false;
    }
    
    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public int asInt() throws NotConvertibleException {
        throw new NotConvertibleException();
    }
    
    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public int asInt(int defaultValue) {
        return defaultValue;
    }
    
    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public long asLong() throws NotConvertibleException {
        throw new NotConvertibleException();
    }
    
    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public long asLong(long defaultValue) {
        return defaultValue;
    }
    
    /**
     * NotConvertibleExceptionをスローする.
     * @throws NotConvertibleException 
     */
    @Override
    public double asDouble() throws NotConvertibleException {
        throw new NotConvertibleException();
    }
    
    /**
     * デフォルト値を返す.
     * @return defaultValueで指定した値
     */
    @Override
    public double asDouble(double defaultValue) {
        return defaultValue;
    }
}
