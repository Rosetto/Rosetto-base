/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
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
    
    /**
     * 現在アクティブな名前空間.
     */
    private NameSpace currentNameSpace;
    
    public WholeSpace() {
        currentNameSpace = create("story");
    }
    
    public NameSpace getNameSpace(String name) {
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
    
    public int getCreatedNameSpaceCount() {
        return nameSpaces.size();
    }

    public NameSpace getCurrentNameSpace() {
        return currentNameSpace;
    }

    public void setNameSpaceAsCurrent(String name) {
        if(name == null || name.length() == 0)
            throw new IllegalArgumentException("name must not be empty");
        currentNameSpace = contains(name) ? 
                getNameSpace(name) : create(name);
    }

}
