package info.rosetto.utils.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class ParserUtilsTest {

    @Test
    public void 単純なスクリプトのパース() throws Exception {
         List<String> sut1 = ParserUtils.splitScript("");
         assertThat(sut1.size(), is(0));
         
         List<String> sut2 = ParserUtils.splitScript("hoge");
         assertThat(sut2.size(), is(1));
         assertThat(sut2.get(0), is("hoge"));
         
         List<String> sut3 = ParserUtils.splitScript("hoge[br]fuga[p]");
         assertThat(sut3.size(), is(2));
         assertThat(sut3.get(0), is("hoge[br]"));
         assertThat(sut3.get(1), is("fuga[p]"));
    }
    
    @Test
    public void ネストした関数呼び出しのパース() throws Exception {
        List<String> sut1 = ParserUtils.splitScript("[print [getg x]]");
        assertThat(sut1.size(), is(1));
        assertThat(sut1.get(0), is("[print [getg x]]"));
        
        List<String> sut2 = ParserUtils.splitScript(
                "[print [getg x]]test[foo (bar baz)][a [b] [c] [d [e]]][print \"te st\"]");
        assertThat(sut2.size(), is(4));
        assertThat(sut2.get(0), is("[print [getg x]]"));
        assertThat(sut2.get(1), is("test[foo (bar baz)]"));
        assertThat(sut2.get(2), is("[a [b] [c] [d [e]]]"));
        assertThat(sut2.get(3), is("[print \"te st\"]"));
        
        List<String> sut3 = ParserUtils.splitScript(
                "[use \"functional\"]"
                + "[println [map ("
                + "[fn (x) [cond "
                + "[eq [rem x 15] 0] fizzbuzz"
                + "[eq [rem x 5]  0] buzz"
                + "[eq [rem x 3]  0] fizz"
                + "true x"
                + "]]"
                + "[range 1 101]"
                + ")"
                + "]"
                + "]");
        assertThat(sut3.size(), is(2));
    }
    

}
