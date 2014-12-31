/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.parsers.rosetto;

import org.frows.lilex.designator.Designators;
import org.frows.lilex.designator.HeadDesignator;
import org.frows.lilex.designator.MiddleDesignator;
import org.frows.lilex.designator.TailDesignator;
import org.ocsoft.rosetto.parsers.StdDesignators;

/**
 * 
 * @author tohhy
 */
public class RosettoDesignators extends Designators {
    
    /**
     * コメント指示子.
     */
    static final MiddleDesignator COMMENT_D = StdDesignators.createMiddleComment(";");
    
    /**
     * ラベル指示子.
     */
    static final HeadDesignator LABEL_D = StdDesignators.createHeadLabel("*", ' ');
    /**
     * ページング指示子.
     */
    static final HeadDesignator PAGING_D = StdDesignators.createHeadReplace("-", "[p]");
    /**
     * キャラクター選択指示子
     */
    static final HeadDesignator CHARA_SELECT_D = 
            StdDesignators.createHeadConvertTag("@", "actor.select");
    /**
     * キャラクター操作指示子
     */
    static final HeadDesignator CHARA_EDIT_D = 
            StdDesignators.createHeadPackageCall("#", "actor.current");
    
    
    /**
     * 無改行指示子
     */
    static final TailDesignator NO_LF_D = 
            StdDesignators.createTailAddition("-", "");
    /**
     * 改行指示子
     */
    static final TailDesignator NO_W_D = 
            StdDesignators.createTailAddition("/", "[lf]");

    public RosettoDesignators() {
        super(
                COMMENT_D,
                LABEL_D, PAGING_D, CHARA_SELECT_D, CHARA_EDIT_D, 
                NO_LF_D, NO_W_D);
    }
    
}
