package info.rosetto.models.text.style;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.models.text.style.LineBounds;
import info.rosetto.models.text.style.TextBounding;

import org.junit.Test;

/**
 * OK(2013/05/26)
 * @author tohhy
 */
public class TextBoundingTest {

    @Test
    public void インスタンス化テスト() {
        TextBounding sut1 = new TextBounding(new LineBounds(10, 10), true);
        assertThat(sut1.getLineBounds().getWidth(), is(10));
        assertThat(sut1.isAutoReturn(), is(true));
        TextBounding sut2 = new TextBounding(null, false);
        assertThat(sut2.getLineBounds().getWidth(), is(-1));
        assertThat(sut2.isAutoReturn(), is(false));
    }
    
    @Test
    public void deriveで派生できる() throws Exception {
        TextBounding sut1 = new TextBounding(new LineBounds(10, 10), true);
        assertThat(sut1.getLineBounds().getWidth(), is(10));
        assertThat(sut1.isAutoReturn(), is(true));
        TextBounding sut2 = sut1.derive(LineBounds.FREE);
        assertThat(sut2.getLineBounds().getWidth(), is(-1));
        assertThat(sut2.isAutoReturn(), is(true));
        //同じ値で派生しても同じインスタンス
        TextBounding sut3 = sut2.derive(LineBounds.FREE);
        assertThat(sut2 == sut3, is(true));
    }

}
