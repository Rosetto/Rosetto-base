/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.parsers.rosetto;

import info.rosetto.parsers.AbstractNormalizer;
import info.rosetto.parsers.ParseUtils;

/**
 * Rosettoのスクリプトを標準形式に変換する.
 * @author tohhy
 */
public class RosettoNormalizer extends AbstractNormalizer {
    //改ページタグの定義
    private static final String PAGING_TAG = "[p]";
    
    //連続したページングを無視するためのフラグ
    private boolean duplicatePageFeed = true;
    //editLineで複数行タグを処理するためのバッファ
    private String tempLine;
    
    public RosettoNormalizer() {
        super(new RosettoDesignators());
    }
    
    @Override
    protected String processMiddleDesignators(String line, String prevLine, String nextLine) {
        //前後のホワイトスペースを除去、括弧のエスケープを処理
        line = line.trim();
        line = ParseUtils.escapeBracket(line);
        
        //コメント等の行中指示子を適用
        line = super.processMiddleDesignators(line, prevLine, nextLine);
        
        //この時点で空行・コメントのみの行なら無視
        if(line.length() == 0) return null;
        
        return line;
    }
    
    @Override
    protected String processHeadDesignators(String line, String prevLine, String nextLine) {
        //括弧の途中での改行に対応するために現在行を記憶しておく
        if(tempLine != null) line = tempLine.concat(line);
        tempLine = null;
        
        //行頭演算子を処理して行を置き換える
        line = super.processHeadDesignators(line, prevLine, nextLine);
        
        //置き換え後が単一のページングタグなら
        if(line.equals(PAGING_TAG)) {
            //ページ送りが重複していれば無視
            if(duplicatePageFeed) return null;
            duplicatePageFeed = true;
        } else {
            duplicatePageFeed = false;
        }
        
        //閉じられていない括弧があれば次の行と連結する
        if(ParseUtils.hasUnClosedBracket(line)) {
            tempLine = line + " ";
            return null;
        }
        return line;
    }
    
    @Override
    protected String processTailDesignators(String line, String prevLine, String nextLine) {
        
        //行末指示子を適用
        String converted = super.processTailDesignators(line, prevLine, nextLine);
        //行末指示子で何らかの変更が生じれば自動改行はしない
        if(!converted.equals(line)) {
            return converted;
        }
        
        if(line.endsWith("[lf]") || line.endsWith("[br]")) {
            //すでに改行で終わっていれば変更しない
            return line;
        } else if(ParseUtils.isTagOnlyLine(line)) {
            //タグのみの行は無改行（変更なし）にする
            return line;
        } else {
            if(nextLine == null) {
                //シナリオの最後なら改行付加
                return line + "[br]";
            } else if(!isPaging(nextLine)) {
                //次の行がページングでなければ改行付加
                return line + "[br]";
            }
            //次の行がページングなら二重に改行されてしまうので付加しない
        }
        return line;
    }
    
    /**
     * その行がページングタグを含んでいるかを返す.
     * @param line 
     * @return 
     */
    private boolean isPaging(String line) {
        //キャラクタ選択もページングを行う
        return line.contains(PAGING_TAG) || line.startsWith("[character.select");
    }
}
