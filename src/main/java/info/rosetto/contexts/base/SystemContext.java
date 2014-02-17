/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.contexts.base;

import info.rosetto.models.state.engine.EngineModel;
import info.rosetto.models.state.parser.Parser;
import info.rosetto.parsers.rosetto.RosettoParser;

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
     * このコンテキストでのエンジンの設定等を格納したモデル.<br>
     * ファイルのロード方式等、プレイヤーの動作に関わる環境設定等を保持する.
     */
    private EngineModel engine;
    
    
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
     * システムが利用するエンジンのモデルを取得する.
     * @return システムが利用するエンジンのモデル
     */
    public EngineModel getEngine() {
        return engine;
    }
    
    /**
     * システムが利用するエンジンのモデルを指定する.
     * @param engine システムが利用するエンジンのモデル
     */
    public void setEngine(EngineModel engine) {
        this.engine = engine;
    }
    
}
