/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.rosetto.observers;

/**
 * 
 * @author tohhy
 */
public class RosettoObservatories {
    
    /**
     * 
     */
    private static RosettoObservatories instance;
    
    /**
     * 
     */
    private final ActionObservatory action = new ActionObservatory();
    
    /**
     * 
     */
    private final NameSpaceObservatory nameSpace = new NameSpaceObservatory();
    
    /**
     * 
     */
    private final VariableObservatory variable = new VariableObservatory();
    
    /**
     * 
     * @return
     */
    private static RosettoObservatories getInstance() {
        if(instance == null) {
            instance = new RosettoObservatories();
        }
        return instance;
    }
    
    /**
     * 
     */
    public static void clear() {
        instance = new RosettoObservatories();
    }
    
    /**
     * 
     * @return
     */
    public static ActionObservatory getAction() {
        return getInstance().action;
    }
    
    /**
     * 
     * @return
     */
    public static NameSpaceObservatory getNameSpace() {
        return getInstance().nameSpace;
    }
    
    /**
     * 
     * @return
     */
    public static VariableObservatory getVariable() {
        return getInstance().variable;
    }
    
}
