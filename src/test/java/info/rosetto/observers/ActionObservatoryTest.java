package info.rosetto.observers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.models.base.blocks.RosettoMacro;
import info.rosetto.models.base.function.ExpandedArguments;
import info.rosetto.models.base.function.RosettoArguments;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.observers.ActionObservatory;
import info.rosetto.observers.ActionObserver;

import org.junit.Test;

public class ActionObservatoryTest {

    @Test
    public void testName() throws Exception {
        final StringBuilder sb = new StringBuilder();
        ActionObservatory.getInstance().addObserver(new ActionObserver() {
            @Override
            public void macroExecuted(RosettoMacro macro) {}
            
            @Override
            public void functionExecuted(RosettoFunction func, RosettoArguments args,
                    RosettoValue evaluatedValue) {
                sb.append(".");
            }
        });
        
        RosettoFunction sut = new RosettoFunction("func") {
            @Override
            protected RosettoValue run(ExpandedArguments args) {
                return null;
            }
        };
        assertThat(sb.length(), is(0));
        sut.execute();
        assertThat(sb.length(), is(1));
    }

}
