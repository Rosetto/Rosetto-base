/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.contexts.base;

import info.rosetto.models.base.parser.RosettoParser;

/**
 * Rosettoのシステム状態を示すインスタンス.
 * @author tohhy
 */
public class SystemContext {
    
    /**
     * このコンテキストでスクリプト解釈に使用されるパーサー.
     */
    private RosettoParser parser;
    
    /**
     * パッケージ内でのみ生成.
     */
    SystemContext() {}
    
    /**
     * システムが現在利用しているパーサーのインスタンスを取得する.
     * @return システムが現在利用しているパーサーのインスタンス
     */
    public RosettoParser getParser() {
        return parser;
    }
    
    /**
     * システムが利用するパーサーのインスタンスを指定する.
     * @param parser システムが利用するパーサーのインスタンス
     */
    public void setParser(RosettoParser parser) {
        this.parser = parser;
    }

}
