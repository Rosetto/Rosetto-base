package info.rosetto.models.base.engine;

import java.util.List;

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
