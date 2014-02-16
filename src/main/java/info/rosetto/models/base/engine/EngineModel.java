/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.base.engine;

import java.util.List;

/**
 * ファイルのロード方式等、プレイヤーの動作に関わる環境設定等を保持する.
 * @author tohhy
 *
 */
public interface EngineModel {

    /**
     * このエンジン特有のデフォルト設定をsettingsに適用する.
     */
    public void applyEngineDefaultSettings();

    /**
     * このエンジンがファイル探索の対象にするパスの一覧を返す.
     * @return このエンジンがファイル探索の対象にするパスの一覧
     */
    public List<SearchPath> getAvalablePathes();
    
    /**
     * このエンジンが処理対象にできる拡張子の一覧を返す.
     * @return このエンジンが処理対象にできる拡張子の一覧
     */
    public AvailableExtensions getAvailableExtensions();
    
    /**
     * デフォルトシナリオのファイル名にマッチする正規表現を返す.
     * 例: 
     * [0-9][0-9]\.txt -> 00.txt, 25.txt...
     * @return デフォルトシナリオのファイル名にマッチする正規表現
     */
    public String getDefaultScenarioNameRegex();
    
}
