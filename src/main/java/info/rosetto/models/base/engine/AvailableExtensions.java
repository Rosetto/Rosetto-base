package info.rosetto.models.base.engine;

import java.io.Serializable;

/**
 * エンジンが処理できる拡張子の一覧を格納するクラス.
 * @author tohhy
 */
public abstract class AvailableExtensions implements Serializable {
    private static final long serialVersionUID = -7866358627420846010L;

    /**
     * エンジンが処理対象にするシナリオファイル拡張子の一覧を返す.
     * 例: new String() {"txt", "rs"};
     * @return エンジンが処理対象にするシナリオファイル拡張子の一覧
     */
    public abstract String[] getScenarioExt();

    /**
     * エンジンが処理可能な画像ファイル拡張子の一覧を返す.
     * 例: new String() {"png", "jpg", "gif"};
     * @return エンジンが処理可能な画像ファイル拡張子の一覧
     */
    public abstract String[] getImageExt();
    
    /**
     * エンジンが処理可能なサウンドファイル拡張子の一覧を返す.
     * 例: new String() {"wav", "ogg", "mp3"};
     * @return エンジンが処理可能なサウンドファイル拡張子の一覧
     */
    public abstract String[] getSoundExt();
    
    /**
     * エンジンが処理可能な動画ファイル拡張子の一覧を返す.
     * 例: new String() {"avi", "mp4", "wmv"};
     * @return エンジンが処理可能な動画ファイル拡張子の一覧
     */
    public abstract String[] getMovieExt();
    
    /**
     * エンジンが処理可能なフォントファイル拡張子の一覧を返す.
     * 例: new String() {"ttf", "tft"};
     * @return エンジンが処理可能なフォントファイル拡張子の一覧
     */
    public abstract String[] getFontExt();

}
