/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.contexts.base;

import info.rosetto.functions.base.BaseFunctions;

/**
 * Rosetto起動時のデフォルトの名前空間.<br>
 * 一般的なノベルゲームはすべてこの名前空間をcurrentとして作成されることを想定.
 * @author tohhy
 */
public class StorySpace extends NameSpace {
    private static final long serialVersionUID = 5500020782161550137L;
    
    
    /**
     * WholeSpaceからのみ生成.
     */
    StorySpace() {
        super("story");
    }

}
