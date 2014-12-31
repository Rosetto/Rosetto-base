/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.rosetto.utils.base;

import java.util.Arrays;
import java.util.List;

/**
 * テキスト処理のユーティリティ.
 * @author tohhy
 */
public class TextUtils {
    
    /**
     * 正規表現でエスケープが必要なcharのリスト.
     */
    public static final List<Character> REGEX_ESCAPE_NEEDED = 
            Arrays.asList(
                    '¥', '*', '+', '.', '?', 
                    '{', '}', '(', ')', '[', ']', 
                    '^', '$', '-', '|');
    
    
    /**
     * 指定文字列を正規表現上で単純な文字列として使えるようにエスケープする.
     * @param str
     * @return
     */
    public static String escapeForRegex(String str) {
        StringBuilder result = new StringBuilder();
        for(char c : str.toCharArray()) {
            if(REGEX_ESCAPE_NEEDED.contains(c)) {
                result.append("\\");
            }
            result.append(c);
        }
        return result.toString();
    }
    
    
    /**
     * 指定の開始文字列と終了文字列で終わるタグ全体にマッチする正規表現を生成する.
     * [, ] => \\[(.*?)\\]
     * @return
     */
    public static String createTagRegex(String start, String end) {
        return escapeForRegex(start) + "(.*?)" + escapeForRegex(end);
    }
    
    /**
     * 指定したcharが指定したString中にいくつ含まれているかを返す.
     */
    public static int containsCount(String str, char c) {
        String temp = str;
        int count = 0;
        int index = temp.indexOf(c);
        while(index > -1) {
            count++;
            temp = temp.substring(index+1);
            index = temp.indexOf(c);
        }
        return count;
    }
    
    /**
     * 文字列がダブルクオートで囲まれていれば取り外す.
     * @param str
     * @return
     */
    public static String removeDoubleQuote(String str) {
        if(str.matches("^\"(.*)\"$")) {
            return str.substring(1, str.length()-1);
        }
        return str;
    }
}
