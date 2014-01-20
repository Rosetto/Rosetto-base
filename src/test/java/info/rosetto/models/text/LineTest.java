package info.rosetto.models.text;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class LineTest {
    private final StyledString[] ss = {
            new StyledString("foo"),
            new StyledString("bar"),
            new StyledString("baz"),
            new StyledString("hoge"),
            new StyledString("fuga"),
    };
    
    @Test
    public void addとaddFirstで文字列を追加できる() throws Exception {
        Line sut = new Line();
        assertThat(sut.size(), is(0));
        assertThat(sut.getTextLength(), is(0));
        
        sut.add(new StyledString(""));
        assertThat(sut.size(), is(1));
        assertThat(sut.getTextLength(), is(0));
        
        sut.add(new StyledString("foo"));
        assertThat(sut.size(), is(2));
        assertThat(sut.getTextLength(), is(3));
        assertThat(sut.getList().get(0).getString(), is(""));
        assertThat(sut.getList().get(1).getString(), is("foo"));
        
        sut.addFirst(new StyledString("bar"), new StyledString("baz"));
        assertThat(sut.size(), is(4));
        assertThat(sut.getTextLength(), is(9));
        assertThat(sut.getList().get(0).getString(), is("bar"));
        assertThat(sut.getList().get(1).getString(), is("baz"));
        assertThat(sut.getList().get(2).getString(), is(""));
        assertThat(sut.getList().get(3).getString(), is("foo"));
    }
    
    @Test
    public void splitで負の値や範囲外はエラー() throws Exception {
        Line line = new Line();
        line.add(ss);
        //負の値
        try {
            line.split(-1);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        //範囲外
        try {
            line.split(line.getTextLength()+1);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

    @Test
    public void splitで行を分割できる() {
        Line line = new Line();
        line.add(ss);
        int len = line.getTextLength();
        String all = line.getAllText();
        //0で分割すると[空, 元]
        Line[] sut1 = line.split(0);
        assertThat(sut1[0].getTextLength(), is(0));
        assertThat(sut1[1], is(line));
        //末尾までで分割すると[元, 空]
        Line[] sut2 = line.split(line.getTextLength());
        assertThat(sut2[0], is(line));
        assertThat(sut2[1].getTextLength(), is(0));
        
        Line[] sut3 = line.split(1);
        assertThat(sut3[0].getAllText(), is("f"));
        assertThat(sut3[1].getAllText(), is("oobarbazhogefuga"));
        
        Line[] sut4 = line.split(2);
        assertThat(sut4[0].getAllText(), is("fo"));
        assertThat(sut4[1].getAllText(), is("obarbazhogefuga"));
        
        Line[] sut5 = line.split(3);
        assertThat(sut5[0].getAllText(), is("foo"));
        assertThat(sut5[1].getAllText(), is("barbazhogefuga"));
        
        //全てチェック
        for(int i=4; i<len; i++) {
            Line[] sut = line.split(i);
            assertThat(sut[0].getAllText(), is(all.substring(0, i)));
            assertThat(sut[1].getAllText(), is(all.substring(i)));
        }
        
    }

    @Test
    public void toStringで全文字列を結合した値が返る() throws Exception {
        Line line = new Line();
        line.add(ss);
        String all = "foobarbazhogefuga";
        assertThat(line.toString(), is(all));
        assertThat(line.getAllText(), is(all));
    }
    
    @Test
    public void splitで空行を処理できる() throws Exception {
        Line line = new Line();
        //0で分割すると[空, 空]
        Line[] sut1 = line.split(0);
        assertThat(sut1[0].getTextLength(), is(0));
        assertThat(sut1[1].getTextLength(), is(0));
        //負の値
        try {
            line.split(-1);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        //範囲外
        try {
            line.split(1);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
}
