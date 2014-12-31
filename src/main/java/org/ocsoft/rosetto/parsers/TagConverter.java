/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.parsers;

import org.ocsoft.rosetto.models.base.elements.values.ActionCall;

/**
 * 単一のタグを異なるタグに変換するコンバーター.<br>
 * Rosettoとは異なる他文法をパースする際、ElementParser内部でタグごとに作成して用いる.
 * @author tohhy
 */
public abstract class TagConverter {
    
    /**
     * 何らかの他文法のタグ名と属性を受け取り、RosettoのActionCallを生成して返す.
     * @param tagName 他文法のタグ名
     * @param attrs 他文法の属性
     * @return 生成したRosettoのActionCall
     */
    protected abstract ActionCall toRosettoTag(String tagName, String[] attrs);
    
}
