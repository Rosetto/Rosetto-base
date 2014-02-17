/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.state.variables;

import info.rosetto.models.base.function.RosettoArguments;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.utils.base.Values;

import java.util.HashMap;
import java.util.Map;

/**
 * ローカル変数を保持するスコープ.<br>
 * 
 * すべての値は評価済みで、ActionCallは含まれない.<br>
 * @author tohhy
 */
public class Scope {
    
    private final Map<String, RosettoValue> vars;
    
    private final Scope parent;
    
    
    public Scope() {
        this.vars = new HashMap<String, RosettoValue>();
        this.parent = null;
    }
    
    public Scope(Scope parent) {
        this.vars = new HashMap<String, RosettoValue>();
        this.parent = parent;
    }
    
    /**
     * 未評価の引数と関数を取り、関数に対して実行時引数として与えるスコープを生成する.<br>
     * インスタンス化のためには引数として与える先の関数が必要になる.<br>
     * 関数の持つ引数リストと照らし合わせ、適切に引数を割り振ることで生成される.
     * @param args 未評価の引数
     * @param func 実行する関数
     */
    public Scope(RosettoArguments args, RosettoFunction func, Scope parent) {
        Map<String, RosettoValue> parsed = args.parse(func, parent);
        this.vars = new HashMap<String, RosettoValue>(parsed);
        this.parent = parent;
    }

    public boolean hasParent() {
        return parent != null;
    }
    
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
    
    @Override
    public String toString() {
        return vars.toString();
    }
    
    
    /**
     * キーワード引数のマップを取得する.読み込み専用.
     * @return
     */
    public Map<String, RosettoValue> getMap() {
        return vars;
    }

}
