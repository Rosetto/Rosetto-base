package info.rosetto.parsers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.values.HashedList;
import info.rosetto.models.base.values.ListValue;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.models.base.values.ValueType;
import info.rosetto.models.state.variables.Scope;
import info.rosetto.parsers.ParseUtils;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ParserUtilsTest {

    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
    }
    
    @Test
    public void 単純なスクリプトのパース() throws Exception {
         List<String> sut1 = ParseUtils.splitScript("");
         assertThat(sut1.size(), is(0));
         
         List<String> sut2 = ParseUtils.splitScript("hoge");
         assertThat(sut2.size(), is(1));
         assertThat(sut2.get(0), is("hoge"));
         
         List<String> sut3 = ParseUtils.splitScript("hoge[br]fuga[p]");
         assertThat(sut3.size(), is(2));
         assertThat(sut3.get(0), is("hoge[br]"));
         assertThat(sut3.get(1), is("fuga[p]"));
    }
    
    @Test
    public void ネストした関数呼び出しのパース() throws Exception {
        List<String> sut1 = ParseUtils.splitScript("[print [getg x]]");
        assertThat(sut1.size(), is(1));
        assertThat(sut1.get(0), is("[print [getg x]]"));
        
        List<String> sut2 = ParseUtils.splitScript(
                "[print [getg x]]test[foo (bar baz)][a [b] [c] [d [e]]][print \"te st\"]");
        assertThat(sut2.size(), is(4));
        assertThat(sut2.get(0), is("[print [getg x]]"));
        assertThat(sut2.get(1), is("test[foo (bar baz)]"));
        assertThat(sut2.get(2), is("[a [b] [c] [d [e]]]"));
        assertThat(sut2.get(3), is("[print \"te st\"]"));
        
        List<String> sut3 = ParseUtils.splitScript(
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
