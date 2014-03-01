package info.rosetto.models.base.elements.values;

import static org.junit.Assert.*;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoValue;

import org.junit.Before;
import org.junit.Test;

public class StringValueTest {

    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
    }

    @Test
    public void convertTest() {
        RosettoValue sut = new StringValue("foo");
        
        //リストとしては単一要素扱い
        ValueTestUtils.isSingleValue(sut);
    }

}
