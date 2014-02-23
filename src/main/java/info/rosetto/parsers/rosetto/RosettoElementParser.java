/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.parsers.rosetto;

import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.values.ActionCall;
import info.rosetto.models.base.elements.values.BoolValue;
import info.rosetto.models.base.elements.values.DoubleValue;
import info.rosetto.models.base.elements.values.IntValue;
import info.rosetto.models.base.elements.values.ListValue;
import info.rosetto.models.base.elements.values.OptionableList;
import info.rosetto.models.base.elements.values.StringValue;
import info.rosetto.parsers.AbstractElementParser;
import info.rosetto.parsers.ParseUtils;
import info.rosetto.utils.base.TextUtils;
import info.rosetto.utils.base.Values;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * 
 * @author tohhy
 */
public class RosettoElementParser extends AbstractElementParser {
    
    /**
     * 与えられた文字列をRosettoの要素としてパースする.<br>
     * アクション呼び出しが含まれていた場合、ActionCallへ変換はするが内容の評価はしない.
     */
    @Override
    public RosettoValue parseElement(String element) {
        //nullならValues.NULL
        if(element == null) return Values.NULL;
        //ダブルクォートで囲まれていれば外す
        element = ParseUtils.removeDoubleQuate(element);
        
        //特定の括弧で囲まれていればそれに合わせた変換
        if(element.startsWith("[") && element.endsWith("]")) {
            //角括弧で囲まれていれば関数呼び出しと判断
            return parseActionCall(element);
        } else if(element.startsWith("(") && element.endsWith(")")) {
            //丸括弧で囲まれていればリストと判断
            return parseList(element);
        }
        
        //特定の接頭詞があればそれに合わせた変換
        if(element.startsWith("@")) {
            //@で始まるならローカル変数呼び出し
            return new ActionCall("getlocal", element.substring(1));
        } else if(element.startsWith("$")) {
            //$で始まるならグローバル変数呼び出し
            return new ActionCall("getglobal", element.substring(1));
        }
        
        //数値ならそれに合わせた実体を持たせる
        if(NumberUtils.isNumber(element)) {
            if(element.contains(".")) {
                return new DoubleValue(NumberUtils.toDouble(element));
            } else {
                return new IntValue(NumberUtils.toLong(element));
            }
        }
        
        //boolならそれに合わせた実体を持たせる
        if(element.equals("true")) {
            return BoolValue.TRUE;
        } else if(element.equals("false")) {
            return BoolValue.FALSE;
        }
        
        //どれにも当てはまらなければ文字列扱い
        return new StringValue(element);
    }
    
    
    
    /**
     * 指定タグをRosettoActionに変換する.<br>
     * 指定文字列が角括弧で囲まれていればそれを外し、
     * 最初に登場するスペースまでをタグ名、それ以降を引数としてActionCallを生成する.
     * @param element 変換するタグ
     * @return 変換後のActionCall
     */
    public ActionCall parseActionCall(String element) {
        if(element == null) return ActionCall.EMPTY;
        String content = ParseUtils.removeSBracket(element);
        String actionName = ParseUtils.toTagName(content);
        String args = content.substring(actionName.length());
        return new ActionCall(actionName, args);
    }
    
    /**
     * 
     * @param element
     * @return
     */
    public RosettoValue parseList(String element) {
        if(element == null) return ListValue.EMPTY;
        String content = ParseUtils.removeRBracket(element);
        List<String> splited = splitElements(content);
        OptionableList elements = OptionableList.createFromString(splited);
        if(elements.hasMappedValue()) {
            return OptionableList.createFromValue(elements);
        }
        return new ListValue(elements.getList());
    }
    
    
    public List<String> splitElements(String elements) {
        ArrayList<String> result = new ArrayList<String>();
        String param = "";
        int sbCount = 0;
        int rbCount = 0;
        for(String str : elements.split(" ")) {
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
        return result;
    }
    
}
