/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/.
 */

package info.rosetto.parsers;

import info.rosetto.models.base.scenario.Label;
import info.rosetto.models.base.scenario.Unit;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.frows.lilex.token.Token;

/**
 * 単一のシナリオを生成するパーサー.
 * 一度のパース実行中にのみ生成され、パース終了後に破棄される.
 * @author tohhy
 */
public class ParserState {
    /**
     * パーサーが保持するユニットやラベルのリスト.
     */
    private final List<Token> tokens = new LinkedList<Token>();
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
     * 指定されたユニットをユニットリストに追加する.
     */
    public void addUnit(Unit unit) {
        tokens.add(unit);
        unitIndex++;
    }
    
    public void addLabel(Label label) {
        tokens.add(label);
    }
    
    /**
     * パーサーが保持するユニットやラベルのリストを読み取り専用で返す.
     * @return
     */
    protected List<Token> getTokens() {
        return Collections.unmodifiableList(tokens);
    }

    public Unit getParsingUnit() {
        return parsingUnit;
    }

    public void setCurrentUnit(Unit currentUnit) {
        this.parsingUnit = currentUnit;
    }

    public int getUnitIndex() {
        return unitIndex;
    }
}
