/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.system.messages;


/**
 * システムで定義される各種の出力メッセージ.<br>
 * 例外処理等をログで処理する場合、メッセージはなるべく手書きせずに定義したメッセージを用いる.<br>
 * 言語ごとにローカライズしたメッセージを用意するようにする.
 * @author tohhy
 */
public enum SystemMessage {
    E1000_GLOBAL_VAR_NOT_FOUND(1000),
    E1100_ACTION_NOT_FOUND(1100),
    
    E7000_NULL_ACTION_CALLED(7000),
    E7100_VOID_ACTION_CALLED(7100),
    
    E8000_WRONG_TYPE_ARGUMENT(8000),
    
    S11000_FUNCTION_EXECUTED(11000),
    S11100_MACRO_EXECUTED(11100),
    
    ;
    /**
     * 
     */
    private final int messageCode;
    
    /**
     * 指定したメッセージコードをもつメッセージを生成する.
     * @param messageCode
     */
    private SystemMessage(int messageCode) {
        this.messageCode = messageCode;
    }
    
    /**
     * このメッセージのメッセージコードを取得する.
     * @return このメッセージのメッセージコード
     */
    public int getMessageCode() {
        return messageCode;
    }
    
}
