/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.parsers;

import java.util.List;

import info.rosetto.models.base.values.RosettoValue;

/**
 * タグそのもの、あるいはタグ中に現れる任意の要素をRosettoValueに変換するためのパーサー.<br>
 * @author tohhy
 */
public abstract class AbstractElementParser {

    /**
     * 与えられた文字列をRosettoValueに変換する.<br>
     * ネストされたActionCallがある場合、ActionCallには変換されるが評価されて展開はされない.
     * @param element
     * @return
     */
    public abstract RosettoValue parseElement(String element);
    
    /**
     * スペース区切りの要素の連なりを括弧やクオートを考慮してリストに分解する.
     * @param elements
     * @return
     */
    public abstract List<String> splitElements(String elements);
}
