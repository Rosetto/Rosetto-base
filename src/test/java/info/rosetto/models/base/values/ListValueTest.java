package info.rosetto.models.base.values;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.utils.base.Values;

import org.junit.Test;

public class ListValueTest {

    @Test
    public void firstTest() {
        ListValue list = new ListValue(Values.create(1), Values.create(2), Values.create(3));
        assertThat(list.first().asInt(), is(1));
    }
    
    @Test
    public void restTest() throws Exception {
        ListValue list = new ListValue(Values.create(1), Values.create(2), Values.create(3), 
                Values.create(4), Values.create(5));
        assertThat(list.getSize(), is(5));
        
        list = (ListValue)list.rest();
        assertThat(list.getSize(), is(4));
        
        list = (ListValue)list.rest();
        list = (ListValue)list.rest();
        assertThat(list.getSize(), is(2));
        
        assertThat((IntValue)list.rest(), is(Values.create(5)));
        
    }

}
