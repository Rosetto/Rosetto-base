package org.ocsoft.rosetto.models.base.elements.values;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.ocsoft.rosetto.contexts.base.Rosetto;
import org.ocsoft.rosetto.models.base.elements.RosettoValue;
import org.ocsoft.rosetto.models.base.elements.values.StringValue;

public class StringValueTest {

    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
    }

    @Test
    public void convertTest() {
        RosettoValue sut = new StringValue("foo");
        
        //リストとしては単一要素扱い
        ValueTestUtils.isSingleValue(sut);
    }

}
