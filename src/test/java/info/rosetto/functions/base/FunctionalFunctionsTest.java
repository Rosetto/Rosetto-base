package info.rosetto.functions.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.values.RosettoFunction;
import info.rosetto.models.system.Scope;
import info.rosetto.utils.base.Values;

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
        Contexts.importPackage(MathFunctions.getInstance(), "math");
        Contexts.usePackage("math");
        testScope = new Scope();
    }
    
    @Test
    public void mapTest() throws Exception {
        RosettoFunction map = FunctionalFunctions.map;
        
        RosettoValue sut1 = map.execute("[fn (x) [+ @x 1]] (1 2 3 4)", testScope);
        assertThat(sut1.asString(), is("(2 3 4 5)"));
        
        RosettoValue sut2 = map.execute("sqrt (16 4 1)", testScope);
        assertThat(sut2.asString(), is("(4.0 2.0 1.0)"));
        
        RosettoValue sut3 = map.execute("sqrt 4", testScope);
        assertThat(sut3.asInt(), is(2));
    }
    
    @Test
    public void rangeTest() throws Exception {
        RosettoFunction range = FunctionalFunctions.range;
        RosettoValue sut1 = range.execute("1 5", testScope);
        assertThat(sut1.asString(), is("(1 2 3 4)"));
        RosettoValue sut2 = range.execute("-2 2", testScope);
        assertThat(sut2.asString(), is("(-2 -1 0 1)"));
    }
    
    @Test
    public void condTest() throws Exception {
        RosettoFunction cond = FunctionalFunctions.cond;
        
        RosettoValue sut1 = cond.execute("(true 100)", testScope);
        assertThat(sut1.asInt(), is(100));
        
        RosettoValue sut2 = cond.execute("(false 1) (false 5) (true -50) (false 5)", testScope);
        assertThat(sut2.asInt(), is(-50));
        
        RosettoValue sut3 = cond.execute("([> 1 5] 1) ([< 100 -100] 5) ([eq? 2 2] 999)", testScope);
        assertThat(sut3.asInt(), is(999));
        
        RosettoValue sut4 = cond.execute("[> 10 5] 1", testScope);
        assertThat(sut4.asInt(), is(1));
        
        RosettoValue sut5 = cond.execute("(false 1) (false 2) (false 3)", testScope);
        assertThat(sut5, is((RosettoValue)Values.NULL));
        
        RosettoValue sut6 = cond.execute("false 1", testScope);
        assertThat(sut6, is((RosettoValue)Values.NULL));
    }



}
