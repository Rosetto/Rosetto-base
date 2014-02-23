/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.function;

import info.rosetto.models.base.elements.RosettoValue;

/**
 * 
 * @author tohhy
 *
 */
public abstract class LambdaFunction extends RosettoFunction {
    private static final long serialVersionUID = 1198484922087018955L;

    public LambdaFunction(RosettoValue args) {
        super("", args);
    }
    
    public LambdaFunction(String...args) {
        super("", args);
    }
    
    @Override
    public String toString() {
        
        return "[fn " + createArgsStr() + "]";
    }

}
