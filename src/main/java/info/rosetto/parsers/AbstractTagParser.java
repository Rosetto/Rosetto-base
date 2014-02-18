/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.parsers;

import info.rosetto.models.base.values.ActionCall;

/**
 * タグ文字列をRosettoのActionCallに変換するためのパーサー.
 * @author tohhy
 */
public abstract class AbstractTagParser {
    
    /**
     * 指定タグを変換する.parseTagから呼び出される.<br>
     * 個々のエンジンごとの独自関数とその引数を受け取り、
     * Rosettoのアクションを呼び出すActionCallに変換して返すように実装する.
     * @param tagName エンジンごとの関数名
     * @param args 文字列形式の引数
     * @return 変換後のActionCall
     */
    protected abstract ActionCall convertTag(String tagName, String args);
    
    /**
     * 指定タグをRosettoActionに変換する.<br>
     * 指定文字列が角括弧で囲まれていればそれを外し、
     * 最初に登場するスペースまでをタグ名、それ以降を引数としてActionCallを生成する.
     * @param tag 変換するタグ
     * @return 変換後のActionCall
     */
    public ActionCall parseTag(String tag) {
        if(tag == null) return ActionCall.EMPTY;
        tag = removeBracket(tag);
        String functionName = toTagName(tag);
        String args = tag.substring(functionName.length());
        return convertTag(functionName, args);
    }
    
    /**
     * 指定文字列を囲んでいる角括弧を除去する.<br>
     * 行頭と行末にそれぞれ左角括弧と右角括弧が存在する場合にその再外周の括弧のみを取り除く.<br>
     * 行頭と行末のホワイトスペースは無視される.
     * @param tag 角括弧を除去する文字列
     * @return 除去後の文字列
     */
    protected String removeBracket(String tag) {
        tag = tag.trim();
        if(tag.startsWith("[") && tag.endsWith("]"))
            return tag.substring(1, tag.length() - 1);
        return tag;
    }

    /**
     * タグを受け取り、タグの名前部分を返す.<br>
     * 角括弧で囲まれていればそれを外し、最初に登場するスペースの直前までをタグ名とみなして返す.<br>
     * 行頭と行末のホワイトスペースは無視される.
     * @param tag タグ
     * @return タグの名前部分
     */
    protected String toTagName(String tag) {
        //括弧を外し、前後の空白を削除
        tag = removeBracket(tag).trim();
        int firstSpaceIndex = tag.indexOf(' ');
        if(firstSpaceIndex > 0) {
            return tag.substring(0, firstSpaceIndex);
        } else {
            //スペースがなければ属性なしのタグと判断
            return tag;
        }
    }
}
