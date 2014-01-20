package info.rosetto.models.text.style;

import info.rosetto.models.text.style.TextPositioning.Align;

import java.io.Serializable;

import org.frows.puregds.color.Color;
import org.frows.puregds.font.Font;

/**
 * メッセージ描画に関するスタイル情報を保持する.
 * イミュータブル.個々のUnitStringがこのクラスへの参照を持ち、スタイル情報を保持する.
 * @author tohhy
 */
public class TextStyle implements Serializable {
    
    /**
     * 0.1.0 (2013/09/08)
     */
    private static final long serialVersionUID = -6884267916462531317L;

    /**
     * フォントとフォント色の情報.
     */
    private final ColoredFont fontStyle;
    
    /**
     * テキストの配置に関する情報.
     */
    private final TextPositioning positioning;
    
    /**
     * デフォルトのスタイル.
     * 白字ゴシックの16px, 行間5px, 左寄せ、 範囲固定なし自動改行あり.
     */
    public static TextStyle DEFAULT = new TextStyle(ColoredFont.DEFAULT, TextPositioning.DEFAULT);
    
    /**
     * フォントのスタイルとテキスト配置情報から初期化する.
     * @param fontStyle フォントとフォント色の情報
     * @param positioning テキストの配置に関する情報
     */
    public TextStyle(ColoredFont fontStyle, TextPositioning positioning) {
        this.fontStyle = fontStyle;
        this.positioning = positioning;
    }
    
    /**
     * フォント色のみを変更した新しいインスタンスを返す.
     * @param fontColor フォント色
     * @return 派生した新しいインスタンス
     */
    public TextStyle derive(Color fontColor) {
        if(fontColor == this.fontStyle.getColor()) return this;
        return new TextStyle(fontStyle.derive(fontColor), positioning);
    }
    
    /**
     * テキスト配置のみを変更した新しいインスタンスを返す.
     * @param positioning テキスト配置
     * @return 派生した新しいインスタンス
     */
    public TextStyle derive(TextPositioning positioning) {
        if(positioning == this.positioning) return this;
        return new TextStyle(fontStyle, positioning);
    }
    
    /**
     * フォントのみを変更した新しいインスタンスを返す.
     * @param font フォント
     * @return 派生した新しいインスタンス
     */
    public TextStyle derive(Font font) {
        if(font == fontStyle.getFont()) return this;
        return new TextStyle(fontStyle.derive(font), positioning);
    }
    
    /**
     * フォントのみを変更した新しいインスタンスを返す.
     * @param font フォント
     * @return 派生した新しいインスタンス
     */
    public TextStyle derive(ColoredFont font) {
        if(font == fontStyle) return this;
        return new TextStyle(font, positioning);
    }

    /**
     * indentのみを変更した新しいインスタンスを返す.
     * @param indent 行頭インデントのピクセル数
     * @return 派生した新しいインスタンス
     */
    public TextStyle deriveByIndent(int indent) {
        if(indent == this.getSpacing().getIndent()) return this;
        final Spacing s = positioning.getSpacing().deriveByIndent(indent);
        final TextPositioning p = positioning.derive(s);
        return derive(p);
    }
    
    /**
     * 行頭インデントのピクセル数を返す.
     */
    public int getIndent() {
        return positioning.getSpacing().getIndent();
    }
    
    /**
     * このインスタンスが保持するフォントを取得する.
     * @return　このインスタンスが保持するフォント
     */
    public Font getFont() {
        return fontStyle.getFont();
    }
    
    /**
     * このインスタンスが保持するフォント色を取得する.
     * @return このインスタンスが保持するフォント色
     */
    public Color getColor() {
        return fontStyle.getColor();
    }
    
    /**
     * インデント、字間、行間の情報を取得する.
     * @return インデント、字間、行間の情報
     */
    public Spacing getSpacing() {
        return positioning.getSpacing();
    }

    /**
     * 横方向の配置位置を取得する.
     * @return 横方向の配置位置
     */
    public Align getAlign() {
        return positioning.getAlign();
    }

    /**
     * テキストの配置ポリシー(行の範囲固定情報、自動改行の有無)を取得する.
     * @return テキストの配置ポリシー
     */
    public TextBounding getBounding() {
        return positioning.getBounding();
    }

    /**
     * 行のサイズ情報を返す.
     * @return 行のサイズ情報
     */
    public LineBounds getLineSize() {
        return positioning.getBounding().getLineBounds();
    }
    
    /**
     * 自動改行をするかどうかを返す.
     * @return 自動改行をするかどうか
     */
    public boolean isAutoReturn() {
        return positioning.getBounding().isAutoReturn();
    }

    /**
     * テキストの配置に関する情報を取得する.
     * @return テキストの配置に関する情報
     */
    public TextPositioning getPositioning() {
        return positioning;
    }

    /**
     * フォントとフォント色の情報を取得する.
     * @return フォントとフォント色の情報
     */
    public ColoredFont getColoredFont() {
        return fontStyle;
    }

}
