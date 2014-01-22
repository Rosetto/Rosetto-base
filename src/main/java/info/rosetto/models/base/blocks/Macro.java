/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.blocks;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.function.RosettoArguments;
import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.base.scenario.Scenario.ScenarioType;
import info.rosetto.models.base.scenario.Unit;
import info.rosetto.models.base.values.ActionCall;

import java.util.ArrayList;
import java.util.List;

/**
 * 単一のマクロを記録するブロック. イミュータブル.
 * @author tohhy
 */
public class Macro extends Block {
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
    public Macro(String name, String body) {
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
    public Scenario toScenario(RosettoArguments contextArgs) {
        contextArgs = (contextArgs == null) ? RosettoArguments.EMPTY : contextArgs;
        Scenario s = Contexts.getParser().parseString(getContent());
        return new Scenario(expand(s.getUnits(), contextArgs), ScenarioType.MACRO);
    }
    
    /**
     * 指定したユニットのリストに含まれるマクロ展開引数を指定した引数で展開する.
     * @param units ユニットのリスト
     * @param contextArgs 展開用引数
     * @return 展開用引数で全ユニットの引数を展開した結果
     */
    private static List<Unit> expand(List<Unit> units, RosettoArguments contextArgs) {
        List<Unit> result = new ArrayList<Unit>();
        for(Unit u:units) {
            RosettoArguments expanded = u.getAction().getArgs()
            .createExpandedArgs(contextArgs, Contexts.getParser().getArgumentSyntax());
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
}
