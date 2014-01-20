package info.rosetto.models.text;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.models.text.style.ColoredFont;
import info.rosetto.models.text.style.LineBounds;
import info.rosetto.models.text.style.Spacing;
import info.rosetto.models.text.style.TextBounding;
import info.rosetto.models.text.style.TextPositioning;
import info.rosetto.models.text.style.TextStyle;
import info.rosetto.models.text.style.TextPositioning.Align;

import org.frows.puregds.color.Color;
import org.frows.puregds.font.Font;
import org.frows.puregds.font.FontFamily;
import org.frows.puregds.font.FontStyle;
import org.junit.Test;

/**
 * OK(2013/05/26)
 * @author tohhy
 */
public class StyledStringTest {

    @Test
    public void インスタンス化テスト() throws Exception {
        TextStyle style = new TextStyle(new ColoredFont(Color.RED), TextPositioning.DEFAULT);
        StyledString sut1 = new StyledString("test", style);
        assertThat(sut1.getString(), is("test"));
        assertThat(sut1.getFontColor(), is(Color.RED));
        
        StyledString sut2 = new StyledString("test", null);
        assertThat(sut2.getString(), is("test"));
        assertThat(sut2.getFontColor(), is(ColoredFont.DEFAULT.getColor()));
        
        StyledString sut3 = new StyledString(null, style);
        assertThat(sut3.getString(), is(""));
        assertThat(sut3.getFontColor(), is(Color.RED));
        
        StyledString sut4 = new StyledString(null, null);
        assertThat(sut4.getString(), is(""));
        assertThat(sut4.getFontColor(), is(ColoredFont.DEFAULT.getColor()));
        
        StyledString sut5 = new StyledString("test");
        assertThat(sut5.getString(), is("test"));
        assertThat(sut5.getFontColor(), is(ColoredFont.DEFAULT.getColor()));
        
        StyledString sut6 = new StyledString(null);
        assertThat(sut6.getString(), is(""));
        assertThat(sut6.getFontColor(), is(ColoredFont.DEFAULT.getColor()));
    }
    
    @Test
    public void スタイル情報をgetterから取得できる() throws Exception {
        TextStyle style = new TextStyle(
                new ColoredFont(new Font(FontFamily.SERIF, FontStyle.BOLD, 20), Color.RED),
                new TextPositioning(
                        new Spacing(2, 5, 10), 
                        Align.CENTER, 
                        new TextBounding(new LineBounds(5), false)
                        ));
        StyledString sut = new StyledString("test", style);
        assertThat(sut.getString(), is("test"));
        assertThat(sut.getAlign(), is(style.getAlign()));
        assertThat(sut.getFont(), is(style.getFont()));
        assertThat(sut.getFontColor(), is(style.getColor()));
        assertThat(sut.getLineSize(), is(style.getLineSize()));
        assertThat(sut.getSpacing(), is(style.getSpacing()));
    }
    
    @Test
    public void toStringで文字列と同じ内容が返る() throws Exception {
        StyledString sut = new StyledString("文字列");
        assertThat(sut.toString(), is(sut.getString()));
    }

}
