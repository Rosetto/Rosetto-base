package info.rosetto.models.text.style;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import info.rosetto.models.text.style.LineBounds;

import org.junit.Test;

/**
 * OK(2013/05/26)
 * @author tohhy
 */
public class LineBoundsTest {

    @Test
    public void インスタンス化テスト() {
        LineBounds sut1 = new LineBounds(10, 20);
        assertThat(sut1.getWidth(), is(10));
        assertThat(sut1.getHeight(), is(20));
        LineBounds sut2 = new LineBounds(10);
        assertThat(sut2.getWidth(), is(-1));
        assertThat(sut2.getHeight(), is(10));
    }
    
    @Test
    public void 負の値はマイナス1に丸められる() throws Exception {
        LineBounds sut1 = new LineBounds(-10, -20);
        assertThat(sut1.getWidth(), is(-1));
        assertThat(sut1.getHeight(), is(-1));
        LineBounds sut2 = new LineBounds(-100);
        assertThat(sut2.getWidth(), is(-1));
        assertThat(sut2.getHeight(), is(-1));
    }
    
    @Test
    public void マイナス値を指定したときfixedはfalse() throws Exception {
        LineBounds sut1 = new LineBounds(10, 20);
        assertThat(sut1.isWidthFixed(), is(true));
        assertThat(sut1.isHeightFixed(), is(true));
        
        LineBounds sut2 = new LineBounds(-1, 2);
        assertThat(sut2.isWidthFixed(), is(false));
        assertThat(sut2.isHeightFixed(), is(true));
        
        LineBounds sut3 = new LineBounds(3, -2);
        assertThat(sut3.isWidthFixed(), is(true));
        assertThat(sut3.isHeightFixed(), is(false));
        
        LineBounds sut4 = new LineBounds(-1, -2);
        assertThat(sut4.isWidthFixed(), is(false));
        assertThat(sut4.isHeightFixed(), is(false));
    }

}
