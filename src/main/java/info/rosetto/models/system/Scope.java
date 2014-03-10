/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.system;

import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.values.ListValue;
import info.rosetto.models.base.elements.values.RosettoFunction;
import info.rosetto.utils.base.Values;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * ローカル変数を保持するスコープ.
 * @author tohhy
 */
public class Scope {
    
    /**
     * このスコープが保持するローカル変数の一覧.
     */
    private final Map<String, RosettoValue> vars;
    
    /**
     * このスコープの親スコープ.
     */
    private final Scope parent;
    
    /**
     * 新しいルートスコープを生成する.
     */
    public Scope() {
        this.vars = new HashMap<String, RosettoValue>();
        this.parent = null;
    }
    
    /**
     * 指定スコープを親とする新しいスコープを生成する.
     * @param parent 生成するスコープの親スコープ
     */
    public Scope(Scope parent) {
        this.vars = new HashMap<String, RosettoValue>();
        this.parent = parent;
    }
    
    /**
     * 指定したローカル変数マッピングをもつ新しいスコープを生成する.
     * @param parent 生成するスコープの親スコープ、ルートスコープならnull
     * @param values このスコープに与えるローカル変数
     */
    public Scope(Scope parent, Map<String, RosettoValue> values) {
        this.vars = new HashMap<String, RosettoValue>(values);
        this.parent = parent;
    }
    
    /**
     * 未評価の引数と関数を取り、関数に対して実行時引数として与えるスコープを生成する.<br>
     * インスタンス化のためには引数として与える先の関数が必要になる.<br>
     * 関数の持つ引数リストと照らし合わせ、適切に引数を割り振ることで生成される.<br>
     * すべてのActionCallは展開された上で格納される.
     * @param args 未評価の引数
     * @param func 実行する関数
     */
    public Scope(ListValue args, RosettoFunction func, Scope parent) {
        Map<String, RosettoValue> parsed = args.bind(func, parent);
        this.vars = new HashMap<String, RosettoValue>(parsed);
        this.parent = parent;
    }
    
    @Override
    public String toString() {
        return vars.toString();
    }

    /**
     * このスコープが親を持っているかどうかを返す.
     * @return このスコープが親を持っているかどうか
     */
    public boolean hasParent() {
        return parent != null;
    }
    
    /**
     * このスコープの親スコープを返す.
     * @return このスコープの親スコープ
     */
    public Scope getParent() {
        return parent;
    }
    
    /**
     * 指定キーにマッピングされた値を取得する.
     * 指定キーが存在しない場合はValues.NULLが返る.
     * @param key 参照するキー
     * @return 指定キーにマッピングされた値
     */
    public RosettoValue get(String key) {
        if(vars.containsKey(key)) return vars.get(key);
        if(hasParent()) return parent.get(key);
        return Values.NULL;
    }
    
    /**
     * このスコープに新しくローカル変数をセットする.
     * @param key セットする変数のキー
     * @param value セットする変数の値
     */
    public void set(String key, RosettoValue value) {
        this.vars.put(key, value);
    }
    
    /**
     * 指定キーがこの引数マップに含まれているかを取得する.
     * @param key 参照するキー
     * @return キーが存在するかどうか
     */
    public boolean containsKey(String key) {
        if(vars.containsKey(key)) return true;
        if(hasParent()) return parent.containsKey(key);
        return false;
    }
    
    /**
     * キーワード引数のマップを取得する.読み込み専用.
     * @return キーワード引数のマップ
     */
    public Map<String, RosettoValue> getMap() {
        return Collections.unmodifiableMap(vars);
    }

}
