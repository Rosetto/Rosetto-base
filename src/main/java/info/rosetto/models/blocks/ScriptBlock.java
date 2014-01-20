/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.blocks;

import java.util.UUID;

/**
 * 埋め込みスクリプトを格納するブロック.
 * 埋め込みスクリプト中の記述はRosettoとは処理体系が異なりエスケープが困難なため、
 * ブロックに文字列として記録した上でユニークなIDを持たせ、専用タグで読み出して使用する.
 * @author tohhy
 */
public class ScriptBlock extends Block {
    private static final long serialVersionUID = 3072405694550063810L;
    
    /**
     * このスクリプトブロックのユニークなID.
     */
    private final String uid;
    
    /**
     * 指定したスクリプトを格納するスクリプトブロックを作成する.
     * @param script 格納するスクリプト
     */
    public ScriptBlock(String script) {
        super(script);
        //この実装なら重複確率は無視していいレベルまで低くなるので当面このまま.
        this.uid = UUID.randomUUID().toString();
    }
    
    /**
     * このスクリプトブロックのユニークなIDを取得する.
     * @return このスクリプトブロックのユニークなID
     */
    public String getUid() {
        return uid;
    }
}
