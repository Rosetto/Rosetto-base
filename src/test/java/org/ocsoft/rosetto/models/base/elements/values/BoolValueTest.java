package org.ocsoft.rosetto.models.base.elements.values;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.ocsoft.rosetto.contexts.base.Rosetto;
import org.ocsoft.rosetto.models.base.elements.values.BoolValue;
import org.ocsoft.rosetto.utils.base.Values;

public class BoolValueTest {
    
    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
    }

    @Test
    public void convertTest() {
        convertAndTestEachOther(BoolValue.TRUE);
        convertAndTestEachOther(BoolValue.FALSE);
        
        //リストとしては単一要素扱い
        ValueTestUtils.isSingleValue(BoolValue.TRUE);
        ValueTestUtils.isSingleValue(BoolValue.FALSE);
    }
    
    
    private void convertAndTestEachOther(BoolValue v1) {
        String s1 = v1.asString();
        BoolValue v2 = (BoolValue)Values.create(s1);
        String s2 = v2.asString();
        assertThat(s1, is(s2));
        assertThat(v1, is(v2));
    }

}
