package info.rosetto.utils.base;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.models.base.values.ListValue;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.models.base.values.ValueType;

import org.junit.Test;

public class ArgumentsUtilsTest {

    @Test
    public void 通常引数のsplit() throws Exception {
        String[] sut1 = ArgumentsUtils.splitStringArgs("foo bar baz");
        assertThat(sut1.length, is(3));
    }
    
    @Test
    public void ダブルクオートを含む引数のsplit() throws Exception {
        String[] sut1 = ArgumentsUtils.splitStringArgs("\"foo\" bar baz");
        assertThat(sut1.length, is(3));
        
        String[] sut2 = ArgumentsUtils.splitStringArgs("\"foo\" \"bar baz\"");
        assertThat(sut2.length, is(2));
        assertThat(sut2[1], is("\"bar baz\""));
    }
    
    @Test
    public void 角括弧を含む引数のsplit() throws Exception {
        String[] sut1 = ArgumentsUtils.splitStringArgs("foo [bar] baz");
        assertThat(sut1.length, is(3));
        
        String[] sut2 = ArgumentsUtils.splitStringArgs("foo [bar baz]");
        assertThat(sut2.length, is(2));
        assertThat(sut2[1], is("[bar baz]"));
    }
    
    @Test
    public void リストリテラルのパース() throws Exception {
        RosettoValue sut1 = ArgumentsUtils.parseArg("(foo bar baz)");
        assertThat(sut1.getType(), is(ValueType.LIST));
        assertThat(((ListValue)sut1).first().asString(), is("foo"));
    }

}
