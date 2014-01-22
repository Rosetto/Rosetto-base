/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.contexts.base;

import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.utils.base.RosettoLogger;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Rosetto中の単一の名前空間を表現するクラス.<br>
 * 名前空間直下に直接保持する変数と、requireにより参照可能になっている絶対参照変数の二種類のMapを保持する.<br>
 * 特定変数名に対する再代入不可フラグをもつ.
 * @author tohhy
 */
public class NameSpace implements Serializable {
    private static final long serialVersionUID = 5454239542524847421L;

    /**
     * この名前空間の名称.
     */
    private final String name;
    
    /**
     * この名前空間が直接保持する変数の一覧.
     */
    private final Map<String, RosettoValue> variables = new HashMap<String, RosettoValue>();
    
    /**
     * この名前空間がパッケージをrequireしたことにより、パッケージ付き名称で保持されている変数の一覧.
     */
    private final Map<String, RosettoValue> absoluteVariables = new HashMap<String, RosettoValue>();
    
    /**
     * ロックされた変数名の一覧.<br>
     * ここに登録された変数名に対して再代入しようとするとエラーになる.<br>
     * 基本関数等はここに登録され、誤って上書きされることによるバグを抑制する.
     */
    private final Set<String> sealedKeys = new HashSet<String>();
    
    /**
     * 指定名の名前空間オブジェクトを作成する.<br>
     * コンストラクタで作成しただけでは登録されない.<br>
     * WholeSpaceに追加して初めてゲーム上で利用可能になる.
     * @param name 作成する名前空間の完全名
     */
    public NameSpace(String name) {
        if(name == null || name.length() == 0)
            throw new IllegalArgumentException("name must not be empty");
        this.name = name;
    }
    
    /**
     * 指定名の変数を取得する.<br>
     * 指定名の変数が存在しない場合、キーにnullを指定した場合はnullが返る.
     * @param key 取得する変数名
     * @return 指定名の変数の値
     */
    public RosettoValue get(String key) {
        if(key == null) return null;
        if(variables.containsKey(key))
            return variables.get(key);
        if(absoluteVariables.containsKey(key))
            return absoluteVariables.get(key);
        return null;
    }
    
    /**
     * 指定した変数名に指定した値を設定する.<br>
     * 変数名にnull, 空文字, '.' を含む文字列を指定した場合はIllegalArgumentExceptionが返る.<br>
     * また、変数の値にnullを指定した場合もIllegalArgumentExceptionが返る.<br>
     * (明示的にnullを示す値を指定したい場合はmodels.base.values.NullValueを与える必要がある)
     * @param key 設定する変数名
     * @param value 設定する値
     */
    public void put(String key, RosettoValue value) {
        if(key == null || key.length() == 0)
            throw new IllegalArgumentException("key must not be empty");
        if(value == null)
            throw new IllegalArgumentException("value must not be null");
        if(key.contains("."))
            throw new IllegalArgumentException("variable name can't contain dot char");
        if(isSealed(key)) {
            RosettoLogger.warning("specified key " + key + " is sealed");
            return;
        }
        variables.put(key, value);
    }
    
    /**
     * 外部パッケージの絶対参照をこの名前空間に追加する.<br>
     * 
     * @param key
     * @param value
     */
    private void putAbsolute(String key, RosettoValue value) {
        if(!key.contains("."))
            throw new IllegalArgumentException("key must contain at least one dots");
        if(isSealed(key)) {
            RosettoLogger.warning("specified key " + key + " is sealed");
            return;
        }
        absoluteVariables.put(key, value);
    }
    
    public void use(NameSpace space) {
        for(Entry<String, RosettoValue> e : space.variables.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }
    
    public void require(NameSpace space) {
        for(Entry<String, RosettoValue> e : space.variables.entrySet()) {
            putAbsolute(e.getKey(), e.getValue());
        }
    }
    
    public boolean isSealed(String key) {
        return sealedKeys.contains(key);
    }
    
    public void seal(String key) {
        sealedKeys.add(key);
    }
    
    public void unSeal(String key) {
        sealedKeys.remove(key);
    }

    public String getName() {
        return name;
    }
    
}
