/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.elements.values;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoAction;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.system.Scope;
import info.rosetto.observers.Observatories;
import info.rosetto.system.RosettoLogger;
import info.rosetto.system.exceptions.NotConvertibleException;
import info.rosetto.utils.base.Values;

/**
 * 
 * @author tohhy
 */
public class ScriptValue implements RosettoAction {
    private static final long serialVersionUID = 40494134581575429L;
    
    private final String name;
    
    private final String script;
    
    public ScriptValue(String name, String script) {
        this.name = name;
        this.script = script;
    }
    
    public ScriptValue(String script) {
        this.name = "";
        this.script = script;
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
        // TODO playerのインタフェースを作って対応
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

    @Override
    public String getName() {
        return name;
    }

    /**
     * 引数なしでこのスクリプトを実行する.
     * execute(RosettoArguments.EMPTY)と同じ.
     */
    @Override
    public RosettoValue execute(Scope parentScope) {
        return execute(OptionableList.EMPTY, parentScope);
    }

    /**
     * 特殊関数では評価順を変える等の処理をする.
     * @param args
     * @param parentScope
     * @return
     */
    protected Scope createScope(OptionableList args, Scope parentScope) {
        OptionableList evaluated = args.evaluateChildren(parentScope);
        return new Scope(parentScope, evaluated.getMap());
    }
    
    /**
     * このスクリプトを実行する.
     * @param args 実行時引数
     */
    @Override
    public RosettoValue execute(OptionableList args, Scope parentScope) {
        if(args == null)
            args = OptionableList.EMPTY;
        RosettoValue result = Values.NULL;
        try {
            Scope scriptScope = createScope(args, parentScope);
            Scenario parsed = Contexts.getParser().parseScript(this);
            Contexts.getPlayer().pushScenario(parsed, scriptScope);
        } catch(Exception e) {
            e.printStackTrace();
        }
        Observatories.getAction().macroExecuted(this, args, result);
        logMacroExecuted();
        return Values.VOID;
    }
    
    /**
     * 悪路実行のログを取る. finerで出力される.
     * @param func 実行された関数
     */
    private void logMacroExecuted() {
        RosettoLogger.finer("macro executed: " + toString());
    }

    @Override
    public RosettoValue execute(String args, Scope parentScope) {
        // TODO Auto-generated method stub
        return null;
    }

    public String getScript() {
        return script;
    }
}
