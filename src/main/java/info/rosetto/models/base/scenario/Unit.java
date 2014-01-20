/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.models.base.scenario;

import info.rosetto.models.base.function.FunctionCall;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.frows.lilex.token.Token;

/**
 * 前のタグの終わりから次のタグの終わりまでの間を表すシナリオ中の基本単位.
 * @author tohhy
 */
@Immutable
public class Unit implements Serializable, Token {
    private static final long serialVersionUID = 4445760452271476373L;
    
    /**
     * このユニットが保持するテキスト.
     */
    @Nonnull
    private final String text;
    /**
     * このユニットが保持する関数呼び出し.
     */
    @Nonnull
    private final FunctionCall action;
    
    /**
     * textを指定してUnitを初期化する.<br>
     * actionにはRosettoAction.EMPTYが保持される.<br>
     * textにnullを与えた場合は空文字が保持される.
     * @param text このUnitが保持する文章
     */
    public Unit(String text) {
        this(text, null);
    }
    
    /**
     * textとactionを指定してUnitを初期化する.<br>
     * textにnullを与えた場合は空文字が保持される.<br>
     * actionにnullを与えた場合はRosettoAction.EMPTYが保持される.
     * @param text このUnitが保持する文章
     * @param action このUnitが保持するアクション
     */
    public Unit(String text, FunctionCall action) {
        this.text = (text != null) ? text : "";
        this.action = (action != null) ? action : FunctionCall.EMPTY;
    }
    
    /**
     * このユニットが保持するテキストを返す.<br>
     * nullにはならないことが保証される.生成時にnullを与えた場合は空文字が返る.
     * @return このユニットが保持するテキスト
     */
    public String getContent() {
        return text;
    }
    
    /**
     * このユニットが保持するアクションを返す.<br>
     * nullにはならないことが保証される.生成時にnullを与えた場合はRosettoAction.EMPTYが返る.
     * @return このユニットが保持するアクション
     */
    public FunctionCall getAction() {
        return action;
    }
    
    /**
     * textとaction.typeの文字列表現を返す.
     * @return textとaction.typeの文字列表現
     */
    @Override
    public String toString() {
        String str = getContent();
        String function = action.getFunctionName();
        return new StringBuilder()
        .append("[text=").append(str).append(", action=").append(function).append("]")
        .toString();
    }
}
