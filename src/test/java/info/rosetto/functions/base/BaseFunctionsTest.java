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
    public void referTest() throws Exception {
        RosettoFunction f = new RosettoFunction("test") {
            @Override
            protected RosettoValue run(ExpandedArguments args) {
                return null;
            }
        };
        FunctionPackage pak = new FunctionPackage(f);
        assertThat(Contexts.get("foo.test"), is(nullValue()));
        
        pak.addTo(Contexts.getNameSpace("foo"));
        assertThat(Contexts.get("foo.test"), is((RosettoValue)f));
        assertThat(Contexts.get("bar.test"), is(nullValue()));
        
        assertThat(BaseFunctions.refer.execute("package=foo as=bar"), is((RosettoValue)Values.VOID));
        assertThat(Contexts.get("foo.test"), is((RosettoValue)f));
        assertThat(Contexts.get("bar.test"), is((RosettoValue)f));
    }
    
    @Test
    public void includeTest() throws Exception {
        assertThat(BaseFunctions.include.execute("package=foo"), is((RosettoValue)Values.VOID));
    }

}
