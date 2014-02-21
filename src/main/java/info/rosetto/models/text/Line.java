/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.frows.puregds.string.StyledString;
import org.frows.puregds.string.TextPositioning;
import org.frows.puregds.string.TextPositioning.Align;

/**
 * MessageText内に保持される行情報.
 * StyledTextのリストとTextPositioningを保持する.
 * @author tohhy
 */
public class Line implements Serializable {
    private static final long serialVersionUID = 8336633197404391481L;
    
    /**
     * テキストの配置情報.
     */
    private TextPositioning positioning;
    
    /**
     * この行が保持するスタイル済み文字列のリスト.
     */
    private final LinkedList<StyledString> list = new LinkedList<StyledString>();
    
    /**
     * デフォルトの配置ポリシーでこの行を初期化する.
     */
    Line() {
        this.setPositioning(TextPositioning.DEFAULT);
    }
    
    /**
     * 指定した配置ポリシーでこの行を初期化する.
     * @param positioning この行の配置ポリシー
     */
    public Line(TextPositioning positioning) {
        this.setPositioning(positioning);
    }
    
    /**
     * この行に指定した文字列オブジェクトを追加する.
     * @param string 追加する文字列
     */
    public void add(StyledString... string) {
        for(StyledString s : string)
            list.add(s);
    }
    
    /**
     * この行の最初に指定した文字列オブジェクトを追加する.
     * @param string 追加する文字列
     */
    public void addFirst(StyledString... string) {
        for(int i=string.length-1; i>=0; i--) {
            list.addFirst(string[i]);
        }
    }
    
    /**
     * この行に指定した文字列オブジェクトを全て追加する.
     * @param strings 追加する文字列
     */
    public void addAll(List<StyledString> strings) {
        list.addAll(strings);
    }
    
    /**
     * この行に指定行の内容を全て追加する.
     * @param strings 追加する行
     */
    public void addAll(Line strings) {
        list.addAll(strings.getList());
    }
    
    /**
     * この行を指定インデックスで2つに分割する.
     * @param index 分割位置
     * @return 分割後の行を格納したサイズ2のLine配列
     */
    public Line[] split(int index) {
        int len = getTextLength();
        if(index < 0)
            throw new IllegalArgumentException("index must be positive integer");
        if(index > len)
            throw new IllegalArgumentException("out of bounds: " + index);
        if(index == len)
            return new Line[]{this, new Line(positioning)};
        if(index == 0)
            return new Line[]{new Line(positioning), this};
        
        //分割の前部分を格納するリスト
        List<StyledString> head = new ArrayList<StyledString>();
        int sum = 0;
        for(StyledString s : list) {
            sum += s.getString().length();
            if(sum > index) {
                int slen = s.getString().length();
                int gap = sum - index;
                String s1 = s.getString().substring(0, slen - gap);
                String s2 = s.getString().substring(slen - gap);
                
                Line l1 = new Line();
                l1.addAll(head);
                l1.add(new StyledString(s1, s.getStyle()));
                Line l2 = new Line();
                List<StyledString> tail = new ArrayList<StyledString>(list);
                tail.remove(s);
                tail.removeAll(head);
                l2.add(new StyledString(s2, s.getStyle()));
                l2.addAll(tail);
                return new Line[] {l1, l2};
            }
            head.add(s);
        }
        return null;
        
    }

    /**
     * getAllText()と同値.
     */
    @Override
    public String toString() {
        return getAllText();
    }

    /**
     * この行に含まれる文字列オブジェクトの数を返す.
     * @return この行に含まれる文字列オブジェクトの数
     */
    public int size() {
        return getList().size();
    }

    /**
     * この行のフォント中で最も高いフォントサイズを返す.
     * この行が空だった場合は0を返す.
     */
    public int getHeight() {
        int max = 0;
        for(StyledString s : getList()) {
            int size = s.getFont().getSize() + s.getSpacing().getLineSpacing();
            if(size > max) max = size;
        }
        return max;
    }
    
    
    /**
     * この行に含まれる文字列の長さの合計を返す.
     */
    public int getTextLength() {
        int result = 0;
        for(StyledString s : getList())
            result += s.getString().length();
        return result;
    }
    
    /**
     * この行に含まれる文字列を全て連結して返す.
     * @return この行に含まれる文字列を全て連結した文字列
     */
    public String getAllText() {
        StringBuilder str = new StringBuilder("");
        for(StyledString s : getList())
            str.append(s.getString());
        return str.toString();
    }
    
    /**
     * この行の配置ポリシーを取得する.
     * @return この行の配置ポリシー
     */
    public TextPositioning getPositioning() {
        return positioning;
    }

    /**
     * この行の配置ポリシーを指定する.
     * @param positioning この行の配置ポリシー
     */
    public void setPositioning(TextPositioning positioning) {
        this.positioning = positioning;
    }
    
    /**
     * この行のインデント幅を取得する.
     * @return この行のインデント幅
     */
    public int getIndent() {
        return positioning.getSpacing().getIndent();
    }
    
    /**
     * この行の横方向の配置位置を取得する.
     * @return この行の横方向の配置位置
     */
    public Align getAlign() {
        return positioning.getAlign();
    }

    /**
     * この行が保持するスタイル済み文字列のリストを取得する.
     * 読み取り専用.
     * @return この行が保持するスタイル済み文字列のリスト
     */
    public List<StyledString> getList() {
        return Collections.unmodifiableList(list);
    }
}