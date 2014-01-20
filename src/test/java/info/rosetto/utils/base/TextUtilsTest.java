package info.rosetto.utils.base;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.utils.base.TextUtils;

import org.junit.Test;

public class TextUtilsTest {


    @Test
    public void createTagRegexで指定タグ全体にマッチする正規表現を生成できる() throws Exception {
        String sut = TextUtils.createTagRegex("[", "]");
        assertThat(sut, is("\\[(.*?)\\]"));
    }
    
    @Test
    public void regexEscapeTest() throws Exception {
        
        String sut = TextUtils.escapeForRegex("[]");
        assertThat(sut, is("\\[\\]"));
    }

}
