/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.contexts.base;

import org.ocsoft.rosetto.models.system.Parser;
import org.ocsoft.rosetto.models.system.ScenarioPlayer;
import org.ocsoft.rosetto.parsers.rosetto.RosettoParser;

/**
 * Rosettoのシステム状態を示すインスタンス.
 * @author tohhy
 */
public class SystemContext {
    
    /**
     * このコンテキストでスクリプト解釈に使用されるパーサー.
     */
    private Parser parser = new RosettoParser();
    
    /**
     * このコンテキストでシナリオ再生に使用されるプレイヤー.
     */
    private ScenarioPlayer player;
    
    /**
     * パッケージ内でのみ生成.
     */
    SystemContext() {}
    
    /**
     * システムが現在利用しているパーサーのインスタンスを取得する.
     * @return システムが現在利用しているパーサーのインスタンス
     */
    public Parser getParser() {
        return parser;
    }
    
    /**
     * システムが利用するパーサーのインスタンスを指定する.
     * @param parser システムが利用するパーサーのインスタンス
     */
    public void setParser(Parser parser) {
        this.parser = parser;
    }
    
    /**
     * システムが利用するシナリオプレイヤーのインスタンスを取得する.
     * @return システムが利用するシナリオプレイヤーのインスタンス
     */
    public ScenarioPlayer getPlayer() {
        return player;
    }
    
    /**
     * システムが利用するシナリオプレイヤーのインスタンスを指定する.
     * @param player システムが利用するシナリオプレイヤーのインスタンス
     */
    public void setPlayer(ScenarioPlayer player) {
        this.player = player;
    }
}
