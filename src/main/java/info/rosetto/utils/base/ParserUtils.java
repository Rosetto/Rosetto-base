/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.utils.base;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * シナリオパーサー関係のユーティリティ.
 * @author tohhy
 */
public class ParserUtils {
    
    
    /**
     * スクリプト文字列をユニット文字列に分割する.<br>
     * タグ部分を末尾にするように行を分割する.
     * @param script
     * @return
     */
    public static LinkedList<String> splitScript(String script) {
        LinkedList<String> list = new LinkedList<String>();
        int scriptLength = script.length();
        int obCount = 0;
        int index = 0;
        StringBuilder buf = new StringBuilder();
        while(index != scriptLength) {
            char c = script.charAt(index);
            buf.append(c);
            if(c == '[') {
                obCount++;
            } else if(c == ']') {
                if(obCount > 0) obCount--;
                //この段階で括弧が閉じきられていればここまでをUnitにする
                if(obCount == 0) {
                    list.add(buf.toString());
                    buf = new StringBuilder();
                }
            }
            index++;
        }
        if(obCount == 0) {
            if(buf.length() > 0) list.add(buf.toString());
        } else {
            throw new IllegalArgumentException("not closed bracket found");
        }
        return list;
    }
    
    
    
    /**
     * 指定した文字列を角括弧で囲む.
     * @param content 囲む文字列
     * @return 角括弧で囲んだcontent
     */
    public static String squareBracket(String content) {
        return "[".concat(content).concat("]");
    }
    
}
