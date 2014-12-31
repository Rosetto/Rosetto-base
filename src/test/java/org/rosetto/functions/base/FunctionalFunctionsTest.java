package org.rosetto.functions.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.rosetto.contexts.base.Rosetto;
import org.rosetto.functions.base.FunctionalFunctions;
import org.rosetto.functions.base.MathFunctions;
import org.rosetto.models.base.elements.RosettoValue;
import org.rosetto.models.base.elements.values.RosettoFunction;
import org.rosetto.models.system.Scope;
import org.rosetto.utils.base.Values;

public class FunctionalFunctionsTest {
    
    private Scope testScope;
    
    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
        Rosetto.importPackage(FunctionalFunctions.getInstance(), "functional");
        Rosetto.usePackage("functional");
        Rosetto.importPackage(MathFunctions.getInstance(), "math");
        Rosetto.usePackage("math");
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
