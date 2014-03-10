package parsetest;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.system.Parser;

import org.junit.Before;
import org.junit.Test;

public class ParseTest1 {
    private Parser parser;
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
        parser = Contexts.getParser();
    }

    @Test
    public void test1() throws Exception {
        Scenario s = parser.parseScript("Hello, World!");
        assertThat(s.getLength(), is(1));
    }
    
    @Test
    public void test2() throws Exception {
        Scenario s = parser.parseScript("Hello, World!\nHello World again!");
        assertThat(s.getLength(), is(2));
        assertThat(s.getUnitAt(0).getAction().asString(), is("[br]"));
    }
    
}
