package info.rosetto.observers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.models.base.blocks.RosettoMacro;
import info.rosetto.models.base.elements.MixedStore;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.state.variables.Scope;

import org.junit.Test;

public class ActionObservatoryTest {

    @Test
    public void testName() throws Exception {
        final StringBuilder sb = new StringBuilder();
        Observatories.getAction().addObserver(new ActionObserver() {
            @Override
            public void macroExecuted(RosettoMacro macro) {}
            
            @Override
            public void functionExecuted(RosettoFunction func, MixedStore args,
                    RosettoValue evaluatedValue) {
                sb.append(".");
            }
        });
        
        RosettoFunction sut = new RosettoFunction("func") {
            private static final long serialVersionUID = 1L;

            @Override
            protected RosettoValue run(Scope functionScope, MixedStore args) {
                return null;
            }
        };
        assertThat(sb.length(), is(0));
        sut.execute(new Scope());
        assertThat(sb.length(), is(1));
    }

}
