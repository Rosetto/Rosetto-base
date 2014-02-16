package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.models.base.function.ExpandedArguments;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.function.RosettoFunctionTest;
import info.rosetto.models.base.values.RosettoAction;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.utils.base.Values;

import org.junit.Test;

@SuppressWarnings("serial")
public class ActionContextTest {
    

    @Test
    public void getAndDefineTest() throws Exception {
        ActionContext sut = new ActionContext();
        assertThat(sut.get("foo"), is((RosettoAction)Values.NULL));
        assertThat(sut.get("foo.bar"), is((RosettoAction)Values.NULL));
        assertThat(sut.containsNameSpace("foo"), is(false));
        
        RosettoFunction f1 = new RosettoFunction("foo") {
            @Override
            protected RosettoValue run(ExpandedArguments args) {
                return null;
            }
        };
        sut.defineAction(f1);
        assertThat(sut.get("foo"), is((RosettoAction)f1));
        assertThat(sut.containsNameSpace("foo"), is(false));
        
        RosettoFunction f2 = new RosettoFunction("bar") {
            @Override
            protected RosettoValue run(ExpandedArguments args) {
                return null;
            }
        };
        sut.defineAction(f2, "foo");
        assertThat(sut.get("foo.bar"), is((RosettoAction)f2));
        assertThat(sut.containsNameSpace("foo"), is(true));
    }

}
