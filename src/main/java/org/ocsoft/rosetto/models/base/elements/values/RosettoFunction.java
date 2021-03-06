/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.models.base.elements.values;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.lang3.StringUtils;
import org.ocsoft.rosetto.models.base.elements.RosettoAction;
import org.ocsoft.rosetto.models.base.elements.RosettoValue;
import org.ocsoft.rosetto.models.base.elements.ValueType;
import org.ocsoft.rosetto.models.system.Scope;
import org.ocsoft.rosetto.observers.RosettoObservatories;
import org.ocsoft.rosetto.system.RosettoLogger;
import org.ocsoft.rosetto.system.exceptions.NotConvertibleException;
import org.ocsoft.rosetto.system.exceptions.UnExpectedTypeValueException;
import org.ocsoft.rosetto.system.messages.SystemMessage;
import org.ocsoft.rosetto.utils.base.Values;

/**
 * Rosettoのスクリプト中の関数を表すオブジェクト.イミュータブル.<br>
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
    private final List<String> args;
    
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
        //引数が空なら空リスト
        if(args == null) {
            this.args = new ArrayList<String>(0);
            return;
        }
        //引数があれば最適なリスト
        int len = args.length;
        this.args = new ArrayList<String>(len);
        for(int i=0;i<len;i++) {
            this.args.add(args[i]);
        }
    }
    
    /**
     * 
     * @param args 引数名リスト
     */
    public RosettoFunction(String name, RosettoValue args) {
        this.name = name;
        if(args == null) {
            this.args = new ArrayList<String>(0);
            return;
        }
        if(args.getType() == ValueType.LIST) {
            ListValue argList = ((ListValue)args);
            this.args = new ArrayList<String>(argList.size());
            for(RosettoValue s : argList.getList()) {
                this.args.add(s.asString());
            }
        } else {
            this.args = new ArrayList<String>(1);
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
    protected abstract RosettoValue run(Scope scope, ListValue rawArgs);

    /**
     * 特殊関数では評価順を変える等の処理をする.
     * @param args
     * @param parentScope
     * @return
     */
    protected Scope createScope(ListValue args, Scope parentScope) {
        //通常はすべての関数が評価されて渡される
        return new Scope(args.evaluateChildren(parentScope), this, parentScope);
    }

    /**
     * 引数なしでこの関数を実行する.
     * execute(RosettoArguments.EMPTY)と同じ.
     */
    public RosettoValue execute(Scope parentScope) {
        return execute(ListValue.EMPTY, parentScope);
    }

    /**
     * この関数を実行する.
     * @param args 実行時引数
     */
    public RosettoValue execute(ListValue args, Scope parentScope) {
        if(args == null)
            args = ListValue.EMPTY;
        RosettoValue result = Values.NULL;
        try {
            Scope functionScope = createScope(args, parentScope);
            result = run(functionScope, args);
        } catch(Exception e) {
            RosettoLogger.throwing(getClass(), "execute", e);
        }
        RosettoObservatories.getAction().functionExecuted(this, args, result);
        RosettoLogger.finer(SystemMessage.S11000_FUNCTION_EXECUTED, this.toString());
        return result;
    }

    /**
     * この関数を実行する.
     * @param args 文字列形式の実行時引数
     */
    public RosettoValue execute(String args, Scope parentScope) {
        ListValue rargs = ListValue.EMPTY;
        if(args != null) {
            rargs = ListValue.createFromString(args);
        }
        return execute(rargs, parentScope);
    }

    /**
     * この関数の情報を表す文字列を生成する.
     * @return この関数の情報を表す文字列
     */
    private String createFunctionInfo() {
        StringBuilder result = new StringBuilder();
        result.append("[").append(name);
        String argsStr = StringUtils.join(getArguments(), ' ');
        if(argsStr.length() != 0) {
            result.append(" ").append(argsStr);
        }
        result.append("]");
        return result.toString();
    }

    /**
     * 指定した値が指定した型のいずれかに該当するかどうかを返す.
     * @param value
     * @param expected
     * @return
     */
    protected boolean checkType(RosettoValue value, ValueType...expected) {
        for(int i=0; i<expected.length; i++) {
            if(value.getType() == expected[i]) return true;
        }
        return false;
    }

    /**
     * 
     * @param value
     * @param expected
     */
    protected void validateType(RosettoValue value, ValueType...expected) {
        boolean isValid = false;
        for(int i=0; i<expected.length; i++) {
            if(value.getType() == expected[i]) {
                isValid =  true;
                return;
            }
        }
        if(!isValid) throw new UnExpectedTypeValueException();
    }

    /**
     * この関数が取る引数のリストを読み取り専用で返す.
     * @return この関数が取る引数のリスト
     */
    public List<String> getArguments() {
        return Collections.unmodifiableList(args);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return createFunctionInfo();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RosettoFunction) {
            RosettoFunction f = (RosettoFunction) obj;
            return name.equals(f.getName()) && args.equals(f.getArguments());
        }
        return false;
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
    public RosettoValue evaluate(Scope scope) {
        return this;
    }

    @Override
    public RosettoValue first() {
        return this;
    }

    @Override
    public RosettoValue rest() {
        return Values.NULL;
    }
    
    @Override
    public RosettoValue cons(RosettoValue head) {
        return new ListValue(head, this);
    }

    @Override
    public RosettoValue getAt(int index) {
        if(index == 0) return this;
        return Values.NULL;
    }
    
    @Override
    public int size() {
        return 1;
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
}
