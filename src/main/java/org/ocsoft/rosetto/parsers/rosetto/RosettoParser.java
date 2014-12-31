/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.parsers.rosetto;

import org.ocsoft.rosetto.parsers.ScenarioParser;

/**
 * RosettoScriptからシナリオオブジェクトを作成するパーサー.
 * @author tohhy
 */
public class RosettoParser extends ScenarioParser {
    /**
     * 生成したマクロの追加先を指定してパーサーを初期化する.
     * @param toAdd パースによって生成されたマクロを追加するマップ.
     */
    public RosettoParser() {
        super(new RosettoNormalizer());
    }
}