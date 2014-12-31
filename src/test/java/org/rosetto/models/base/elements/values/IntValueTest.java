package org.rosetto.models.base.elements.values;

import org.junit.Before;
import org.junit.Test;
import org.rosetto.contexts.base.Rosetto;
import org.rosetto.models.base.elements.RosettoValue;
import org.rosetto.models.base.elements.values.IntValue;

public class IntValueTest {

    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
    }

    @Test
    public void convertTest() {
        RosettoValue sut = new IntValue(100);
        
        //リストとしては単一要素扱い
        ValueTestUtils.isSingleValue(sut);
    }

}
