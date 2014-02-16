/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.system.errors;

import java.util.HashMap;
import java.util.Map;

/**
 * 言語ごとのエラーメッセージを提供する.
 * @author tohhy
 */
public class ErrorMessages {
    
    private static ErrorMessages instance;
    
    private Map<Integer, String> errorMessanges = new HashMap<Integer, String>();
    
    public static ErrorMessages getInstance() {
        if(instance == null) {
            instance = new ErrorMessages();
        }
        return instance;
    }
    
    public static String get(int errorCode) {
        String s = instance.errorMessanges.get(errorCode);
        return (s != null) ? s : "[error] code = " + errorCode;
    }
    
    public static String get(RosettoError error) {
        String s = instance.errorMessanges.get(error.getErrorCode());
        return (s != null) ? s : "[error]" + error.name();
    }

}
