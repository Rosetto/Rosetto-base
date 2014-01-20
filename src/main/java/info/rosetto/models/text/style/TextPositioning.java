package info.rosetto.models.text.style;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;

/**
 * テキストの配置に関する情報を保持する.
 * @author tohhy
 */
@Immutable
public class TextPositioning implements Serializable {
    
    /**
     * 0.1.0 (2013/09/08)
     */
    private static final long serialVersionUID = -3201462738026124456L;

    /**
     * 横方向の配置位置を表す.
     * @author tohhy
     */
    public enum Align {
        DEFAULT, LEFT, CENTER, TOP, RIGHT, 
    }
    
    /**
     * インデント、字間、行間の情報.
     */
    private final Spacing spacing;
    
    /**
     * 横方向の配置位置.
     */
    private final Align align;
    
    /**
     * テキストの配置ポリシー(行の範囲固定情報、自動改行の有無).
     */
    private final TextBounding bounding;
    
    /**
     * デフォルトのテキスト配置.
     * 行間5px, 左寄せ、 範囲固定なし自動改行あり.
     */
    public static final TextPositioning DEFAULT = 
            new TextPositioning(Spacing.DEFAULT, Align.LEFT, TextBounding.DEFAULT);
    
    /**
     * 各種情報を指定して初期化する.
     * @param spacing インデント、字間、行間の情報
     * @param align 横方向の配置位置
     * @param bounding テキストの配置ポリシー(行の範囲固定情報、自動改行の有無)
     */
    public TextPositioning(Spacing spacing, Align align, TextBounding bounding) {
        this.spacing = spacing;
        this.align = align;
        this.bounding = bounding;
    }
    
    /**
     * spacingのみを変更した新しいインスタンスを生成して返す.
     * @param spacing インデント、字間、行間の情報
     * @return 派生した新しいインスタンス
     */
    public TextPositioning derive(Spacing spacing) {
        if(spacing == this.spacing)
            return this;
        return new TextPositioning(spacing, align, bounding);
    }
    
    /**
     * alignのみを変更した新しいインスタンスを生成して返す.
     * @param align 横方向の配置位置
     * @return 派生した新しいインスタンス
     */
    public TextPositioning derive(Align align) {
        if(align == this.align)
            return this;
        return new TextPositioning(spacing, align, bounding);
    }
    
    /**
     * boundingのみを変更した新しいインスタンスを生成して返す.
     * @param bounding テキストの配置ポリシー(行の範囲固定情報、自動改行の有無)
     * @return 派生した新しいインスタンス
     */
    public TextPositioning derive(TextBounding bounding) {
        if(bounding == this.bounding)
            return this;
        return new TextPositioning(spacing, align, bounding);
    }
    
    /**
     * インデント、字間、行間の情報を取得する.
     * @return インデント、字間、行間の情報
     */
    public Spacing getSpacing() {
        return spacing;
    }

    /**
     * 横方向の配置位置を取得する.
     * @return 横方向の配置位置
     */
    public Align getAlign() {
        return align;
    }

    /**
     * テキストの配置ポリシー(行の範囲固定情報、自動改行の有無)を取得する.
     * @return テキストの配置ポリシー
     */
    public TextBounding getBounding() {
        return bounding;
    }
}
