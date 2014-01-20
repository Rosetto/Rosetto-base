package info.rosetto.models.text.style;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import info.rosetto.models.text.style.ColoredFont;
import info.rosetto.models.text.style.TextPositioning;
import info.rosetto.models.text.style.TextStyle;

import org.junit.Test;

public class TextStyleTest {
    
    @Test
    public void インスタンス化テスト() throws Exception {
        TextStyle sut1 = new TextStyle(ColoredFont.DEFAULT, TextPositioning.DEFAULT);
        assertThat(sut1.getColoredFont(), is(TextStyle.DEFAULT.getColoredFont()));
        assertThat(sut1.getPositioning(), is(TextStyle.DEFAULT.getPositioning()));
    }


}
