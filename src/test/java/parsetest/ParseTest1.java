package parsetest;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.rosetto.contexts.base.Rosetto;
import org.rosetto.models.base.scenario.Scenario;
import org.rosetto.models.system.Parser;

public class ParseTest1 {
    private Parser parser;
    
    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
        parser = Rosetto.getParser();
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
