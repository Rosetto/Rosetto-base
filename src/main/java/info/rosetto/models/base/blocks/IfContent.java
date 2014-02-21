/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.blocks;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.base.scenario.Scenario.ScenarioType;

import java.io.Serializable;
import java.util.List;

/**
 * if文の内容を定義したモデル.
 * 分岐ごとにテキストを保持する.イミュータブル.
 * @author tohhy
 */
public class IfContent implements Serializable {
    private static final long serialVersionUID = -6519087805855131798L;
    
    /**
     * 分岐のリスト.
     */
    private final List<IfBranch> branches;
    
    /**
     * else節の内容.
     */
    private final String elseBody;
    
    /**
     * 分岐のリストとelse節の内容を与えてこのIf文を初期化する.
     * @param branches 分岐のリスト
     * @param elseBody else節の内容
     */
    public IfContent(List<IfBranch> branches, String elseBody) {
        if(branches == null) 
            throw new IllegalArgumentException("null list received");
        this.branches = branches;
        this.elseBody = elseBody;
    }
    
    /**
     * このIf文の内容をシナリオに変換する.
     * 分岐ごとの条件式を評価し、trueだったものをシナリオ化して返す.
     * @return シナリオに変換したこのif文
     */
    public Scenario create() {
        Scenario s = Contexts.getParser().parseScript(getTrueBranch());
        return new Scenario(s.getUnits(), ScenarioType.IF);
    }
    
    /**
     * branchesを上から処理し、条件がtrueだったBranchの内容を返す.
     * 全Branchが条件に一致しなかった場合はelseBodyを返す.
     * elseBodyが定義されていなければnullが返る.
     * @return 条件がtrueだったBranchの内容、またはelse節の内容、またはnull
     */
    public String getTrueBranch() {
        for(IfBranch b : branches) {
            //TODO
//            Object evaluatedExp = Contexts.getIScript().evaluate(b.getExp());
//            boolean isTrueKey = Context.toBoolean(evaluatedExp);
//            if(isTrueKey) return b.getContent();
            return b.getContent();
        }
        return elseBody;
    }
}
