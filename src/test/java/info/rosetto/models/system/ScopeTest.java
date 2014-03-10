package info.rosetto.models.system;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.values.ListValue;
import info.rosetto.models.base.elements.values.RosettoFunction;
import info.rosetto.utils.base.Values;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("serial")
public class ScopeTest {
    
    
    private static RosettoFunction func1 = new RosettoFunction("func1",
            "x", "y") {
        
        @Override
        protected RosettoValue run(Scope scope, ListValue args) {
            return null;
        }
    };
    
    private Scope testScope;
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
        testScope = new Scope();
    }
    
    @Test
    public void constructorTest() throws Exception {
         Scope sut1  = new Scope();
         assertThat(sut1.toString(), is("{}"));
         assertThat(sut1.getParent(), is(nullValue()));
         assertThat(sut1.containsKey("foo"), is(false));
         assertThat(sut1.get("foo"), is((RosettoValue)Values.NULL));
         sut1.set("foo", Values.create(100));
         assertThat(sut1.toString(), is("{foo=100}"));
         assertThat(sut1.containsKey("foo"), is(true));
         assertThat(sut1.get("foo").asInt(), is(100));
         
         Scope sut2 = new Scope(sut1);
         assertThat(sut2.getParent(), is(sut1));
         assertThat(sut2.containsKey("foo"), is(true));
         assertThat(sut2.get("foo").asInt(), is(100));
         assertThat(sut2.get("bar"), is((RosettoValue)Values.NULL));
         sut2.set("bar", Values.create(10));
         
         assertThat(sut1.get("bar"), is((RosettoValue)Values.NULL));
         assertThat(sut2.get("bar").asInt(), is(10));
         
         Map<String, RosettoValue> map = new HashMap<String, RosettoValue>();
         map.put("baz", Values.create(12345));
         Scope sut3 = new Scope(sut2, map);
         assertThat(sut3.getParent(), is(sut2));
         assertThat(sut3.get("foo").asInt(), is(100));
         assertThat(sut3.get("bar").asInt(), is(10));
         assertThat(sut3.get("baz").asInt(), is(12345));
         assertThat(sut3.getMap(), is(map));
    }

    @Test
    public void functionConstructorTest() throws Exception {
         Scope sut1 = new Scope(ListValue.createFromString("100 [+ 1 3]"), func1, testScope);
         assertThat(sut1.get("x").asString(), is("100"));
         assertThat(sut1.get("y").asString(), is("4"));
         Scope sut2 = new Scope(ListValue.createFromString("[+ 1 [+ 2 4]] [+ 1 [+ 1 [+ 3 5]]]"), 
                 func1, testScope);
         assertThat(sut2.get("x").asString(), is("7"));
         assertThat(sut2.get("y").asString(), is("10"));
    }

}
