package info.rosetto.models.text.style;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import info.rosetto.models.text.style.LineBounds;
import info.rosetto.models.text.style.Spacing;
import info.rosetto.models.text.style.TextBounding;
import info.rosetto.models.text.style.TextPositioning;
import info.rosetto.models.text.style.TextPositioning.Align;

import org.junit.Test;

public class TextPositioningTest {
    Spacing spacing = new Spacing(10, 20, 30);
    Align align = Align.CENTER;
    TextBounding bounding = new TextBounding(new LineBounds(10), true);

    @Test
    public void インスタンス化テスト() throws Exception {
         TextPositioning sut1 = new TextPositioning(Spacing.DEFAULT, Align.LEFT, TextBounding.DEFAULT);
         assertThat(sut1.getSpacing(), is(TextPositioning.DEFAULT.getSpacing()));
         assertThat(sut1.getAlign(), is(TextPositioning.DEFAULT.getAlign()));
         assertThat(sut1.getBounding(), is(TextPositioning.DEFAULT.getBounding()));
         
         TextPositioning sut2 = new TextPositioning(spacing, align, bounding);
         assertThat(sut2.getSpacing(), is(spacing));
         assertThat(sut2.getAlign(), is(align));
         assertThat(sut2.getBounding(), is(bounding));
    }
    
    @Test
    public void deriveで派生できる() throws Exception {
        TextPositioning sut1 = new TextPositioning(Spacing.DEFAULT, Align.LEFT, TextBounding.DEFAULT);
        TextPositioning sut2 = sut1.derive(spacing);
        
        //sut1には変化なし
        assertThat(sut1.getSpacing(), is(TextPositioning.DEFAULT.getSpacing()));
        assertThat(sut1.getAlign(), is(TextPositioning.DEFAULT.getAlign()));
        assertThat(sut1.getBounding(), is(TextPositioning.DEFAULT.getBounding()));
        //sut2はspacingのみ変化
        assertThat(sut2.getSpacing(), is(spacing));
        assertThat(sut2.getAlign(), is(TextPositioning.DEFAULT.getAlign()));
        assertThat(sut2.getBounding(), is(TextPositioning.DEFAULT.getBounding()));
        
        TextPositioning sut3 = sut2.derive(align);
        TextPositioning sut4 = sut3.derive(bounding);
        assertThat(sut4.getSpacing(), is(spacing));
        assertThat(sut4.getAlign(), is(align));
        assertThat(sut4.getBounding(), is(bounding));
        
        //同じ要素で派生しても変化なし
        TextPositioning sut5 = sut4.derive(bounding).derive(spacing).derive(align);
        assertThat(sut5, is(sut4));
    }

}
