package info.rosetto.models.base.values;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.values.BoolValue;
import info.rosetto.utils.base.Values;

import org.junit.Before;
import org.junit.Test;

public class BoolValueTest {
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
    }

    @Test
    public void convertTest() {
        convertAndTestEachOther(BoolValue.TRUE);
        convertAndTestEachOther(BoolValue.FALSE);
    }
    
    
    private void convertAndTestEachOther(BoolValue v1) {
        String s1 = v1.asString();
        BoolValue v2 = (BoolValue)Values.create(s1);
        String s2 = v2.asString();
        assertThat(s1, is(s2));
        assertThat(v1, is(v2));
    }

}
