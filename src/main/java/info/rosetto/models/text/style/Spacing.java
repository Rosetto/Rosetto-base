package info.rosetto.models.text.style;

import java.io.Serializable;

/**
 * 文字表示の際のインデントと行間と字間の情報を保持する.
 * イミュータブル.
 * @author tohhy
 */
public class Spacing implements Serializable {

    /**
     * 0.1.0 (2013/09/08)
     */
    private static final long serialVersionUID = -6879843368199066814L;

    /**
     * 文字表示の際のインデント幅.
     */
    private final int indent;
    
    /**
     * 文字表示の際の行間.
     */
    private final int lineSpacing;
    
    /**
     * 文字表示の際の字間.
     */
    private final int letterSpacing;
    
    /**
     * デフォルトのSpacing. インデントと字間なし、行間5px.
     */
    public static final Spacing DEFAULT = new Spacing(0, 5, 0);
    
    /**
     * インデント、行間、字間の値を指定してSpacingを初期化する.
     * @param indent 行頭空けのピクセル数
     * @param lineSpacing 行間のピクセル数
     * @param letterSpacing 字間のピクセル数
     */
    public Spacing(int indent, int lineSpacing, int letterSpacing) {
        this.indent = indent;
        this.lineSpacing = lineSpacing;
        this.letterSpacing = letterSpacing;
    }
    
    /**
     * indentのみを変更したこのSpacingの新しいインスタンスを返す.
     */
    public Spacing deriveByIndent(int indent) {
        if(indent == this.indent)
            return this;
        return new Spacing(indent, lineSpacing, letterSpacing);
    }
    
    /**
     * lineSpacingのみを変更したこのSpacingの新しいインスタンスを返す.
     */
    public Spacing deriveByLineSpacing(int lineSpacing) {
        if(lineSpacing == this.lineSpacing)
            return this;
        return new Spacing(indent, lineSpacing, letterSpacing);
    }
    
    /**
     * letterSpacingのみを変更したこのSpacingの新しいインスタンスを返す.
     */
    public Spacing deriveByLetterSpacing(int letterSpacing) {
        if(letterSpacing == this.letterSpacing)
            return this;
        return new Spacing(indent, lineSpacing, letterSpacing);
    }

    /**
     * 行頭インデントのピクセル数を返す.
     */
    public int getIndent() {
        return indent;
    }

    /**
     * 行間のピクセル数を返す.
     */
    public int getLineSpacing() {
        return lineSpacing;
    }

    /**
     * 字間のピクセル数を返す.
     */
    public int getLetterSpacing() {
        return letterSpacing;
    }
    
    /**
     * 文字列表現を返す.
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[indent: ").append(indent)
                .append(", lineSpacing: ").append(lineSpacing)
                .append(", letterSpacing: ").append(letterSpacing).append("]")
                .toString();
    }
}
