/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.parsers;

import info.rosetto.utils.base.TextUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * パースに関連した操作を扱うユーティリティクラス.
 * @author tohhy
 */
public class ParseUtils {
    
    /**
     * 指定文字列を囲んでいる角括弧を除去する.<br>
     * 行頭と行末にそれぞれ左角括弧と右角括弧が存在する場合にその再外周の括弧のみを取り除く.<br>
     * 行頭と行末のホワイトスペースは無視される.
     * @param tag 角括弧を除去する文字列
     * @return 除去後の文字列
     */
    public static String removeSBracket(String tag) {
        tag = tag.trim();
        if(tag.startsWith("[") && tag.endsWith("]"))
            return tag.substring(1, tag.length() - 1);
        return tag;
    }
    
    /**
     * 指定文字列を囲んでいるダブルクォートを除去する.<br>
     * 行頭と行末のホワイトスペースは無視される.
     * @param tag 角括弧を除去する文字列
     * @return 除去後の文字列
     */
    public static String removeDoubleQuate(String str) {
        str = str.trim();
        if(str.startsWith("\"") && str.endsWith("\""))
            return str.substring(1, str.length() - 1);
        return str;
    }
    
    /**
     * 指定文字列を囲んでいる丸括弧を除去する.<br>
     * 行頭と行末にそれぞれ左角括弧と右角括弧が存在する場合にその再外周の括弧のみを取り除く.<br>
     * 行頭と行末のホワイトスペースは無視される.
     * @param tag 角括弧を除去する文字列
     * @return 除去後の文字列
     */
    public static String removeRBracket(String tag) {
        tag = tag.trim();
        if(tag.startsWith("(") && tag.endsWith(")"))
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
    public static String toTagName(String tag) {
        //括弧を外し、前後の空白を削除
        tag = removeSBracket(tag).trim();
        int firstSpaceIndex = tag.indexOf(' ');
        if(firstSpaceIndex > 0) {
            return tag.substring(0, firstSpaceIndex);
        } else {
            //スペースがなければ属性なしのタグと判断
            return tag;
        }
    }
    

    /**
     * 与えられた行中のタブ記号を全て取り除いて返す.
     * @param line 変換する行
     * @return タブ記号を全て取り除いたline
     */
    public static String removeTabs(String line) {
        return line.replace("\t", "");
    }
    
    /**
     * 指定した行を角括弧で囲む.
     * @param line 変換する行
     * @return 角括弧で囲んだline
     */
    public static String addSquareBracket(String line) {
        return "[".concat(line).concat("]");
    }
    
    /**
     * 指定した行が閉じられていない角括弧を持つかどうかを判定する.<br>
     * 連続する左角括弧によるエスケープは無効、括弧のネストは無視して最外周の階層のみ見る
     */
    public static boolean hasUnClosedBracket(String line) {
        //成立しているタグを全て取り除く
        line = removeAllTags(line);
        //括弧の有無を調べる
        int obIndex = line.indexOf("[");
        int cbIndex = line.indexOf("]");
        //左括弧はあるが右括弧がないなら閉じられていない括弧と判断
        if(obIndex >= 0 && cbIndex == -1) return true;
        //それ以外なら通常文字列として扱う
        return false;
    }
    
    /**
     * 行中から全てのタグを取り除く.<br>
     * 角括弧で囲まれている文字列をタグとみなす.<br>
     * 連続する左角括弧によるエスケープは無効、括弧のネストは無視して最外周の階層のみ見る.<br>
     * ダブルクオートは平叙文には存在せず、タグ内の値を囲むためだけに用いられると仮定する
     * @param line 変換する行
     * @return タグを取り除いた行
     */
    public static String removeAllTags(String line) {
        //括弧の有無を調べる
        int obIndex = line.indexOf("[");
        int cbIndex = line.indexOf("]");
        //左括弧か右括弧がなければそのまま
        if(obIndex == -1 || cbIndex == -1) return line;
        //タグ内のダブルクオートで囲まれた要素を取り除いておく
        line = removeAllDoubleQuotedStrings(line);
        //ダブルクオート除去後の括弧位置に更新、cbはobより後のものに制限する
        obIndex = line.indexOf("[");
        int cb = line.substring(obIndex).indexOf(']');
        if(cb == -1) return line;
        cbIndex = cb + obIndex;
        //どちらも存在し括弧を形成しているならその部分を取り除いて再帰
        String striped = line.substring(0, obIndex) + line.substring(cbIndex+1);
        return removeAllTags(striped);
    }
    

    /**
     * 行中から全てのダブルクオートで囲まれた部分を取り除く.
     * @param line 編集する行
     * @return ダブルクオートで囲まれた部分を取り除いた行
     */
    public static String removeAllDoubleQuotedStrings(String line) {
        int firstIndex = line.indexOf('"');
        if(firstIndex == -1 || firstIndex == line.length() - 1) return line;
        int secondIndex = line.substring(firstIndex + 1).indexOf('"');
        if(secondIndex == -1) return line;
        secondIndex = secondIndex + firstIndex;
        return removeAllDoubleQuotedStrings(line.substring(0, firstIndex) + line.substring(secondIndex+1));
    }
    
    /**
     * その行がタグのみかどうかを返す.<br>
     * 連続する左角括弧によるエスケープは無効、括弧のネストは無視して最外周の階層のみ見る
     * @param line 判定する行
     * @return その行がタグのみかどうか
     */
    public static boolean isTagOnlyLine(String line) {
        //成立しているタグを全て取り除く
        line = removeAllTags(line);
        //この時点で文字長0ならタグのみの行と判断できる
        return (line.length() == 0);
    }
    
    /**
     * 複数行からなる文字列を行ごとのリストへ変換する.
     * @param scenarioText 
     * @return 
     */
    public static List<String> asLines(String scenarioText) {
        //行を読み出す
        Scanner scanner = null;
        try {
            scanner = new Scanner(scenarioText);
            List<String> lines = new LinkedList<String>();
            while(scanner.hasNext()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        } finally {
            if(scanner != null)
                scanner.close();
        }
    }
    
    /**
     * 指定した文字列を角括弧で囲む.
     * @param content 囲む文字列
     * @return 角括弧で囲んだcontent
     */
    public static String squareBracket(String content) {
        return "[".concat(content).concat("]");
    }
    
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
     * mapの属性値がダブルクオートで囲まれていれば外す.
     */
    public static void removeDoubleQuotes(Map<String, String> attrs) {
        for(Entry<String, String> e : attrs.entrySet()) {
            //属性値がダブルクオートで囲まれていれば外す
            e.setValue(TextUtils.removeDoubleQuote(e.getValue()));
        }
    }

}
