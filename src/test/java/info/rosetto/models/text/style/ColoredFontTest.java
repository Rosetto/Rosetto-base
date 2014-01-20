package info.rosetto.models.text.style;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.models.text.style.ColoredFont;

import org.frows.puregds.color.Color;
import org.frows.puregds.font.Font;
import org.frows.puregds.font.FontFamily;
import org.frows.puregds.font.FontStyle;
import org.junit.Test;

/**
 * OK(2013/05/26)
 * @author tohhy
 */
public class ColoredFontTest {

    @Test
    public void インスタンス化テスト() {
        Font f = new Font(FontFamily.SERIF, FontStyle.BOLD, 20);
        Color c = Color.RED;
        Font df = ColoredFont.getDefaultFont();
        Color dc = ColoredFont.getDefaultColor();
        ColoredFont sut1 = new ColoredFont(f, c);
        assertThat(sut1.getFont(), is(f));
        assertThat(sut1.getColor(), is(c));
        
        ColoredFont sut2 = new ColoredFont(null, c);
        assertThat(sut2.getFont(), is(df));
        assertThat(sut2.getColor(), is(c));
        
        ColoredFont sut3 = new ColoredFont(f, null);
        assertThat(sut3.getFont(), is(f));
        assertThat(sut3.getColor(), is(dc));
        
        ColoredFont sut4 = new ColoredFont(null, null);
        assertThat(sut4.getFont(), is(df));
        assertThat(sut4.getColor(), is(dc));
        
        ColoredFont sut5 = new ColoredFont((Color)null);
        assertThat(sut5.getFont(), is(df));
        assertThat(sut5.getColor(), is(dc));
        
        ColoredFont sut6 = new ColoredFont(c);
        assertThat(sut6.getFont(), is(df));
        assertThat(sut6.getColor(), is(c));
        
        ColoredFont sut7 = new ColoredFont((Font)null);
        assertThat(sut7.getFont(), is(df));
        assertThat(sut7.getColor(), is(dc));
        
        ColoredFont sut8 = new ColoredFont(f);
        assertThat(sut8.getFont(), is(f));
        assertThat(sut8.getColor(), is(dc));
        
        //4,5,7は同じ
        assertThat(sut4, is(sut5));
        assertThat(sut5, is(sut7));
        
        //2,6は同じ
        assertThat(sut2, is(sut6));
        
        //3,8は同じ
        assertThat(sut3, is(sut8));
    }
    
    @Test
    public void equalsで中身が等価なオブジェクトを判別できる() throws Exception {
        Font f = new Font(FontFamily.SERIF, FontStyle.BOLD, 20);
        Color c = Color.RED;
        ColoredFont sut = new ColoredFont(f, c);
        assertThat(sut.equals(new String("型の違うオブジェクト")), is(false));
        assertThat(sut.equals(sut), is(true));
        assertThat(sut.equals(new ColoredFont(null, null)), is(false));
        assertThat(sut.equals(new ColoredFont(f, null)), is(false));
        assertThat(sut.equals(new ColoredFont(null, c)), is(false));
    }
    
    @Test
    public void デフォルトを指定できる() throws Exception {
        //記録
        Color defaultColor = ColoredFont.getDefaultColor();
        Font defaultFont = ColoredFont.getDefaultFont();
        
        ColoredFont.setDefaultColor(Color.AQUA); 
        ColoredFont sut1 = new ColoredFont(defaultFont);
        assertThat(sut1.getColor(), is(Color.AQUA));
        ColoredFont.setDefaultColor(Color.BROWN);
        assertThat(sut1.getColor(), is(Color.AQUA));
        ColoredFont sut2 = new ColoredFont(defaultFont);
        assertThat(sut2.getColor(), is(Color.BROWN));
        
        Font f1 = new Font("Ariel", FontStyle.ITALIC, 30);
        Font f2 = new Font("Monospaced", FontStyle.BOLD, 10);
        ColoredFont.setDefaultFont(f1); 
        ColoredFont sut3 = new ColoredFont(defaultColor);
        assertThat(sut3.getFont(), is(f1));
        ColoredFont.setDefaultFont(f2);
        assertThat(sut3.getFont(), is(f1));
        ColoredFont sut4 = new ColoredFont(defaultColor);
        assertThat(sut4.getFont(), is(f2));
        
        //復元
        ColoredFont.setDefaultColor(defaultColor);
        ColoredFont.setDefaultFont(defaultFont);
    }
    
    @Test
    public void deriveで色を変更して派生できる() throws Exception {
        Font f = new Font(FontFamily.SERIF, FontStyle.BOLD, 20);
        Color c = Color.RED;
        Color c2 = Color.BLUE;
        ColoredFont sut = new ColoredFont(f, c);
        assertThat(sut.getFont(), is(f));
        assertThat(sut.getColor(), is(c));
        
        //色で派生
        ColoredFont cderived = sut.derive(c2);
        //非破壊なので元々のオブジェクトは変化しない
        assertThat(sut.getFont(), is(f));
        assertThat(sut.getColor(), is(c));
        //派生後は別オブジェクト
        assertThat((cderived == sut), is(false));
        assertThat(cderived.getFont(), is(f));
        assertThat(cderived.getColor(), is(c2));
    }
    
    @Test
    public void deriveでフォントを変更して派生できる() throws Exception {
        Font f = new Font(FontFamily.SERIF, FontStyle.BOLD, 20);
        Color c = Color.RED;
        Font f2 = new Font(FontFamily.SANS_SERIF, FontStyle.ITALIC, 10);
        ColoredFont sut = new ColoredFont(f, c);
        assertThat(sut.getFont(), is(f));
        assertThat(sut.getColor(), is(c));
        
        //フォントで派生
        ColoredFont fderived = sut.derive(f2);
        //非破壊なので元々のオブジェクトは変化しない
        assertThat(sut.getFont(), is(f));
        assertThat(sut.getColor(), is(c));
        //派生後は別オブジェクト
        assertThat((fderived == sut), is(false));
        assertThat(fderived.getFont(), is(f2));
        assertThat(fderived.getColor(), is(c));
        
        //同じもので派生してもオブジェクトは変わらない
        assertThat((sut.derive(sut.getColor()) == sut), is(true));
        assertThat((sut.derive(sut.getFont()) == sut), is(true));
        
    }
    
    
    @Test
    public void deriveでフォント要素を変更して派生できる() throws Exception {
        Font f = new Font(FontFamily.SERIF, FontStyle.BOLD, 20);
        Color c = Color.RED;
        ColoredFont sut = new ColoredFont(f, c);
        assertThat(sut.getFont(), is(f));
        assertThat(sut.getColor(), is(c));
        
        //フォントサイズで派生
        ColoredFont sderived = sut.derive(30);
        //非破壊なので元々のオブジェクトは変化しない
        assertThat(sut.getFont(), is(f));
        assertThat(sut.getColor(), is(c));
        //派生後は別オブジェクト
        assertThat((sderived == sut), is(false));
        assertThat(sderived.getFontSize(), is(30));
        assertThat(sderived.getFontFamily(), is(f.getFamily()));
        assertThat(sderived.getStyle(), is(f.getStyle()));
        
        ColoredFont fderived = sut.derive(new FontFamily("Meiryo"));
        assertThat((fderived == sut), is(false));
        assertThat(fderived.getFontSize(), is(f.getSize()));
        assertThat(fderived.getFontFamily(), is("Meiryo"));
        assertThat(fderived.getStyle(), is(f.getStyle()));
        
        ColoredFont fsderived = sut.derive("MS GOTHIC");
        assertThat((fderived == sut), is(false));
        assertThat(fsderived.getFontSize(), is(f.getSize()));
        assertThat(fsderived.getFontFamily(), is("MS GOTHIC"));
        assertThat(fsderived.getStyle(), is(f.getStyle()));
        
        ColoredFont stderived = sut.derive(FontStyle.BOLD_ITALIC);
        assertThat((stderived == sut), is(false));
        assertThat(stderived.getFontSize(), is(f.getSize()));
        assertThat(stderived.getFontFamily(), is(f.getFamily()));
        assertThat(stderived.getStyle(), is(FontStyle.BOLD_ITALIC));
        
    }
}
