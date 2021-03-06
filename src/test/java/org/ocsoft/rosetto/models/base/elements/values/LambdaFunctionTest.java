package org.ocsoft.rosetto.models.base.elements.values;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.ocsoft.rosetto.contexts.base.Rosetto;
import org.ocsoft.rosetto.models.base.elements.RosettoValue;
import org.ocsoft.rosetto.models.base.elements.values.LambdaFunction;
import org.ocsoft.rosetto.models.base.elements.values.ListValue;
import org.ocsoft.rosetto.models.system.Scope;

public class LambdaFunctionTest {
    
    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
    }

    @SuppressWarnings("serial")
    @Test
    public void constructorTest() throws Exception {
        LambdaFunction sut1 = new LambdaFunction() {
            
            @Override
            protected RosettoValue run(Scope scope, ListValue rawArgs) {
                return null;
            }
        }; 
        assertThat(sut1.toString(), is("[fn ()]"));
        
        LambdaFunction sut2 = new LambdaFunction("foo", "bar=baz") {
            
            @Override
            protected RosettoValue run(Scope scope, ListValue rawArgs) {
                return null;
            }
        };
        assertThat(sut2.toString(), is("[fn (foo bar=baz)]"));
        
        LambdaFunction sut3 = new LambdaFunction(new ListValue("hoge", "fuga=piyo")) {
            
            @Override
            protected RosettoValue run(Scope scope, ListValue rawArgs) {
                return null;
            }
        };
        assertThat(sut3.toString(), is("[fn (hoge fuga=piyo)]"));
    }

}
