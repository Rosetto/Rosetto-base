package org.rosetto.functions.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.rosetto.contexts.base.Rosetto;
import org.rosetto.functions.base.MathFunctions;
import org.rosetto.models.base.elements.ValueType;
import org.rosetto.models.system.Scope;

public class MathFunctionsTest {
    
    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
    }
    
    private Scope testScope;
    
    @Test
    public void sqrtTest() throws Exception {
        assertThat(MathFunctions.sqrt.execute("4", testScope).asInt(), is(2));
        assertThat(MathFunctions.sqrt.execute("16 4 1", testScope).asString(), is("(4.0 2.0 1.0)"));
    }
    
    @Test
    public void randomTest() {
        assertThat(MathFunctions.random.execute(testScope).getType(), is(ValueType.DOUBLE));
    }

}
