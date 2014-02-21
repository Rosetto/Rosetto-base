package info.rosetto.functions.base;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.state.variables.Scope;

import org.junit.Before;
import org.junit.Test;

public class FunctionalFunctionsTest {
    
    private Scope testScope;
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
        Contexts.importPackage(FunctionalFunctions.getInstance(), "functional");
        Contexts.usePackage("functional");
        Contexts.importPackage(ArithmeticFunctions.getInstance(), "arithmetic");
        Contexts.usePackage("arithmetic");
        testScope = new Scope();
    }
    
    @Test
    public void condTest() throws Exception {
        RosettoFunction cond = FunctionalFunctions.cond;
        
        RosettoValue sut1 = cond.execute("(true 100)", testScope);
        assertThat(sut1.asInt(), is(100));
        
        RosettoValue sut2 = cond.execute("(false 1) (false 5) (true -50)", testScope);
        assertThat(sut2.asInt(), is(-50));
        
        RosettoValue sut3 = cond.execute("([> 1 5] 1) ([< 100 -100] 5) ([eq? 2 2] 999)", testScope);
        assertThat(sut3.asInt(), is(999));
        
    }



}
