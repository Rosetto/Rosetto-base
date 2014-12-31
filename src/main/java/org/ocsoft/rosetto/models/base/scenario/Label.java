/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.ocsoft.rosetto.models.base.scenario;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;

/**
 * ラベルのモデル.Scenarioが保持する.<br>
 * シナリオ中の位置を記録し、gotoコマンドやcallコマンドでのジャンプ先として用いる.
 * @author tohhy
 */
@Immutable
public class Label implements Serializable, ScenarioToken {
    private static final long serialVersionUID = 1014810670973539619L;

    /**
     * このラベルの識別名.シナリオ中で一意である必要がある.
     */
    private final String id;
    
    /**
     * シナリオ中の位置.
     */
    private final int index;
    
    /**
     * ラベルタイトル.セーブロードで飛び先の識別名を表示する必要がある場合などに指定する.
     */
    private final String title;
    
    /**
     * このラベルのidと、ラベルを配置するインデックス、
     * ラベルタイトルを指定して初期化する.
     * @param id このラベルのid、gotoコマンド等で指定される
     * @param index このラベルの配置インデックス
     * @param title このラベルのタイトル、nullを指定すると空文字になる
     */
    public Label(String id, int index, String title) {
        if(id == null || id.length() <= 0)
            throw new IllegalArgumentException("ラベルのidが指定されていません");
        this.id = id;
        if(index < 0)
            throw new IllegalArgumentException("unitIndexは0以上である必要があります");
        this.index = index;
        this.title = (title == null) ? "" : title;
    }
    
    /**
     * このラベルのidと、ラベルを配置するインデックスを指定して初期化する.
     * @param id このラベルのid、gotoコマンド等で指定される
     * @param index このラベルの配置インデックス
     */
    public Label(String id, int index) {
        this(id, index, null);
    }
    
    /**
     * このラベルの識別名を返す.<br>
     * nullにはならないこと、1文字以上の文字列であることが保証される.
     */
    public String getName() {
        return id;
    }
    
    /**
     * このラベルのタイトルを返す.<br>
     * nullにはならないことが保証される. nullを指定した場合は空文字になる.
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * このラベルがシナリオ中の何番目のユニットを指定しているかを返す.<br>
     * 0以上の整数であることが保証される.
     */
    public int getIndex() {
        return index;
    }
}
