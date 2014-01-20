/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.text.style;

import java.io.Serializable;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.frows.puregds.color.Color;
import org.frows.puregds.font.Font;
import org.frows.puregds.font.FontFamily;
import org.frows.puregds.font.FontStyle;

/**
 * フォントとフォント色の情報を保持する.
 * @author tohhy
 */
@Immutable
public class ColoredFont implements Serializable {
    private static final long serialVersionUID = -4944087552143021571L;

    /**
     * このオブジェクトが保持するフォント情報.
     */
    @Nonnull
    private final Font font;
    
    /**
     * このオブジェクトが保持する色情報.
     */
    @Nonnull
    private final Color color;
    
    /**
     * このオブジェクトが初期化される際のデフォルトのフォント情報.
     * 変更するとそれ以降にオブジェクトを新しく生成した際に適用される.
     */
    @Nonnull
    private static Font defaultFont = 
            new Font(FontFamily.SANS_SERIF, FontStyle.PLAIN, 16);
    
    /**
     * このオブジェクトが初期化される際のデフォルトの色情報.
     * 変更するとそれ以降にオブジェクトを新しく生成した際に適用される.
     */
    @Nonnull
    private static Color defaultColor = Color.BLUE;
    
    /**
     * デフォルトのフォントスタイル. 青色、ゴシックの16px.
     * デフォルトフォント・デフォルトカラーを再設定しても変化しない.
     * 青色にしているのは背景色が白・黒・灰色等のいずれの場合でも文字描画が正常に行われていることを確認しやすくするため.
     * 基本的にはデフォルト色を変更して使用されることを想定.
     */
    public static final ColoredFont DEFAULT = new ColoredFont(defaultFont, defaultColor);
    
    /**
     * フォントとフォント色を指定して初期化する.
     * @param font 指定するフォント
     * @param color 指定するフォント色
     */
    public ColoredFont(Font font, Color color) {
        this.font = (font == null) ? defaultFont : font;
        this.color = (color == null) ? defaultColor : color;
    }
    
    /**
     * フォントを指定して初期化する.
     * フォント色はデフォルト値になる.
     * @param font 指定するフォント
     */
    public ColoredFont(Font font) {
        this(font, null);
    }
    
    /**
     * フォント色を指定して初期化する.
     * フォントはデフォルト値になる.
     * @param color 指定するフォント色
     */
    public ColoredFont(Color color) {
        this(null, color);
    }
    
    /**
     * このインスタンスのフォントを変更した新しいインスタンスを返す.
     * 変更がない場合はそのままこのインスタンスを返す.
     * @param font 変更先のフォント
     * @return このインスタンスのフォントを変更した新しいインスタンス
     */
    @Nonnull
    public ColoredFont derive(Font font) {
        if(font == this.font) return this;
        return new ColoredFont(font, this.color);
    }
    
    /**
     * このインスタンスのフォントファミリを変更した新しいインスタンスを返す.
     * 変更がない場合はそのままこのインスタンスを返す.
     * @param font 変更先のフォント
     * @return このインスタンスのフォントを変更した新しいインスタンス
     */
    @Nonnull
    public ColoredFont derive(FontFamily family) {
        if(family.equals(this.font.getFamily())) return this;
        return new ColoredFont(getFont().derive(family), this.color);
    }
    
    /**
     * このインスタンスのフォントファミリを変更した新しいインスタンスを返す.
     * 変更がない場合はそのままこのインスタンスを返す.
     * @param font 変更先のフォント
     * @return このインスタンスのフォントを変更した新しいインスタンス
     */
    @Nonnull
    public ColoredFont derive(String family) {
        if(family.equals(this.font.getFamily().toString())) return this;
        return new ColoredFont(getFont().derive(new FontFamily(family)), this.color);
    }
    
    /**
     * このインスタンスのフォントサイズを変更した新しいインスタンスを返す.
     * 変更がない場合はそのままこのインスタンスを返す.
     * @param font 変更先のフォント
     * @return このインスタンスのフォントを変更した新しいインスタンス
     */
    @Nonnull
    public ColoredFont derive(int size) {
        if(size == this.font.getSize()) return this;
        return new ColoredFont(getFont().derive(size), this.color);
    }
    
    /**
     * このインスタンスのフォントファミリを変更した新しいインスタンスを返す.
     * 変更がない場合はそのままこのインスタンスを返す.
     * @param font 変更先のフォント
     * @return このインスタンスのフォントを変更した新しいインスタンス
     */
    @Nonnull
    public ColoredFont derive(FontStyle style) {
        if(style == this.font.getStyle()) return this;
        return new ColoredFont(getFont().derive(style), this.color);
    }
    
    /**
     * このインスタンスのフォント色を変更した新しいインスタンスを返す.
     * 変更がない場合はそのままこのインスタンスを返す.
     * @param color 変更先のフォント色
     * @return このインスタンスのフォント色を変更した新しいインスタンス
     */
    @Nonnull
    public ColoredFont derive(Color color) {
        if(color == this.color) return this;
        return new ColoredFont(this.font, color);
    }

    /**
     * このインスタンスが保持するフォント色を取得する.
     * @return このインスタンスが保持するフォント色
     */
    @Nonnull
    public Color getColor() {
        return color;
    }

    /**
     * このインスタンスが保持するフォントを取得する.
     * @return　このインスタンスが保持するフォント
     */
    @Nonnull
    public Font getFont() {
        return font;
    }
    
    /**
     * このインスタンスが保持するフォントのサイズを取得する.
     * @return このインスタンスが保持するフォントのサイズ
     */
    @Nonnegative
    public int getFontSize() {
        return font.getSize();
    }
    
    /**
     * このインスタンスが保持するフォントのフォントファミリを取得する.
     * @return このインスタンスが保持するフォントのフォントファミリ
     */
    @Nonnull
    public String getFontFamily() {
        return font.getFamily();
    }
    
    /**
     * このインスタンスが保持するフォントのスタイルを取得する.
     * @return このインスタンスが保持するフォントのスタイル
     */
    @Nonnull
    public FontStyle getStyle() {
        return font.getStyle();
    }
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof ColoredFont)) return false;
        ColoredFont sut = (ColoredFont) obj;
        return sut.getColor().equals(color) && sut.getFont().equals(font);
    }

    /**
     * このクラスのインスタンスを作成する際のデフォルトフォントを取得する.
     * @return デフォルトフォント
     */
    @Nonnull
    public static Font getDefaultFont() {
        return defaultFont;
    }

    /**
     * このクラスのインスタンスを作成する際のデフォルトフォントを指定する.
     * 変更するとそれ以降にオブジェクトを新しく生成した際に適用される.
     * @param defaultFont デフォルトフォント
     */
    public static void setDefaultFont(Font defaultFont) {
        if(defaultFont == null)
            throw new IllegalArgumentException("defaultFont must not be null");
        ColoredFont.defaultFont = defaultFont;
    }

    /**
     * このクラスのインスタンスを作成する際のデフォルトカラーを取得する.
     * @return デフォルトカラー
     */
    @Nonnull
    public static Color getDefaultColor() {
        return defaultColor;
    }
    
    /**
     * このクラスのインスタンスを作成する際のデフォルトカラーを指定する.
     * 変更するとそれ以降にオブジェクトを新しく生成した際に適用される.
     * @param defaultColor デフォルトカラー
     */
    public static void setDefaultColor(Color defaultColor) {
        if(defaultColor == null)
            throw new IllegalArgumentException("dafaultColor must not be null");
        ColoredFont.defaultColor = defaultColor;
    }
}
