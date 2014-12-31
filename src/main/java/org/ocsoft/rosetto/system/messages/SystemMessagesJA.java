/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.system.messages;

import java.util.EnumMap;
import java.util.Map;

/**
 * 日本語のエラーメッセージ定義.<br>
 * エラーコードをキーとしたマップで定義する.
 * @author tohhy
 */
public class SystemMessagesJA {
    
    public SystemMessagesJA() {
        messages.put(SystemMessage.E1000_GLOBAL_VAR_NOT_FOUND, 
                "指定名のグローバル変数が見つかりません : ");
        messages.put(SystemMessage.E1100_ACTION_NOT_FOUND, 
                "指定名の関数またはマクロがコンテキスト中に見つかりません : ");
        messages.put(SystemMessage.E8000_WRONG_TYPE_ARGUMENT, 
                "引数の型が一致しません : ");
    }
    
    private static final SystemMessagesJA instance = new SystemMessagesJA();
    
    private final Map<SystemMessage, String> messages = 
            new EnumMap<SystemMessage, String>(SystemMessage.class);
    
    public static SystemMessagesJA getInstance() {
        return instance;
    }
    
    
    public String get(SystemMessage message) {
        return messages.get(message);
    }
}
