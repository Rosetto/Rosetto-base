/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.observers;

public class Observatories {
    
    private static Observatories instance;
    
    private final ActionObservatory action = new ActionObservatory();
    
    private final NameSpaceObservatory nameSpace = new NameSpaceObservatory();
    
    private final VariableObservatory variable = new VariableObservatory();
    
    private static Observatories getInstance() {
        if(instance == null) {
            instance = new Observatories();
        }
        return instance;
    }
    
    public static void clear() {
        instance = new Observatories();
    }
    
    public static ActionObservatory getAction() {
        return getInstance().action;
    }
    
    public static NameSpaceObservatory getNameSpace() {
        return getInstance().nameSpace;
    }
    
    
    public static VariableObservatory getVariable() {
        return getInstance().variable;
    }
    
}
