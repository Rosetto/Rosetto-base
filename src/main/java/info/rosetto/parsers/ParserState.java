/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.parsers;

import info.rosetto.models.base.scenario.Label;
import info.rosetto.models.base.scenario.ScenarioToken;
import info.rosetto.models.base.scenario.Unit;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * パース実行中のパーサーの状態.<br>
 * 一度のパース実行中にのみ生成され、パース終了後に破棄される.
 * @author tohhy
 */
public class ParserState {
    /**
     * パーサーが保持するユニットやラベルのリスト.
     */
    private final List<ScenarioToken> tokens = new LinkedList<ScenarioToken>();
    /**
     * 現在パース中のユニット.
     */
    private Unit parsingUnit;
    /**
     * 現在パース中のユニットのインデックス.
     * ラベルの位置を記憶する際に用いる.
     */
    private int unitIndex;
    
    /**
     * パッケージ内でのみ生成.
     */
    ParserState() {}
    
    /**
     * 指定されたユニットをトークンリストに追加する.
     * @param unit 追加するユニット
     */
    public void addUnit(Unit unit) {
        tokens.add(unit);
        unitIndex++;
    }
    
    /**
     * 指定されたラベルをトークンリストに追加する.
     * @param label
     */
    public void addLabel(Label label) {
        tokens.add(label);
    }
    
    /**
     * パーサーが保持するユニットやラベルのリストを読み取り専用で返す.
     * @return
     */
    protected List<ScenarioToken> getTokens() {
        return Collections.unmodifiableList(tokens);
    }
    
    /**
     * 現在パース中のユニットを返す.
     * @return 現在パース中のユニット
     */
    public Unit getParsingUnit() {
        return parsingUnit;
    }
    
    /**
     * 現在パース中のユニットを指定する.
     * @param currentUnit 現在パース中のユニット
     */
    public void setCurrentUnit(Unit currentUnit) {
        this.parsingUnit = currentUnit;
    }
    
    /**
     * 現在パース中のユニットのインデックスを取得する.
     * @return 現在パース中のユニットのインデックス
     */
    public int getUnitIndex() {
        return unitIndex;
    }
}
