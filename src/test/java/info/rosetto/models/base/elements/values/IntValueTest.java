package info.rosetto.models.base.elements.values;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoValue;

import org.junit.Before;
import org.junit.Test;

public class IntValueTest {

    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
    }

    @Test
    public void convertTest() {
        RosettoValue sut = new IntValue(100);
        
        //リストとしては単一要素扱い
        ValueTestUtils.isSingleValue(sut);
    }

}
