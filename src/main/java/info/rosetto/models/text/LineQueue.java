package info.rosetto.models.text;

import info.rosetto.models.text.style.TextStyle;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 画面表示する文字列のリスト.
 * @author tohhy
 */
public class LineQueue implements Serializable {
    private static final long serialVersionUID = 1800107012645343360L;
    private final LinkedList<Line> list = new LinkedList<Line>();
    
    public void add(Line line) {
        list.add(line);
    }
    
    public int size() {
        return list.size();
    }
    
    public Line get(int index) {
        return list.get(index);
    }
    
    public void clear() {
        list.clear();
    }
    
    public Line peekLast() {
        return list.peekLast();
    }

    public Line pollLast() {
        return list.pollLast();
    }
    
    public void addAll(LineQueue lines) {
        list.addAll(lines.list);
    }
    
    public void addAll(List<Line> lines) {
        list.addAll(lines);
    }
    
    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    public Line pop() {
        return list.pop();
    }
    
    /**
     * 指定したスタイルを適用した空の行を追加する.
     */
    public void enqueueNewLine(TextStyle lineStyle) {
        list.add(new Line(lineStyle.getPositioning()));
    }
}
