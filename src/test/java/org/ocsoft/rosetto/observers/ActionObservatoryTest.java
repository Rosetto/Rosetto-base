package org.ocsoft.rosetto.observers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.ocsoft.rosetto.models.base.elements.RosettoValue;
import org.ocsoft.rosetto.models.base.elements.values.ListValue;
import org.ocsoft.rosetto.models.base.elements.values.RosettoFunction;
import org.ocsoft.rosetto.models.base.elements.values.ScriptValue;
import org.ocsoft.rosetto.models.system.Scope;
import org.ocsoft.rosetto.observers.ActionObserver;
import org.ocsoft.rosetto.observers.RosettoObservatories;

public class ActionObservatoryTest {

    @Test
    public void testName() throws Exception {
        final StringBuilder sb = new StringBuilder();
        RosettoObservatories.getAction().addObserver(new ActionObserver() {
            
            @Override
            public void functionExecuted(RosettoFunction func, ListValue args,
                    RosettoValue evaluatedValue) {
                sb.append(".");
            }

            @Override
            public void macroExecuted(ScriptValue macro, ListValue args,
                    RosettoValue evaluatedValue) {}
        });
        
        RosettoFunction sut = new RosettoFunction("func") {
            private static final long serialVersionUID = 1L;

            @Override
            protected RosettoValue run(Scope functionScope, ListValue args) {
                return null;
            }
        };
        assertThat(sb.length(), is(0));
        sut.execute(new Scope());
        assertThat(sb.length(), is(1));
    }

}
