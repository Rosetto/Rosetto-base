/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.system.errors;

import java.util.EnumMap;
import java.util.Map;

/**
 * 日本語のエラーメッセージ定義.<br>
 * エラーコードをキーとしたマップで定義する.
 * @author tohhy
 */
public class ErrorMessagesJA {
    
    public ErrorMessagesJA() {
        messages.put(RosettoError.E1000_GLOBAL_VAR_NOT_FOUND, 
                "指定名のグローバル変数が見つかりません : ");
        messages.put(RosettoError.E1100_ACTION_NOT_FOUND, 
                "指定名の関数またはマクロがコンテキスト中に見つかりません : ");
    }
    
    private static final ErrorMessagesJA instance = new ErrorMessagesJA();
    
    private final Map<RosettoError, String> messages = 
            new EnumMap<RosettoError, String>(RosettoError.class);
    
    public static ErrorMessagesJA getInstance() {
        return instance;
    }
    
    public String get(int code) {
        return messages.get(RosettoError.getByCode(code));
    }
    
    public String get(RosettoError error) {
        return messages.get(error);
    }
}
