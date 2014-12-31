/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.rosetto.system.exceptions;

import org.rosetto.models.base.elements.values.RosettoFunction;
import org.rosetto.models.system.Scope;
import org.rosetto.system.messages.SystemMessage;
import org.rosetto.system.messages.SystemMessages;

/**
 * 
 * @author tohhy
 *
 */
public class WrongTypeArgumentException extends RosettoException {
    private static final long serialVersionUID  = -8495406859808098415L;
    
    public WrongTypeArgumentException() {
        super();
    }
    
    public WrongTypeArgumentException(RosettoFunction function, Scope args) {
        super(SystemMessages.get(SystemMessage.E8000_WRONG_TYPE_ARGUMENT) + 
                function.getArguments() + " <- " + args);
    }
    
}
