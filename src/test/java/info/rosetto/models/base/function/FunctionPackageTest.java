package info.rosetto.models.base.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.models.base.values.RosettoValue;

import org.junit.Test;

@SuppressWarnings("serial")
public class FunctionPackageTest {
    
    @Test
    public void constructorTest() throws Exception {
        FunctionPackage sut1 = new FunctionPackage("org.example");
        assertThat(sut1.getFunctionCount(), is(0));
        assertThat(sut1.getFunctionNames().size(), is(0));
        assertThat(sut1.getPackageName(), is("org.example"));
        
        FunctionPackage sut2 = new FunctionPackage("foo.bar", 
                new RosettoFunction("baz") {
                    @Override
                    protected RosettoValue run(ExpandedArguments args) {
                        return null;
                    }
                });
        assertThat(sut2.getFunctionCount(), is(1));
        assertThat(sut2.getFunctionNames().get(0), is("baz"));
        assertThat(sut2.getPackageName(), is("foo.bar"));
        
        try {
            new FunctionPackage(null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        try {
            new FunctionPackage("");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

}
