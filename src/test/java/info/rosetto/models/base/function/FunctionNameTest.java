package info.rosetto.models.base.function;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import info.rosetto.models.base.function.FunctionName;

import org.junit.Test;

public class FunctionNameTest {

    @Test
    public void ConstructorTest() {
        FunctionName sut = new FunctionName("org.test", "testfunc");
        assertThat(sut.getPackage(), is("org.test"));
        assertThat(sut.getShortName(), is("testfunc"));
        assertThat(sut.getFullName(), is("org.test.testfunc"));
        assertThat(sut.toString(), is("org.test.testfunc"));
    }
    
    @Test
    public void ConstructorExceptionTest() throws Exception {
        try {
            new FunctionName("", "testfunc");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        try {
            new FunctionName(null, "testfunc");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        try {
            new FunctionName("org.test", "");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        try {
            new FunctionName("org.test", null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void equalsTest() throws Exception {
        assertThat(new FunctionName("org.test", "testfunc")
        .equals(new FunctionName("org.test", "testfunc"))
                , is(true));
        assertThat(new FunctionName("org.foo", "testfunc")
        .equals(new FunctionName("org.test", "testfunc"))
                , is(false));
        assertThat(new FunctionName("org.test", "bar")
        .equals(new FunctionName("org.test", "testfunc"))
                , is(false));
    }

    @Test
    public void equalsNameTest() throws Exception {
         FunctionName sut = new FunctionName("org.test", "testfunc");
         assertThat(sut.equalsName("testfunc"), is(true));
         assertThat(sut.equalsName("org.test.testfunc"), is(true));
         assertThat(sut.equalsName("some.other.org.testfunc"), is(false));
         assertThat(sut.equalsName(""), is(false));
         //nullだとエラー
         try {
             sut.equalsName(null);
             fail();
         } catch(Exception e) {
             assertThat(e, instanceOf(IllegalArgumentException.class));
         }
         
    }
}
