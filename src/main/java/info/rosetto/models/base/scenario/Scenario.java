/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.models.base.scenario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.concurrent.Immutable;

import org.frows.lilex.token.Token;

/**
 * １つのシナリオを保持するモデル.
 * イミュータブル.
 * @author tohhy
 */
@Immutable
public class Scenario implements Serializable {
    private static final long serialVersionUID = 2673278568820929562L;
    
    /**
     * このシナリオが保持するユニットのリスト.読み取り専用.
     */
    private final List<Unit> units;
    
    /**
     * このシナリオが保持するラベルのマップ.読み取り専用.
     */
    private final Map<String, Label> labels;
    
    
    /**
     * 指定したトークンのリストでこのシナリオを初期化する.
     * トークンのリストはラベルとユニットのリストに振り分けられて格納される.
     * @param tokens シナリオに追加するトークンのリスト
     */
    public Scenario(Token... tokens) {
        this(Arrays.asList((tokens == null) ? new Token[0] : tokens));
    }
    
    /**
     * 指定したトークンのリストとシナリオタイプでこのシナリオを初期化する.
     * トークンのリストはラベルとユニットのリストに振り分けられて格納される.
     * @param units シナリオに追加するトークンのリスト
     */
    public Scenario(List<? extends Token> tokens) {
        this.units = (tokens != null) ? 
                Collections.unmodifiableList(createUnitList(tokens)) : 
                Collections.unmodifiableList(new ArrayList<Unit>());
        this.labels = (tokens != null) ? 
                Collections.unmodifiableMap(createLabelMap(tokens)) : 
                Collections.unmodifiableMap(new HashMap<String, Label>());
    }
    
    /**
     * 指定したユニットとラベルのリストとシナリオタイプでこのシナリオを初期化する.
     * @param units シナリオに追加するユニットのリスト
     * @param labels シナリオに追加するラベルのリスト
     */
    public Scenario(List<Unit> units, List<Label> labels) {
        this.units = (units != null) ? 
                Collections.unmodifiableList(units) : 
                Collections.unmodifiableList(new ArrayList<Unit>());
        this.labels = (labels != null) ? 
                Collections.unmodifiableMap(createLabelMap(labels)) : 
                Collections.unmodifiableMap(new HashMap<String, Label>());
    }
    
    private static List<Unit> createUnitList(List<? extends Token> tokens) {
        List<Unit> result = new ArrayList<Unit>();
        for(Token t : tokens) {
            if(t instanceof Unit) {
                result.add((Unit)t);
            }
        }
        return result;
    }
    
    private static Map<String, Label> createLabelMap(List<? extends Token> tokens) {
        HashMap<String, Label> result = new HashMap<String, Label>();
        for(Token t : tokens) {
            if(t instanceof Label) {
                Label l = (Label)t;
                result.put(l.getName(), l);
            }
        }
        return result;
    }
    
    /**
     * このページが保持するユニット一覧の文字列表現を返す.
     */
    @Override
    public String toString() {
        return units.toString();
    }
    
    /**
     * このページが保持するユニット数を返す.
     * getUnits().lengthと同じ.
     * @return このページが保持するユニット数
     */
    public int getLength() {
        return units.size();
    }
    
    /**
     * 指定インデックスが所属するラベルを返す.
     * @param index 取得するラベルが所属するインデックス
     * @return 指定インデックスが所属するラベル
     */
    public Label getLabelAt(int index) {
        Label selected = null;
        int selectedIndex = Integer.MIN_VALUE;
        for(Label l : labels.values()) {
            if(l.getIndex() <= index && l.getIndex() > selectedIndex) {
                selected = l;
                selectedIndex = l.getIndex();
            }
        }
        return selected;
    }
    
    /**
     * 指定インデックスに存在するユニットを返す.
     * 指定インデックスにユニットが存在しなければnull.
     * @param index 取得するユニットのインデックス
     * @return 指定インデックスに存在するユニット
     */
    public Unit getUnitAt(int index) {
        if(index < 0 || index >= units.size()) return null;
        return units.get(index);
    }
    
    /**
     * このシナリオが終端ユニットのみを含む空シナリオかどうかを返す.
     * @return このシナリオが空かどうか
     */
    public boolean isEmpty() {
        return units.isEmpty();
    }

    /**
     * このページが保持するユニットのリストを返す. 読み取り専用.
     * @return このページが保持するユニットのリスト
     */
    public List<Unit> getUnits() {
        return units;
    }
    
    /**
     * このシナリオが保持するラベルのマップを返す. 読み取り専用.
     * @return labels このシナリオが保持するラベルのマップ
     */
    public Map<String, Label> getLabels() {
        return labels;
    }
    
}
