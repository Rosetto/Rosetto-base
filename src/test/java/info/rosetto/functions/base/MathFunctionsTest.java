package info.rosetto.functions.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.system.Scope;

import org.junit.Before;
import org.junit.Test;

public class MathFunctionsTest {
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
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
