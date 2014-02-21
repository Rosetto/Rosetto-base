package info.rosetto.models.text;

import java.io.Serializable;

import org.frows.puregds.string.StyledString;
import org.frows.puregds.string.TextPositioning;

public class LineProgress implements Serializable {
    private static final long serialVersionUID = 1507596084184729852L;

    /**
     * 文字送り中の行
     */
    private Line line;
    
    /**
     * 文字送り中のインデックス
     */
    private int writingIndex = 0;
    
    public LineProgress(Line line) {
        this.line = line;
    }
    
    public void addFirst(StyledString...str) {
        line.addFirst(str);
    }
    
    public void addLast(StyledString...str) {
        line.add(str);
    }

    public void setPositioning(TextPositioning positioning) {
        this.line.setPositioning(positioning);
    }

    public int getWritingIndex() {
        return writingIndex;
    }

    public Line getLine() {
        return line;
    }
    
    @Override
    public String toString() {
        return line.getAllText().substring(0, writingIndex);
    }
    
    /**
     * writingIndexを一つ進める.
     * 行の終わりだった場合はfalseを返す.
     * 行の終わりだった場合はキューから取り出して次の行へ進む.
     * キューからの取り出しにも失敗した場合（キューが空）はfalseを返して何もしない
     */
    public boolean nextChar() {
        if(isEnd()) {
            return false;
        } else {
            writingIndex++;
            return true;
        }
    }
    public String getWrittenString() {
        return line.getAllText().substring(0, writingIndex);
    }
    
    public char getCurrentChar() {
        return line.getAllText().charAt(writingIndex-1);
    }
    
    public void clearWrittenString() {
        line = line.split(writingIndex)[1];
        writingIndex = 0;
    }
    
    public boolean isEmptyLine() {
        return line.getTextLength() == 0;
    }

    /**
     * 現在のwritingLineが書き終わっているかを返す.
     */
    public boolean isEnd() {
        return (writingIndex >= line.getTextLength());
    }
}
