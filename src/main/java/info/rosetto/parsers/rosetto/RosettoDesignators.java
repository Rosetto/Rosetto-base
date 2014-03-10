/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.parsers.rosetto;

import info.rosetto.parsers.StdDesignators;

import org.frows.lilex.designator.Designators;
import org.frows.lilex.designator.HeadDesignator;
import org.frows.lilex.designator.MiddleDesignator;
import org.frows.lilex.designator.TailDesignator;

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
     * アクター操作指示子
     */
    static final HeadDesignator ACTOR_EDIT_D = new HeadDesignator("!") {
        
        @Override
        protected String processLine(String subLine) {
            //!actorname! funcname arg1 arg2 arg3
            //-> [actor.funcname target=actorname arg1 arg2 arg3]
            String[] splited = subLine.split(" +");
            if(splited.length <= 1) {
                //この段階で長さが1以下なら関数が作れない.
                throw new IllegalArgumentException(
                        "必要な引数を満たせません: " + subLine);
            } else {
                String actorName = splited[0];
                //末尾の!を取る
                if(actorName.endsWith("!"))
                    actorName = actorName.substring(0, actorName.length()-1);
                String funcName = splited[1];
                StringBuilder argsBuilder = new StringBuilder();
                if(splited.length >= 3)
                    for(int i=2; i<splited.length; i++) {
                        argsBuilder.append(splited[i]).append(" ");
                    }
                String content = "actor." + funcName + " target=" + 
                        actorName + " " + argsBuilder.toString();
                return "[" + content.trim() + "]";
            }
        }
    };
    
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
                LABEL_D, PAGING_D, CHARA_SELECT_D, CHARA_EDIT_D, ACTOR_EDIT_D,
                NO_LF_D, NO_W_D);
    }
    
}
