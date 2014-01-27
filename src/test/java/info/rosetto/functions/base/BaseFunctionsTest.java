package info.rosetto.functions.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.contexts.base.Contexts;
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
         assertThat(BaseFunctions.getInstance().getPackageName(), is("rosetto.base"));
    }
    
    @Test
    public void passTest() throws Exception {
        assertThat(BaseFunctions.pass.execute(), is((RosettoValue)Values.VOID));
    }
    
    @Test
    public void referTest() throws Exception {
        assertThat(BaseFunctions.refer.execute("package=foo"), is((RosettoValue)Values.VOID));
    }
    
    @Test
    public void includeTest() throws Exception {
        assertThat(BaseFunctions.include.execute("package=foo"), is((RosettoValue)Values.VOID));
    }

}
