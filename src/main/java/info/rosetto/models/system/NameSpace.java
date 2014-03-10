/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.system;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.observers.Observatories;
import info.rosetto.system.RosettoLogger;
import info.rosetto.system.exceptions.VariableSealedException;
import info.rosetto.utils.base.Values;

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
        if(name.endsWith("."))
            throw new IllegalArgumentException("name must not end with dot");
        this.name = name;
    }
    
    /**
     * rootの名前空間作成用.
     */
    private NameSpace() {
        this.name = "";
    }
    
    /**
     * ルート用の特殊な名前空間を生成する.nameは空文字になる.
     * @return ルートの名前空間
     */
    public static NameSpace createRootSpace() {
        return new NameSpace();
    }
    
    @Override
    public String toString() {
        return "[namespace:" + name + 
                " variables:" + variables + 
                " sealed:" + sealedKeys + "]";
    }
    
    /**
     * 指定名の変数を取得する.<br>
     * 指定名の変数が存在しない場合、キーにnullを指定した場合はNullValueが返る.
     * @param key 取得する変数名
     * @return 指定名の変数の値
     */
    public RosettoValue get(String key) {
        if(key == null) return Values.NULL;
        
        if(!key.contains(".")) {
            return variables.containsKey(key) ? variables.get(key) : Values.NULL;
        }
        String nameSpace = this.name + "." + key.substring(0, key.lastIndexOf("."));
        String varName = key.substring(key.lastIndexOf(".") + 1);
        return Contexts.getVariableContext().get(nameSpace, varName);
    }
    
    /**
     * 指定した変数名に指定した値を設定する.<br>
     * 変数名にnull, 空文字, '.' を含む文字列を指定した場合はIllegalArgumentExceptionが返る.<br>
     * また、変数の値にnullを指定した場合もIllegalArgumentExceptionが返る.<br>
     * (明示的にnullを示す値を指定したい場合はmodels.base.values.NullValueを与える必要がある)
     * @param key 設定する変数名
     * @param value 設定する値
     */
    public void define(String key, RosettoValue value) {
        if(key == null || key.length() == 0)
            throw new IllegalArgumentException("key must not be empty");
        if(value == null)
            throw new IllegalArgumentException("value must not be null");
        if(key.contains("."))
            throw new IllegalArgumentException("variable name can't contain dot char");
        if(isSealed(key)) {
            throw new VariableSealedException("specified key " + key + " is sealed");
        }
        variables.put(key, value);
        Observatories.getVariable().valueChanged(this.name, key, value);
    }
    
    /**
     * 指定した名前空間の内容をこの名前空間にコピーして取り込み、読み出せるようにする.
     * @param space 取り込む名前空間
     */
    public void include(NameSpace space) {
        if(space == null)
            throw new IllegalArgumentException("namespace is null");
        
        for(Entry<String, RosettoValue> e : space.variables.entrySet()) {
            try {
                define(e.getKey(), e.getValue());
            } catch(VariableSealedException ex) {
                RosettoLogger.warning("variable" + e.getKey() + " include failed: "
                        + "key already sealed");
            }
            //シールされているキーをincludeした場合このパッケージでもseal
            if(space.isSealed(e.getKey())) seal(e.getKey());
        }
    }
    
    /**
     * 指定した変数がロックされているかどうかを取得する.
     * @param key 対象とする変数名
     * @return 変数がロックされているかどうか
     */
    public boolean isSealed(String key) {
        return sealedKeys.contains(key);
    }
    
    /**
     * 指定した変数をロックし、編集不能にする.
     * @param key ロックする変数名
     */
    public void seal(String key) {
        sealedKeys.add(key);
    }
    
    /**
     * 指定した変数のロックを解除し、編集可能にする.
     * @param key ロックを解除する変数名
     */
    public void unSeal(String key) {
        sealedKeys.remove(key);
    }
    
    /**
     * この名前空間の完全名を取得する.
     * @return この名前空間の完全名
     */
    public String getName() {
        return name;
    }
}
