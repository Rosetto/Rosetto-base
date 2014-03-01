/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.system.messages;

import java.util.HashMap;
import java.util.Map;

/**
 * 言語ごとのシステムメッセージを提供する.
 * @author tohhy
 */
public class SystemMessages {
    
    /**
     * シングルトンインスタンス.
     */
    private static SystemMessages instance;
    
    /**
     * 
     */
    private static final Map<Integer, SystemMessage> mapByCode = 
            new HashMap<Integer, SystemMessage>();
    
    /**
     * メッセージ定義のインスタンス.
     */
    private final SystemMessagesJA localizedMessages = SystemMessagesJA.getInstance();
    
    public SystemMessages() {
        for(SystemMessage m : SystemMessage.values()) {
            mapByCode.put(m.getMessageCode(), m);
        }
    }
    
    /**
     * 指定したメッセージコードに対応するメッセージを取得する.
     * @param messageCode
     * @return
     */
    public static String get(int messageCode) {
        String s = getInstance().localizedMessages.get(mapByCode.get(messageCode));
        return (s != null) ? s : "[sys] code = " + messageCode;
    }
    
    /**
     * 指定したメッセージ列挙子に対応するメッセージを取得する.
     * @param message
     * @return
     */
    public static String get(SystemMessage message) {
        String s = getInstance().localizedMessages.get(message);
        return (s != null) ? s : "[sys]" + message.name();
    }
    
    /**
     * メッセージコードからメッセージのインスタンスを取得する.
     * @param code メッセージコード
     * @return 取得したインスタンス、一致するものがなければnull
     */
    public static SystemMessage getByCode(int code) {
        return mapByCode.get(code);
    }
    
    private static SystemMessages getInstance() {
        if(instance ==  null)
            instance = new SystemMessages();
        return instance;
    }
    
}
