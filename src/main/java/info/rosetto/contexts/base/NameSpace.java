package info.rosetto.contexts.base;

import info.rosetto.models.base.values.RosettoValue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author tohhy
 *
 */
public class NameSpace {
    private final Map<String, RosettoValue> variables = new HashMap<String, RosettoValue>();
    
    /**
     * ロックされた変数名の一覧.<br>
     * ここに登録された変数名に対して再代入しようとするとエラーになる.<br>
     * 基本関数の名称等はここに登録され、誤って上書きされることによるバグを抑制する.
     */
    private final Set<String> sealedKeys = new HashSet<String>();
    
    private final String spacePath;
    
    public NameSpace(String spacePath) {
        this.spacePath = spacePath;
    }
    
    public RosettoValue get(String key) {
        return variables.get(key);
    }
    
    public void put(String key, RosettoValue value) {
        variables.put(key, value);
    }
}
