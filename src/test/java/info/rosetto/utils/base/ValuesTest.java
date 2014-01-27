package info.rosetto.utils.base;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import info.rosetto.exceptions.NotConvertibleException;
import info.rosetto.models.base.values.IntValue;
import info.rosetto.models.base.values.ValueType;

import org.junit.Test;

public class ValuesTest {

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

}
