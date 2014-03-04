package info.rosetto.functions.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.logging.Level;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.base.elements.values.RosettoFunction;
import info.rosetto.models.system.Scope;
import info.rosetto.system.RosettoLogger;
import info.rosetto.utils.base.Values;

import org.junit.Before;
import org.junit.Test;

public class BaseFunctionsTest {
    
    private Scope testScope;
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
        testScope = new Scope();
    }
    
    @Test
    public void getInstanceTest() throws Exception {
         assertThat(BaseFunctions.getInstance(), is(notNullValue()));
    }
    
    @Test
    public void passTest() throws Exception {
        assertThat(BaseFunctions.pass.execute(testScope), is((RosettoValue)Values.VOID));
    }
    
    
    @Test
    public void setTest() throws Exception {
        assertThat(BaseFunctions.def.execute("foo bar", testScope), 
                is((RosettoValue)Values.VOID));
        assertThat(Contexts.get("foo").asString(), is("bar"));
        
        assertThat(BaseFunctions.def.execute("bar.baz 100", testScope), 
                is((RosettoValue)Values.VOID));
        assertThat(Contexts.get("bar.baz").asInt(), is(100));
        
        assertThat(BaseFunctions.def.execute("fuga.hoge 1.234", testScope), 
                is((RosettoValue)Values.VOID));
        assertThat(Contexts.get("fuga.hoge").asDouble(), is(1.234));
        
    }
    
    @Test
    public void fnTest() {
        RosettoValue sut1 = BaseFunctions.fn.execute("(x) [* @x 2]", testScope);
        assertThat(sut1.getType(), is(ValueType.FUNCTION));
        assertThat(((RosettoFunction)sut1).execute("2", testScope).asInt(), is(4));
        
        RosettoValue sut2 = BaseFunctions.fn.execute("(x) [* @x 10] [* @x 100]", testScope);
        assertThat(sut2.getType(), is(ValueType.FUNCTION));
        assertThat(((RosettoFunction)sut2).execute("5", testScope).asInt(), is(500));
    }
    
    @Test
    public void defnTest() {
        RosettoValue sut1 = BaseFunctions.defn.execute("foo (x) [* @x 2]", testScope);
        assertThat(sut1.getType(), is(ValueType.FUNCTION));
        assertThat(Contexts.getAction("foo").execute("2", testScope).asInt(), is(4));
    }
    
    @Test
    public void doTest() throws Exception {
        RosettoValue sut1 = BaseFunctions.doActions.execute("[* 2 2]", testScope);
        assertThat(sut1.getType(), is(ValueType.INTEGER));
        assertThat(sut1.asInt(), is(4));
        RosettoLogger.setLevel(Level.ALL);
        RosettoValue sut2 = BaseFunctions.doActions.execute("1 2 3 4 5", testScope);
        assertThat(sut2.getType(), is(ValueType.INTEGER));
        assertThat(sut2.asInt(), is(5));
    }

}
