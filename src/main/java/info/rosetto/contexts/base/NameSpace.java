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
 * 
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
     * 作成しただけでは登録されない.<br>
     * WholeSpaceに追加して初めてゲーム上で利用可能になる.
     * @param name
     */
    public NameSpace(String name) {
        this.name = name;
    }
    
    public RosettoValue get(String key) {
        if(variables.containsKey(key))
            return variables.get(key);
        if(absoluteVariables.containsKey(key))
            return absoluteVariables.get(key);
        return null;
    }
    
    public void put(String key, RosettoValue value) {
        if(isSealed(key)) {
            RosettoLogger.warning("specified key " + key + " is sealed");
            return;
        }
        variables.put(key, value);
    }
    
    private void putAbsolute(String key, RosettoValue value) {
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
