package info.rosetto.models.text;

import info.rosetto.models.text.style.TextStyle;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class NovelText implements Serializable {
    private static final long serialVersionUID = -9167958065397367478L;
    //デフォルトのスタイル
    private TextStyle defaultStyle = TextStyle.DEFAULT;
    //現在適用されているスタイル
    private TextStyle style = TextStyle.DEFAULT;
    //既に最後まで文字送りされ、画面に直接表示されている行
    private final LinkedList<Line> writtenLines = new LinkedList<Line>();
    //現在文字送り途中の行
    private LineProgress writing;
    
    public LineProgress getWriting() {
        if(writing == null) writing = new LineProgress(new Line(style.getPositioning()));
        return writing;
    }
    
    public void setWriting(Line line) {
        writing = new LineProgress(line);
    }
    
    public void resetWriting() {
        setWriting(new Line(style.getPositioning()));
    }
    
    /**
     * このテキストの中身が描画待ちキューも含めて完全に空であるかどうかを返す.
     * @return
     */
    public boolean isEmpty() {
        return (writtenLines.isEmpty() && writing.isEmptyLine());
    }
    
    /**
     * フォントとフォント色をデフォルトの値にリセットする.
     */
    public void resetFont() {
        this.style = style
                .derive(defaultStyle.getColoredFont().getFont())
                .derive(defaultStyle.getColoredFont().getColor());
    }
    
    
    /**
     * このメッセージテキストにすでに書き込まれた内容をクリアする.
     * 描画行の描画済みの部分と、描画済み行が消去される.
     */
    public void clear() {
        if(writing != null)
            writing.clearWrittenString();
        writtenLines.clear();
    }
    
    
    /**
     * このテキストに文字を書き込む.
     * 文字送りを行うためにキューに蓄積され、nextCharで取り出されるまで反映されない.
     */
    public void write(String s) {
        StyledString styled = new StyledString(s, this.getStyle());
        getWriting().addLast(styled);
    }
    

    /**
     * このテキストが書き込み終わっているかを返す.
     * @return このテキストが書き込み終わっているか
     */
    public boolean isWriteFinished() {
        return (getWriting().isEnd());
    }
    

    /**
     * フォント以外のスタイルをデフォルトの値にリセットする.
     */
    public void resetStyle() {
        this.style = style.derive(defaultStyle.getPositioning());
    }

    /**
     * 指定した描画済み行と描画行でこのテキストをリセットする.
     * @param written
     * @param writing
     */
    public void resetText(List<Line> written, Line writing) {
        this.writtenLines.clear();
        this.writtenLines.addAll(written);
        this.writing = new LineProgress(writing);
    }

    /**
     * このテキストのすでに書かれた内容が空であるかどうかを返す.
     * @return
     */
    public boolean isWrittenEmpty() {
        return (writtenLines.isEmpty() && writing.getWritingIndex() == 0);
    }
    
    /**
     * 現在描画中の行を返す.
     * もし描画中の行が存在しなければ描画待ちキューの行を取り出し描画行にして返す.
     * 描画待ちキューも空であれば新しい行を生成し描画行にして返す.
     */
    public Line getWritingLine() {
        return getWriting().getLine();
    }

    /**
     * 現在描画中の行の何文字目を描画しているかを返す.
     * メッセージレイヤの文字送り処理に用いる.
     */
    public int getWritingIndex() {
        return getWriting().getWritingIndex();
    }

    /**
     * このテキストに書き込まれている全ての文字列を結合して返す.
     * @return このテキストに書き込まれている全ての文字列
     */
    public String getWrittenString() {
        StringBuilder result = new StringBuilder();
        for(Line l : writtenLines) {
            result.append(l.getAllText());
        }
        result.append(getWriting().getWrittenString());
        return result.toString();
    }
    
    
    /**
     * 描画済みの行のリストを返す.
     */
    public List<Line> getWrittenLines() {
        return writtenLines;
    }
    
    

    public TextStyle getDefaultStyle() {
        return defaultStyle;
    }

    public void setDefaultStyle(TextStyle defaultStyle) {
        this.defaultStyle = defaultStyle;
    }

    public TextStyle getStyle() {
        return style;
    }

    public void setStyle(TextStyle style) {
        this.style = style;
        getWriting().setPositioning(style.getPositioning());
    }
    

}
