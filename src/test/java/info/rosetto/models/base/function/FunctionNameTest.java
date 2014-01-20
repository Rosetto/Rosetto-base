package info.rosetto.models.base.function;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import info.rosetto.models.base.function.FunctionName;

import org.junit.Test;

/**
 * OK(2013/05/16)
 * @author tohhy
 */
public class FunctionNameTest {

    @Test
    public void インスタンス化テスト() {
        FunctionName sut = new FunctionName("org.test", "testfunc");
        assertThat(sut.getPackage(), is("org.test"));
        assertThat(sut.getShortName(), is("testfunc"));
        assertThat(sut.getFullName(), is("org.test.testfunc"));
        assertThat(sut.toString(), is("org.test.testfunc"));
    }
    
    @Test
    public void パッケージ名がない場合と関数名がない場合にエラー() throws Exception {
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
    public void equalsが正常に動作する() throws Exception {
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
    public void equalsNameが正常に動作する() throws Exception {
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
