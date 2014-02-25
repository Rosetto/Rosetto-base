package info.rosetto.utils.base;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.base.elements.values.BoolValue;
import info.rosetto.models.base.elements.values.DoubleValue;
import info.rosetto.models.base.elements.values.IntValue;
import info.rosetto.system.exceptions.NotConvertibleException;

import org.junit.Before;
import org.junit.Test;

public class ValuesTest {
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
    }

    @Test
    public void createIntValueTest() throws Exception {
        assertThat(Values.create(100), is(new IntValue(100)));
        assertThat(Values.create(100).getType(), is(ValueType.INTEGER));
        assertThat(Values.create(100).asInt(), is(100));
        assertThat(Values.create(100).asLong(), is(100L));
        assertThat(Values.create(100).asDouble(), is(100.0));
        assertThat(Values.create(100).asString(), is("100"));
        try {
            Values.create(100).asBool();
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(NotConvertibleException.class));
        }
        
        assertThat(Values.create(-200).asInt(), is(-200));
        assertThat(Values.create(0).asInt(), is(0));
        assertThat(Values.create(Integer.MAX_VALUE).asInt(), is(Integer.MAX_VALUE));
        assertThat(Values.create(Integer.MIN_VALUE).asInt(), is(Integer.MIN_VALUE));
        assertThat(Values.create(Long.MAX_VALUE).asLong(), is(Long.MAX_VALUE));
        assertThat(Values.create(Long.MIN_VALUE).asLong(), is(Long.MIN_VALUE));
        
        try {
            Values.create(Long.MAX_VALUE).asInt();
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(NotConvertibleException.class));
        }
        try {
            Values.create(Long.MIN_VALUE).asInt();
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(NotConvertibleException.class));
        }
    }
    
    @Test
    public void createDoubleValueTest() throws Exception {
        assertThat(Values.create(100.0), is(new DoubleValue(100.0)));
        assertThat(Values.create(100.0).getType(), is(ValueType.DOUBLE));
        assertThat(Values.create(100.0).asInt(), is(100));
        assertThat(Values.create(100.0).asLong(), is(100L));
        assertThat(Values.create(100.0).asDouble(), is(100.0));
        assertThat(Values.create(100.0).asString(), is("100.0"));
        try {
            Values.create(100.0).asBool();
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(NotConvertibleException.class));
        }
        
        assertThat(Values.create(-50.0).asDouble(), is(-50.0));
        assertThat(Values.create(0.0).asDouble(), is(0.0));
        assertThat(Values.create(Double.MAX_VALUE).asDouble(), is(Double.MAX_VALUE));
        assertThat(Values.create(Double.MIN_VALUE).asDouble(), is(Double.MIN_VALUE));
    }
    
    @Test
    public void createBoolValueTest() throws Exception {
        assertThat(Values.create(true), is(BoolValue.TRUE));
        assertThat(Values.create(false), is(BoolValue.FALSE));
        assertThat(Values.create(true).getType(), is(ValueType.BOOLEAN));
        assertThat(Values.create(true).asString(), is("true"));
        
        
        try {
            Values.create(true).asInt();
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(NotConvertibleException.class));
        }
        
        try {
            Values.create(true).asLong();
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(NotConvertibleException.class));
        }
        
        try {
            Values.create(true).asDouble();
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(NotConvertibleException.class));
        }
        
    }
    
    @Test
    public void createFromJSONTest() throws Exception {
        RosettoValue sut1 = Values.createFromJSON("100");
        assertThat(sut1.asInt(), is(100));
        
        RosettoValue sut2 = Values.createFromJSON("[1,2,3,4,5]");
        assertThat(sut2.asString(), is("(1 2 3 4 5)"));
    }
    
    @Test
    public void createQuotedValueTest() throws Exception {
        assertThat(Values.create("\"foo bar\"").asString(), is("foo bar"));
    }
    

}
