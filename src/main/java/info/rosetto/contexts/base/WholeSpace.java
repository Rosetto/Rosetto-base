package info.rosetto.contexts.base;

import java.util.HashMap;
import java.util.Map;

/**
 * Rosettoが認識している全ての名前空間.<br>
 * 実質的にRosettoが認識する全てのゲーム状態と等しくなる.<br>
 * WholeSpaceをシリアライズすることでゲーム上の状態が完全に保存できるように実装する.
 * @author tohhy
 */
public class WholeSpace {
    
    private final Map<String, NameSpace> nameSpaces = new HashMap<String, NameSpace>();
    
    public WholeSpace() {}
    
    public NameSpace get(String name) {
        return nameSpaces.get(name);
    }
    
    public NameSpace create(String name) {
        NameSpace space = new NameSpace(name);
        nameSpaces.put(name, space);
        return space;
    }
    
    public boolean contains(String name) {
        return nameSpaces.containsKey(name);
    }

}
