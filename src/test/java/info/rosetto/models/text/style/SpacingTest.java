package info.rosetto.models.text.style;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import info.rosetto.models.text.style.Spacing;

import org.junit.Test;

/**
 * OK(2013/05/26)
 * @author tohhy
 */
public class SpacingTest {

    @Test
    public void インスタンス化テスト() {
        Spacing sut = new Spacing(0, 0, 0);
        assertThat(sut.getIndent(), is(0));
        assertThat(sut.getLineSpacing(), is(0));
        assertThat(sut.getLetterSpacing(), is(0));
    }
    
    @Test
    public void toStringで文字列表現が返る() throws Exception {
        Spacing sut = new Spacing(1, 2, 3);
        String result = "[indent: 1, lineSpacing: 2, letterSpacing: 3]";
        assertThat(sut.toString(), is(result));
    }
    
    @Test
    public void deriveで派生できる() throws Exception {
        Spacing sut = new Spacing(1, 2, 3);
        assertThat(sut.getIndent(), is(1));
        assertThat(sut.getLineSpacing(), is(2));
        assertThat(sut.getLetterSpacing(), is(3));
        
        Spacing sut2 = sut.deriveByIndent(10);
        //大本には影響なし
        assertThat(sut.getIndent(), is(1));
        assertThat(sut.getLineSpacing(), is(2));
        assertThat(sut.getLetterSpacing(), is(3));
        //派生後は変化
        assertThat(sut == sut2, is(false));
        assertThat(sut2.getIndent(), is(10));
        assertThat(sut2.getLineSpacing(), is(2));
        assertThat(sut2.getLetterSpacing(), is(3));
        
        Spacing sut3 = sut2.deriveByLineSpacing(20).deriveByLetterSpacing(30);
        assertThat(sut3.getIndent(), is(10));
        assertThat(sut3.getLineSpacing(), is(20));
        assertThat(sut3.getLetterSpacing(), is(30));
        
        //同じ値を指定しても変化しない
        Spacing sut4 = sut3.deriveByIndent(10).deriveByLineSpacing(20).deriveByLetterSpacing(30);
        assertThat(sut3 == sut4, is(true));
    }

}
