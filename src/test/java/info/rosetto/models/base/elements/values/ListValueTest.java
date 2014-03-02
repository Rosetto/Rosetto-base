package info.rosetto.models.base.elements.values;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.values.IntValue;
import info.rosetto.models.base.elements.values.ListValue;
import info.rosetto.utils.base.Values;

import org.junit.Before;
import org.junit.Test;

public class ListValueTest {
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
    }

    @Test
    public void firstTest() {
        ListValue list = new ListValue(Values.create(1), Values.create(2), Values.create(3));
        assertThat(list.first().asInt(), is(1));
    }
    
    @Test
    public void restTest() throws Exception {
        ListValue list = new ListValue(Values.create(1), Values.create(2), Values.create(3), 
                Values.create(4), Values.create(5));
        assertThat(list.size(), is(5));
        
        list = (ListValue)list.rest();
        assertThat(list.size(), is(4));
        
        list = (ListValue)list.rest();
        list = (ListValue)list.rest();
        assertThat(list.size(), is(2));
        
        assertThat((IntValue)list.rest(), is(Values.create(5)));
        
    }
    
    @Test
    public void convertTest() throws Exception {
         convertAndTestEachOther("(foo bar baz)");
         convertAndTestEachOther("()");
         convertAndTestEachOther("(1.0 2 123456789012345)");
         convertAndTestEachOther("([print [map [fn (x) [+ x 1]] (1 2 3 4 5)]])");
    }
    
    private void convertAndTestEachOther(String s) {
        ListValue v1 = (ListValue)Values.create(s);
        String s1 = v1.asString();
        ListValue v2 = (ListValue)Values.create(s1);
        String s2 = v2.asString();
        assertThat(s1, is(s2));
        assertThat(v1, is(v2));
    }

}
