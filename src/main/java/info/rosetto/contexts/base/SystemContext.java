/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.contexts.base;

import info.rosetto.models.base.engine.EngineModel;
import info.rosetto.models.base.parser.ParserModel;

/**
 * Rosettoのシステム状態を示すインスタンス.
 * @author tohhy
 */
public class SystemContext {
    
    /**
     * このコンテキストでスクリプト解釈に使用されるパーサー.
     */
    private ParserModel parser;
    
    /**
     * このコンテキストでのエンジンの設定等.
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
    public ParserModel getParser() {
        return parser;
    }
    
    /**
     * システムが利用するパーサーのインスタンスを指定する.
     * @param parser システムが利用するパーサーのインスタンス
     */
    public void setParser(ParserModel parser) {
        this.parser = parser;
    }

    /**
     * 
     * @return
     */
    public EngineModel getEngine() {
        return engine;
    }

    /**
     * 
     * @param engine
     */
    public void setEngine(EngineModel engine) {
        this.engine = engine;
    }

}
