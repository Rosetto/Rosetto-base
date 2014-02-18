/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.system.messages;

/**
 * 言語ごとのシステムメッセージを提供する.
 * @author tohhy
 */
public class SystemMessages {
    
    /**
     * シングルトンインスタンス.
     */
    private static SystemMessages instance = new SystemMessages();
    
    /**
     * メッセージ定義のインスタンス.
     */
    private final SystemMessagesJA errorMessages = SystemMessagesJA.getInstance();
    
    /**
     * 指定したメッセージコードに対応するメッセージを取得する.
     * @param messageCode
     * @return
     */
    public static String get(int messageCode) {
        String s = instance.errorMessages.get(messageCode);
        return (s != null) ? s : "[sys] code = " + messageCode;
    }
    
    /**
     * 指定したメッセージ列挙子に対応するメッセージを取得する.
     * @param message
     * @return
     */
    public static String get(SystemMessage message) {
        String s = instance.errorMessages.get(message.getMessageCode());
        return (s != null) ? s : "[sys]" + message.name();
    }

}
