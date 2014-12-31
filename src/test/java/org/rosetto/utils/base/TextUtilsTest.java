package org.rosetto.utils.base;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.rosetto.utils.base.TextUtils;

public class TextUtilsTest {

    @Test
    public void createTagRegexTest() throws Exception {
        String sut = TextUtils.createTagRegex("[", "]");
        assertThat(sut, is("\\[(.*?)\\]"));
    }
    
    @Test
    public void regexEscapeTest() throws Exception {
        
        assertThat(TextUtils.escapeForRegex("[]"), is("\\[\\]"));
        assertThat(TextUtils.escapeForRegex("[* [+ $x @a]]"), is("\\[\\* \\[\\+ \\$x @a\\]\\]"));
    }
    
    @Test
    public void containsCountTest() throws Exception {
         assertThat(TextUtils.containsCount("Hello, World!", 'l'), is(3));
    }
    
    @Test
    public void removeDoubleQuoteTest() throws Exception {
        assertThat(TextUtils.removeDoubleQuote("\"hello!\""), is("hello!")); 
        assertThat(TextUtils.removeDoubleQuote("not quoted"), is("not quoted")); 
    }

}
