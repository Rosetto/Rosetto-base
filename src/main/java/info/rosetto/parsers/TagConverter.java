/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.parsers;

import info.rosetto.models.base.values.ActionCall;
import info.rosetto.utils.base.ParserUtils;

/**
 * 単一のタグを異なるタグに変換するコンバーター.
 * TagParser内部でタグごとに作成して用いる.
 * @author tohhy
 */
public abstract class TagConverter {
    
    /**
     * 何らかの他文法のタグ名と属性を受け取り、RosettoActionを生成して返す.
     * @param tagName 他文法のタグ名
     * @param attrs 他文法の属性
     * @return 生成したRosettoAction
     */
    protected abstract ActionCall toRosettoTag(String tagName, String[] attrs);
    
    /**
     * 何らかの他文法のタグ名と属性を受け取り、RosettoActionを生成して返す.
     * @param tagName 他文法のタグ名
     * @param attrs 他文法の属性
     * @return 生成したRosettoAction
     */
    public ActionCall toRosettoTag(String tagName, String attrs) {
        return toRosettoTag(tagName, ParserUtils.splitStringArgs(attrs));
    }
}
