package info.rosetto.functions.base;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.models.base.values.ValueType;
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



}
