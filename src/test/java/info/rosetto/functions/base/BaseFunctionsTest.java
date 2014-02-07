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
            private static final long serialVersionUID = 1005332135613281500L;

            @Override
            protected RosettoValue run(ExpandedArguments args) {
                return null;
            }
        };
        FunctionPackage pak = new FunctionPackage(f);
        assertThat(Contexts.get("foo.test"), is((RosettoValue)Values.NULL));
        
        pak.addTo(Contexts.getNameSpace("foo"));
        assertThat(Contexts.get("!foo.test"), is((RosettoValue)f));
        assertThat(Contexts.get("bar.test"), is((RosettoValue)Values.NULL));
        
        assertThat(BaseFunctions.refer.execute("ns=foo as=bar"), is((RosettoValue)Values.VOID));
        assertThat(Contexts.get("!foo.test"), is((RosettoValue)f));
        assertThat(Contexts.get("!bar.test"), is((RosettoValue)f));
    }
    
    @Test
    public void includeTest() throws Exception {
        RosettoFunction f = new RosettoFunction("test") {
            private static final long serialVersionUID = 1005332135613281500L;

            @Override
            protected RosettoValue run(ExpandedArguments args) {
                return null;
            }
        };
        FunctionPackage pak = new FunctionPackage(f);
        pak.addTo(Contexts.getNameSpace("foo"));
        
        assertThat(Contexts.get("test"), is((RosettoValue)Values.NULL));
        
        assertThat(BaseFunctions.include.execute("ns=foo"), is((RosettoValue)Values.VOID));
        
        assertThat(Contexts.get("test"), is((RosettoValue)f));
    }
    
    
    @Test
    public void setTest() throws Exception {
        assertThat(BaseFunctions.set.execute("foo bar"), is((RosettoValue)Values.VOID));
        assertThat(Contexts.get("foo").asString(), is("bar"));
        assertThat(Contexts.get("!story.foo").asString(), is("bar"));
        
        assertThat(BaseFunctions.set.execute("bar.baz 100"), is((RosettoValue)Values.VOID));
        assertThat(Contexts.get("bar.baz").asInt(), is(100));
        assertThat(Contexts.get("!story.bar.baz").asInt(), is(100));
        
        assertThat(BaseFunctions.set.execute("hoge 1.234 ns=fuga"), is((RosettoValue)Values.VOID));
        assertThat(Contexts.get("!fuga.hoge").asDouble(), is(1.234));
        assertThat(Contexts.get("!story.fuga.hoge"), is((RosettoValue)Values.NULL));
        
    }

}
