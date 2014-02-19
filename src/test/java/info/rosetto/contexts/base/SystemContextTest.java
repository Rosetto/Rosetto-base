package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.parsers.rosetto.RosettoParser;

import org.junit.Test;

public class SystemContextTest {
    

    
    @Test
    public void getAndSetParserTest() throws Exception {
        SystemContext sut = new SystemContext();
        
        //初期状態ではRosettoParser
        assertThat(sut.getParser(), instanceOf(RosettoParser.class));
    }



}
