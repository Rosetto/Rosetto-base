package info.rosetto.functions.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.function.ExpandedArguments;
import info.rosetto.models.base.function.FunctionPackage;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.utils.base.Values;

import org.junit.Before;
import org.junit.Test;

public class BaseFunctionsTest {
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
    }
    
    @Test
    public void getInstanceTest() throws Exception {
         assertThat(BaseFunctions.getInstance(), is(notNullValue()));
    }
    
    @Test
    public void passTest() throws Exception {
        assertThat(BaseFunctions.pass.execute(), is((RosettoValue)Values.VOID));
    }
    
    
    @Test
    public void setTest() throws Exception {
        assertThat(BaseFunctions.def.execute("foo bar"), is((RosettoValue)Values.VOID));
        assertThat(Contexts.get("foo").asString(), is("bar"));
        
        assertThat(BaseFunctions.def.execute("bar.baz 100"), is((RosettoValue)Values.VOID));
        assertThat(Contexts.get("bar.baz").asInt(), is(100));
        
        assertThat(BaseFunctions.def.execute("fuga.hoge 1.234"), is((RosettoValue)Values.VOID));
        assertThat(Contexts.get("fuga.hoge").asDouble(), is(1.234));
        
    }

}
