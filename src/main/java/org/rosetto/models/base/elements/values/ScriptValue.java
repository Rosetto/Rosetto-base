/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.rosetto.models.base.elements.values;

import org.rosetto.contexts.base.Rosetto;
import org.rosetto.models.base.elements.RosettoAction;
import org.rosetto.models.base.elements.RosettoValue;
import org.rosetto.models.base.elements.ValueType;
import org.rosetto.models.base.scenario.Scenario;
import org.rosetto.models.system.Scope;
import org.rosetto.observers.RosettoObservatories;
import org.rosetto.system.RosettoLogger;
import org.rosetto.system.exceptions.NotConvertibleException;
import org.rosetto.system.messages.SystemMessage;
import org.rosetto.utils.base.Values;

/**
 * 
 * @author tohhy
 */
public class ScriptValue implements RosettoAction {
    private static final long serialVersionUID = 40494134581575429L;
    
    public static final ScriptValue EMPTY = new ScriptValue("");
    
    private final String script;
    
    public ScriptValue(String script) {
        this.script = script;
    }
    
    /**
     * 特殊関数では評価順を変える等の処理をする.
     * @param args
     * @param parentScope
     * @return
     */
    protected Scope createScope(ListValue args, Scope parentScope) {
        ListValue evaluated = args.evaluateChildren(parentScope);
        return new Scope(parentScope, evaluated.getMap());
    }

    /**
     * 引数なしでこのスクリプトを実行する.
     * execute(RosettoArguments.EMPTY)と同じ.
     */
    @Override
    public RosettoValue execute(Scope parentScope) {
        return execute(ListValue.EMPTY, parentScope);
    }

    /**
     * このスクリプトを実行する.
     * @param args 実行時引数
     */
    @Override
    public RosettoValue execute(ListValue args, Scope parentScope) {
        if(args == null)
            args = ListValue.EMPTY;
        RosettoValue result = Values.NULL;
        try {
            Scope scriptScope = createScope(args, parentScope);
            Scenario parsed = Rosetto.getParser().parseScript(this);
            Rosetto.getPlayer().pushScenario(parsed, scriptScope);
        } catch(Exception e) {
            e.printStackTrace();
        }
        RosettoObservatories.getAction().macroExecuted(this, args, result);
        String s = (script.length() > 10) ? asString().substring(0, 10) : asString();
        RosettoLogger.finer(SystemMessage.S11100_MACRO_EXECUTED, s);
        return Values.VOID;
    }

    @Override
    public RosettoValue execute(String args, Scope parentScope) {
        return execute(ListValue.createFromString(args), parentScope);
    }

    public String getScript() {
        return script;
    }

    @Override
    public String toString() {
        return script;
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ScriptValue) {
            return script.equals(((ScriptValue)obj).toString());
        }
        return false;
    }
    

    @Override
    public ValueType getType() {
        return ValueType.SCRIPT;
    }

    @Override
    public Object getValue() {
        return script;
    }

    @Override
    public RosettoValue evaluate(Scope scope) {
        Rosetto.getPlayer().pushScenario(Rosetto.getParser().parseScript(script), scope);
        return Values.VOID;
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
    public String asString() throws NotConvertibleException {
        return "{" + script + "}";
    }

    @Override
    public String asString(String defaultValue) {
        return "{" + script + "}";
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
