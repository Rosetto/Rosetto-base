package info.rosetto.models.text.style;

import java.io.Serializable;

/**
 * テキストの配置ポリシー(行の範囲固定情報、自動改行の有無)を保持する.
 * イミュータブル.
 * @author tohhy
 */
public class TextBounding implements Serializable {
    /**
     * 0.1.0 (2013/09/08)
     */
    private static final long serialVersionUID = -567553430412177479L;

    /**
     * 行の範囲情報.
     */
    private final LineBounds lineBounds;
    
    /**
     * 自動改行をするかどうか.
     */
    private final boolean isAutoReturn;
    
    /**
     * 行サイズを指定せず、自動改行するTextBounding.
     */
    public static TextBounding DEFAULT = new TextBounding(LineBounds.FREE, true);
    
    /**
     * 行サイズを指定せず、自動改行もしないTextBounding.
     */
    public static TextBounding NO_AUTO_RETURN = new TextBounding(LineBounds.FREE, false);
    
    /**
     * 行のサイズ情報と自動改行の有無を指定して初期化する.
     * @param size 行のサイズ指定
     * @param isAutoReturn 自動改行の有無
     */
    public TextBounding(LineBounds size, boolean isAutoReturn) {
        this.lineBounds = (size == null) ? LineBounds.FREE : size;
        this.isAutoReturn = isAutoReturn;
    }
    
    /**
     * LineBoundsで派生した新しいインスタンスを返す.
     * @param size 指定するLineBounds
     * @return 派生した新しいインスタンス
     */
    public TextBounding derive(LineBounds size) {
        if(size == this.lineBounds)
            return this;
        return new TextBounding(size, isAutoReturn);
    }
    
    /**
     * 行のサイズ情報を返す.
     * @return 行のサイズ情報
     */
    public LineBounds getLineBounds() {
        return lineBounds;
    }
    
    /**
     * 自動改行をするかどうかを返す.
     * @return 自動改行をするかどうか
     */
    public boolean isAutoReturn() {
        return isAutoReturn;
    }
}
