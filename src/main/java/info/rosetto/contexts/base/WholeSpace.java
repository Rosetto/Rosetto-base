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
    private static final WholeSpace instance = new WholeSpace();
    
    private final Map<String, NameSpace> nameSpaces = new HashMap<String, NameSpace>();
    
    private WholeSpace() {}
    
    public static NameSpace get(String name) {
        return instance.nameSpaces.get(name);
    }
    
    public static void create(String name) {
        instance.nameSpaces.put(name, new NameSpace(name));
    }

}
