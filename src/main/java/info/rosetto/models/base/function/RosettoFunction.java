/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.function;

import info.rosetto.models.base.values.ListValue;
import info.rosetto.models.base.values.RosettoAction;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.models.base.values.ValueType;
import info.rosetto.models.state.variables.Scope;
import info.rosetto.observers.ActionObservatory;
import info.rosetto.system.RosettoLogger;
import info.rosetto.system.exceptions.NotConvertibleException;
import info.rosetto.utils.base.Values;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.concurrent.Immutable;

/**
 * Rosettoのスクリプト中の関数を表すオブジェクト.イミュータブル.
 * 処理を定義してインスタンス化し、Functionsクラスのコンテキストに追加することで
 * スクリプト上で関数として呼び出せるようになる.
 * @author tohhy
 */
@Immutable
public abstract class RosettoFunction implements RosettoValue, RosettoAction {
    private static final long serialVersionUID = 4377270634098291404L;

    /**
     * この関数の名前.
     */
    private final String name;
    
    /**
     * この関数がとる引数のリスト.
     */
    private final List<String> args = new ArrayList<String>();
    
    /**
     * この関数のオブジェクトを指定した名前と引数で生成する.<br>
     * 
     * 抽象クラスになっているため、継承およびインスタンス化の際に
     * コンストラクタ引数と処理関数の実装が要求される.<br>
     * 
     * argsは可変長引数になっており、0個以上の文字列かString配列を取る.<br>
     * 例：<br>
     * <code>
     * RosettoFunction font = new RosettoFunction("font",
     *  "family", "style=plain", "size=20", "option=none") {...};<br>
     * </code>
     * @param name 関数名オブジェクト
     * @param args 引数(0個以上)
     */
    public RosettoFunction(String name, String...args) {
        if(name == null)
            throw new IllegalArgumentException("name must not be null");
        this.name = name;
        if(args == null) return;
        for(String s : args) {
            this.args.add(s);
        }
    }
    
    /**
     * 無名のlambda関数を生成する.
     * @param args 引数名リスト
     */
    public RosettoFunction(RosettoValue args) {
        this("", args);
    }
    
    /**
     * @param args 引数名リスト
     */
    public RosettoFunction(String name, RosettoValue args) {
        this.name = name;
        if(args == null) return;
        if(args.getType() == ValueType.HASH) {
            
        } else if(args.getType() == ValueType.LIST) {
            for(RosettoValue s : ((ListValue)args).getList()) {
                this.args.add(s.asString());
            }
        } else {
            this.args.add(args.asString());
        }
    }
    
    /**
     * この関数の実行内容を定義するメソッド.<br>
     * argsは非nullが保証される.空引数の際にはRuntimeArguments.EMPTYが渡される.<br>
     * RuntimeArgumentsはTreeMapで、展開済みのキーワード引数になっている.<br>
     * そのため、個々の引数も非nullになる.<br>
     * execメソッドが実行される際にこのメソッドが呼び出される.
     */
    protected abstract RosettoValue run(Scope scope, RosettoArguments rawArgs);
    
    
    /**
     * 特殊関数では評価順を変える等の処理をする.
     * @param args
     * @param parentScope
     * @return
     */
    protected Scope createScope(RosettoArguments args, Scope parentScope) {
        return new Scope(args, this, parentScope);
    }

    /**
     * 引数なしでこの関数を実行する.
     * execute(RosettoArguments.EMPTY)と同じ.
     */
    public RosettoValue execute(Scope parentScope) {
        return execute(RosettoArguments.EMPTY, parentScope);
    }
    
    /**
     * この関数を実行する.
     * @param args 実行時引数
     */
    public RosettoValue execute(RosettoArguments args, Scope parentScope) {
        if(args == null)
            args = RosettoArguments.EMPTY;
        RosettoValue result = Values.NULL;
        try {
            result = run(createScope(args, parentScope), args);
        } catch(Exception e) {
            e.printStackTrace();
        }
        ActionObservatory.getInstance().functionExecuted(this, args, result);
        logFunctionExecuted(this);
        return result;
    }
    
    
    /**
     * この関数を実行する.
     * @param args 文字列形式の実行時引数
     */
    public RosettoValue execute(String args, Scope parentScope) {
        RosettoArguments rargs = RosettoArguments.EMPTY;
        if(args != null) {
            rargs = new RosettoArguments(args);
        }
        RosettoValue result = Values.NULL;
        try {
            result = run(createScope(rargs, parentScope), rargs);
        } catch(Exception e) {
            e.printStackTrace();
        }
        ActionObservatory.getInstance().functionExecuted(this, rargs, result);
        logFunctionExecuted(this);
        return result;
    }
    
    /**
     * この関数の情報を表す文字列を生成する.
     * @return この関数の情報を表す文字列
     */
    private String createFunctionInfo() {
        StringBuilder result = new StringBuilder();
        result.append("[").append(name).append(" ");
        List<String> args = getArguments();
        for(int i=0; i<args.size(); i++) {
            result.append(args.get(i));
            if(i != args.size()-1)
                result.append(" ");
        }
        result.append("]");
        return result.toString();
    }
    
    @Override
    public String toString() {
        return createFunctionInfo();
    }
    
    /**
     * この関数が取る引数のリストを読み取り専用で返す.<br>
     * 関数がoption(o1, o2=10, o3="hoge")なら
     * {"o1", "o2=10", "o3=hoge"}が返る.
     * @return この関数が取る引数のリスト
     */
    public List<String> getArguments() {
        return Collections.unmodifiableList(args);
    }
    
    public String getName() {
        return name;
    }


    @Override
    public ValueType getType() {
        return ValueType.FUNCTION;
    }

    @Override
    public RosettoFunction getValue() {
        return this;
    }

    @Override
    public String asString() {
        return toString();
    }
    
    @Override
    public String asString(String defaultValue) {
        return toString();
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
    
    
    /**
     * 関数実行のログを取る. finerで出力される.
     * @param func 実行された関数
     */
    private static void logFunctionExecuted(RosettoFunction func) {
        RosettoLogger.finer("function executed: " + func.toString());
    }
    
}
