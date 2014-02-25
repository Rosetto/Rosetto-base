package info.rosetto.models.base.function;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.values.OptionableList;
import info.rosetto.models.system.Scope;

import org.junit.Test;

@SuppressWarnings("serial")
public class FunctionPackageTest {
    
    @Test
    public void constructorTest() throws Exception {
        FunctionPackage sut1 = new FunctionPackage();
        assertThat(sut1.getFunctionCount(), is(0));
        assertThat(sut1.getFunctionNames().size(), is(0));
        
        FunctionPackage sut2 = new FunctionPackage(
                new RosettoFunction("foo") {
                    @Override
                    protected RosettoValue run(Scope functionScope, OptionableList args) {
                        return null;
                    }
                });
        assertThat(sut2.getFunctionCount(), is(1));
        assertThat(sut2.getFunctionNames().get(0), is("foo"));
        
    }

}
