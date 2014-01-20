/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.text;

import info.rosetto.models.text.style.LineBounds;
import info.rosetto.models.text.style.Spacing;
import info.rosetto.models.text.style.TextStyle;
import info.rosetto.models.text.style.TextPositioning.Align;

import java.io.Serializable;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import org.frows.puregds.color.Color;
import org.frows.puregds.font.Font;

/**
 * 画面描画用のスタイル情報を持つ文字列.
 * MessageText中に保持され、文字送りに応じて画面に描画される.
 * @author tohhy
 */
@Immutable
public class StyledString implements Serializable {
    private static final long serialVersionUID = -6776089219872168018L;
    
    /**
     * スタイルを付加する文字列.
     */
    @Nonnull
    private final String string;
    
    /**
     * 付加するスタイル.
     */
    @Nonnull
    private final TextStyle style;
    
    /**
     * 指定文字列と指定スタイルでStyledStringを初期化する.
     * 文字列がnullの場合は空文字、スタイルがnullの場合はTextStyle.DEFAULTが保持される.
     * @param string 保持する文字列
     * @param style 保持するスタイル
     */
    public StyledString(String string, TextStyle style) {
        this.string = (string == null) ? "" : string;
        this.style = (style == null) ? TextStyle.DEFAULT : style;
    }
    
    /**
     * 指定文字列でStyledStringを初期化する.
     * スタイルにはTextStyle.DEFAULTが保持される.
     * 文字列がnullの場合は空文字が保持される.
     * @param string 保持する文字列
     */
    public StyledString(String string) {
        this(string, null);
    }
    
    /**
     * stringをそのまま返す.
     */
    @Override
    public String toString() {
        return string;
    }
    
    /**
     * このStyledStringが保持する文字列を返す.
     * @return このStyledStringが保持する文字列
     */
    @Nonnull
    public String getString() {
        return string;
    }

    /**
     * このStyledStringが保持するフォント情報を返す.
     * @return このStyledStringが保持するフォント情報
     */
    @Nonnull
    public Font getFont() {
        return getStyle().getFont();
    }

    /**
     * このStyledStringが保持するフォント色情報を返す.
     * @return このStyledStringが保持するフォント色情報
     */
    @Nonnull
    public Color getFontColor() {
        return getStyle().getColor();
    }

    /**
     * このStyledStringが保持するスペーシング情報を返す.
     * @return このStyledStringが保持するスペーシング情報
     */
    @Nonnull
    public Spacing getSpacing() {
        return getStyle().getSpacing();
    }

    /**
     * このStyledStringが保持する文字寄せ情報を返す.
     * @return このStyledStringが保持する文字寄せ情報
     */
    @Nonnull
    public Align getAlign() {
        return getStyle().getAlign();
    }

    /**
     * このStyledStringが保持する行サイズ情報を返す.
     * @return このStyledStringが保持する行サイズ情報
     */
    @Nonnull
    public LineBounds getLineSize() {
        return getStyle().getLineSize();
    }

    /**
     * このStyledStringが保持するスタイル情報を返す.
     * @return このStyledStringが保持する行サイズ情報
     */
    @Nonnull
    public TextStyle getStyle() {
        return style;
    }
}
