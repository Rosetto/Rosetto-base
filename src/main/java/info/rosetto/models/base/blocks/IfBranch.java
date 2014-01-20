/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.blocks;

import javax.annotation.concurrent.Immutable;

/**
 * if文における単一の分岐を表すブロック.
 * @author tohhy
 */
@Immutable
public class IfBranch extends Block {
    private static final long serialVersionUID = -4892178641695555615L;
    
    /**
     * このブロックに付加された条件式.
     */
    private final String exp;
    
    /**
     * 条件式と内容を与えてこのifBranchを初期化する.
     * @param exp 条件式
     * @param body 内容
     */
    public IfBranch(String exp, String body) {
        super(body);
        if(exp == null) 
            throw new IllegalArgumentException("exp must not be null");
        this.exp = exp;
    }

    /**
     * このブロックに付加された条件式を取得する.
     * @return 条件式
     */
    public String getExp() {
        return exp;
    }
}
