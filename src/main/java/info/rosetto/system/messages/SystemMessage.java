/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.system.messages;

import java.util.HashMap;
import java.util.Map;

/**
 * システムで定義される各種の出力メッセージ.
 * @author tohhy
 */
public enum SystemMessage {
    E1000_GLOBAL_VAR_NOT_FOUND(1000),
    E1100_ACTION_NOT_FOUND(1100),
    
    E7000_NULL_ACTION_CALLED(7000),
    
    E8000_WRONG_TYPE_ARGUMENT(8000),
    
    ;
    private static final Map<Integer, SystemMessage> mapByCode = new HashMap<Integer, SystemMessage>();
    private final int messageCode;
    
    private SystemMessage(int messageCode) {
        this.messageCode = messageCode;
    }
    
    @Override
    public String toString() {
        return String.valueOf(messageCode);
    }
    
    public int getMessageCode() {
        return messageCode;
    }
    
    public static SystemMessage getByCode(int code) {
        return mapByCode.get(code);
    }

}
