/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.blocks;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoAction;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.base.elements.values.ActionCall;
import info.rosetto.models.base.elements.values.OptionableList;
import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.base.scenario.Scenario.ScenarioType;
import info.rosetto.models.base.scenario.Unit;
import info.rosetto.models.state.variables.Scope;
import info.rosetto.system.exceptions.NotConvertibleException;

import java.util.ArrayList;
import java.util.List;

/**
 * 単一のマクロを記録するブロック. イミュータブル.
 * @author tohhy
 */
public class RosettoMacro extends Block implements RosettoAction {
    private static final long serialVersionUID = 4177216826565925097L;
    
    /**
     * このマクロの名称.
     */
    private final String name;
    
    /**
     * マクロの名称と実行内容を指定してこのマクロブロックを初期化する.
     * @param name マクロの名称
     * @param body 実行内容
     */
    public RosettoMacro(String name, String body) {
        super(body);
        if(name == null || name.length() == 0) 
            throw new IllegalArgumentException("name must not be empty");
        this.name = name;
    }
    
    /**
     * このマクロブロックをシナリオへパースする.
     * @param contextArgs マクロ展開引数の展開の際に用いる引数
     * @return マクロブロックをパースして生成したシナリオ
     */
    public Scenario toScenario(OptionableList contextArgs) {
        contextArgs = (contextArgs == null) ? OptionableList.EMPTY : contextArgs;
        Scenario s = Contexts.getParser().parseScript(getContent());
        return new Scenario(expand(s.getUnits(), contextArgs), ScenarioType.MACRO);
    }
    
    /**
     * 指定したユニットのリストに含まれるマクロ展開引数を指定した引数で展開する.
     * @param units ユニットのリスト
     * @param contextArgs 展開用引数
     * @return 展開用引数で全ユニットの引数を展開した結果
     */
    private static List<Unit> expand(List<Unit> units, OptionableList contextArgs) {
        List<Unit> result = new ArrayList<Unit>();
        for(Unit u:units) {
            OptionableList expanded = u.getAction().getArgs();//TODO
            result.add(new Unit(u.getContent(), 
                    new ActionCall(u.getAction().getFunctionName(), expanded)));
        }
        return result;
    }

    /**
     * このマクロブロックのマクロ名を取得する.
     * @return このマクロブロックのマクロ名
     */
    public String getName() {
        return name;
    }

    @Override
    public ValueType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RosettoValue evaluate(Scope scope) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RosettoValue first() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RosettoValue rest() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RosettoValue cons(RosettoValue head) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RosettoValue getAt(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String asString() throws NotConvertibleException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String asString(String defaultValue) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean asBool() throws NotConvertibleException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean asBool(boolean defaultValue) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int asInt() throws NotConvertibleException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int asInt(int defaultValue) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long asLong() throws NotConvertibleException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public long asLong(long defaultValue) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double asDouble() throws NotConvertibleException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double asDouble(double defaultValue) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public RosettoValue execute(Scope parentScope) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RosettoValue execute(OptionableList args, Scope parentScope) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RosettoValue execute(String args, Scope parentScope) {
        // TODO Auto-generated method stub
        return null;
    }
}
