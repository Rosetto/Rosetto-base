/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.blocks;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;

import org.frows.lilex.token.Token;

/**
 * スクリプト中の一部分を格納するブロック.
 * 未解釈のシナリオオブジェクトやスクリプトなどを格納する.
 * スクリプト中ではリテラルとして扱われる要素なので、Tokenを継承している.
 * @author tohhy
 */
@Immutable
public abstract class Block implements Serializable, Token {
    private static final long serialVersionUID = -8715582992836803590L;
    
    private final String text;
    
    /**
     * 格納するテキストを指定してこのブロックを初期化する.
     * @param text 格納するテキスト
     */
    public Block(String text) {
        if(text == null) 
            throw new IllegalArgumentException("text must not be null");
        this.text = text;
    }

    public String getContent() {
        return text;
    }
}
