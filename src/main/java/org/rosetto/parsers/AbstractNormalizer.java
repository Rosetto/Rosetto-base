/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.rosetto.parsers;

import java.util.ArrayList;
import java.util.List;

import org.frows.lilex.designator.Designators;
import org.frows.lilex.parser.Normalizer;

/**
 * 与えられたスクリプトを変換し、角括弧形式のタグとプレーンテキストのみからなる標準形式にするノーマライザ.<br>
 * 文法ごとに定義し、パーサーの引数に与える必要がある.<br>
 * normalizeメソッドを実装し、そのスクリプトの行のリストを正規化して前述の標準形式にできるようにしなければいけない.
 * @author tohhy
 */
public abstract class AbstractNormalizer extends Normalizer {
    /**
     * 指定した指示子を用いるノーマライザを生成する.
     * @param designators このノーマライザが使用する指示子一覧
     */
    public AbstractNormalizer(Designators designators) {
        super(designators);
    }
    
    /**
     * 指示子を一切与えずにノーマライザを生成する.
     */
    public AbstractNormalizer() {
        super(new Designators());
    }
    
    /**
     * 与えられたスクリプトの行のリストを加工し、角括弧形式のタグとプレーンテキストのみにして返す.<br>
     * 通常の文章表示以外の機能呼び出しの全てが[と]で囲まれている状態にする.一行にいくつ括弧を含むかは任意.<br>
     * 変換後の段階ではまだ文法ごとの独自タグのままで、タグをRosettoのものに変換する処理はTagParserで行う.<br>
     * <br>
     * パーサー中では全ての機能呼び出しは[と]で囲まれていることを前提にして処理をするため、
     * KAGのコマンド行やRosettoのキャラクタ指定（行頭に@）、NScripterの関数などは
     * 文法上の形式が異なっても全て角括弧形式に変換しなくてはいけない.<br>
     * <br>
     * この変換規則をモデル化したものが指示子(Designator)で、ノーマライザは追加された指示子を一定の順番で適用する.<br>
     * 優先順位は 行末 ＜ 行頭 ＜ 行中 ＜ タグ型 になっており、優先順位が高いものから順に処理する.<br>
     * まずタグ型の変換を全行に適用、次に行中、行頭...という処理を行った結果を返す.
     * @return 正規化した行のリスト
     */
    public final List<String> normalize(List<String> lines) {
        List<String> result = new ArrayList<String>(lines);
        //前処理
        result = prepareNormalize(result);
        //指示子による変換
        result = super.normalize(lines);
        //後処理
        result = finishNormalize(result);
        return result;
    }
    
    /**
     * 指示子による変換が行われる前のスクリプトの行のリストが渡される.<br>
     * 前処理の必要があれば前処理を行って返すように実装する.
     * @param lines 指示子による変換が行われる前のスクリプトの行のリスト
     * @return 前処理を行った後の行のリスト、処理の必要が無ければlinesをそのまま渡す
     */
    protected List<String> prepareNormalize(List<String> lines) {
        return lines;
    }
    
    /**
     * 全ての指示子による変換が終わった結果が渡される.<br>
     * この関数で最終処理を行い、正規化の結果として返す.<br>
     * この関数の返り値は必ずテキストとタグのみからなる正規記法であることを保証しなくてはいけない.
     * @param lines 全ての指示子による変換が終わった結果の行のリスト
     * @return 後処理を行った後の行のリスト、処理の必要が無ければlinesをそのまま渡す
     */
    protected List<String> finishNormalize(List<String> lines) {
        return lines;
    }
    
}
