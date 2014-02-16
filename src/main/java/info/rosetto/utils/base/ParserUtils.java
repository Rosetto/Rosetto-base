/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.utils.base;

import info.rosetto.models.base.values.ActionCall;
import info.rosetto.models.base.values.ListValue;
import info.rosetto.models.base.values.RosettoValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

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
    

    private static class ArgParser {
        Stack<Integer> obIndex = new Stack<Integer>();
        Map<Integer, Integer> coress = new HashMap<Integer, Integer>();
        Map<Integer, ActionCall> parsed = new HashMap<Integer, ActionCall>();
        private final String argStr;
        
        public ArgParser(String argStr) {
            this.argStr = argStr;
        }
        
        private RosettoValue rawValue() {
            if(argStr.startsWith("(") && argStr.endsWith(")")) {
                return new ListValue(argStr.substring(1, argStr.length()-1).split(" "));
            }
            return Values.create(argStr);
        }
        
        public RosettoValue parse() {
            if(!argStr.startsWith("[")) return rawValue();
            int strLen = argStr.length();
            
            for(int i=0; i<strLen; i++) {
                char c = argStr.charAt(i);
                
                if(c == '[') {
                    obIndex.push(i);
                } else if(c == ']') {
                    if(obIndex.isEmpty()) throw new IllegalArgumentException("");
                    int ob = obIndex.pop();
                    coress.put(ob, i);
                    ActionCall v = parseRegion(ob, i);
                    parsed.put(ob, v);
                }
            }
            RosettoValue result = parsed.get(0);
            if(result instanceof ActionCall) result = ((ActionCall)result).evaluate();
            return result;
        }
        
        private ActionCall parseRegion(int start, int end) {
            List<String> result = new ArrayList<String>();
            StringBuilder buf = new StringBuilder();
            for(int i=start+1; i<end; i++) {
                char c = argStr.charAt(i);
                
                if(c == '[') {
                    //ここまでのbuf内容を追加してクリア
                    if(buf.length() > 0) {
                        result.add(buf.toString());
                        buf = new StringBuilder();
                    }
                    //パース済みのactioncallをresultに追加
                    result.add(parsed.get(i).asString());
                    //対応する括弧までスキップ
                    i = coress.get(i);
                } else if(c == ']') {
                    throw new IllegalArgumentException("");
                } else if(c == ' ') {
                    //ここまでのbuf内容を追加してクリア
                    if(buf.length() > 0) {
                        result.add(buf.toString());
                        buf = new StringBuilder();
                    }
                } else {
                    buf.append(c);
                }
            }
            //残っていればここまでのbuf内容を追加
            if(buf.length() > 0) result.add(buf.toString());
            
            if(result.size() == 0) {
                return ActionCall.EMPTY;
            } else if(result.size() == 1) {
                return new ActionCall(result.get(0));
            } else {
                String[] args = new String[result.size()-1];
                for(int i=1; i<result.size(); i++) {
                    args[i-1] = result.get(i);
                }
                return new ActionCall(result.get(0), args);
            }
            
        }
    }
    
    
    /**
     * 文字列の引数をパースしてRosettoValueに変換する.
     * @param argStr
     * @return
     */
    public static RosettoValue parseArg(String argStr) {
        return new ArgParser(argStr).parse();
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
    
    /**
     * 文字列の引数をString配列に分解する.
     * ダブルクオートで囲まれた引数のデフォルト値にスペースが含まれる場合や
     * スペースが連続した場合に対応.
     * @param args
     * @return
     */
    public static String[] splitStringArgs(String args) {
        ArrayList<String> result = new ArrayList<String>();
        String param = "";
        int sbCount = 0;
        int rbCount = 0;
        for(String str : args.split(" ")) {
            //キーも値もなければ飛ばす
            if(str.length() == 0) continue;
            param += str;
            
            //括弧の開きをカウント
            int sb = TextUtils.containsCount(str, '[');
            int rb = TextUtils.containsCount(str, '(');
            //括弧の閉じをカウント
            int csb = TextUtils.containsCount(str, ']');
            int crb = TextUtils.containsCount(str, ')');
            //開きの残りを計算
            sbCount += sb-csb;
            rbCount += rb-crb;
            //負の値になっていれば0に丸める
            if(sbCount < 0) sbCount = 0;
            if(rbCount < 0) rbCount = 0;
            
            //奇数個のダブルクオートを含むならスペースを補って次の文字列と連結
            if(TextUtils.containsCount(param, '\"') % 2 != 0) {
                param = param + " ";
                continue;
            }
            
            //括弧の開きが残っているならスペースを補って次の文字列と連結
            if(sbCount > 0 || rbCount > 0) {
                param = param + " ";
                continue;
            }
            //ここまで到達できればここまでの内容をひとつの引数とする
            result.add(param);
            param = "";
        }
        if(sbCount > 0 || rbCount > 0)
            throw new IllegalArgumentException("bracket not closed");
        return result.toArray(new String[result.size()]);
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
