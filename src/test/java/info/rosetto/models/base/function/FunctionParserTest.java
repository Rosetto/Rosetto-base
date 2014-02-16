package info.rosetto.models.base.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.utils.base.FunctionParser;

import org.junit.Test;

public class FunctionParserTest {

    
    @Test
    public void 通常引数を含む関数をパースできる() throws Exception {
        String sut1 = "foo";
        assertThat(FunctionParser.parse(sut1).getFunctionName(), is("foo"));
        
        String sut2 = "foo bar";
        assertThat(FunctionParser.parse(sut2).getFunctionName(), is("foo"));
        assertThat(FunctionParser.parse(sut2).getArgs().get(0), is("bar"));
        
        String sut3 = "foo bar=baz";
        assertThat(FunctionParser.parse(sut3).getFunctionName(), is("foo"));
        assertThat(FunctionParser.parse(sut3).getArgs().get("bar"), is("baz"));
    }
    

}
