/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.utils.base;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 引数解釈に必要な処理を集めたユーティリティクラス.
 * @author tohhy
 */
public class ArgumentsUtils {
    
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
        for(String str : args.split(" ")) {
            //キーも値もなければ飛ばす
            if(str.length() == 0) continue;
            param += str;
            if(TextUtils.containsCount(param, '\"') % 2 != 0) {
                //奇数個のダブルクオートを含むならスペースを補って次の文字列と連結
                param = param + " ";
                continue;
            }
            result.add(param);
            param = "";
        }
        return result.toArray(new String[result.size()]);
    }

}
