package info.rosetto.parsers.rosetto;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.base.elements.values.MixedStoreValue;
import info.rosetto.models.base.elements.values.ListValue;
import info.rosetto.models.state.variables.Scope;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class RosettoElementParserTest {
    
    private RosettoElementParser parser;

    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
        parser = new RosettoElementParser();
    }
    
    @Test
    public void リストリテラルのパース() throws Exception {
        RosettoValue sut1 = parser.parseArg("(foo bar baz)", new Scope());
        assertThat(sut1.getType(), is(ValueType.LIST));
        assertThat(((ListValue)sut1).first().asString(), is("foo"));
        
        RosettoValue sut2 = parser.parseArg("([= 1 2] foo)", new Scope());
        assertThat(sut2.getType(), is(ValueType.LIST));
        assertThat(((ListValue)sut2).first().asString(), is("[= 1 2]"));
    }
    
    @Test
    public void ハッシュドリストリテラルのパース() throws Exception {
        RosettoValue sut1 = parser.parseArg("(foo=bar baz)", new Scope());
        assertThat(sut1.getType(), is(ValueType.HASHED_LIST));
        assertThat(((MixedStoreValue)sut1).get("foo").asString(), is("bar"));
        assertThat(((MixedStoreValue)sut1).first().asString(), is("baz"));
        
        RosettoValue sut2 = parser.parseArg("(foo=bar hoge baz=100 fuga)", new Scope());
        assertThat(sut2.getType(), is(ValueType.HASHED_LIST));
        assertThat(((MixedStoreValue)sut2).get("foo").asString(), is("bar"));
        assertThat(((MixedStoreValue)sut2).getAt(0).asString(), is("hoge"));
    }
    

    @Test
    public void 通常引数のsplit() throws Exception {
        List<String> sut1 = parser.splitElements("foo bar baz");
        assertThat(sut1.size(), is(3));
    }
    
    @Test
    public void ダブルクオートを含む引数のsplit() throws Exception {
        List<String> sut1 = parser.splitElements("\"foo\" bar baz");
        assertThat(sut1.size(), is(3));
        
        List<String> sut2 = parser.splitElements("\"foo\" \"bar baz\"");
        assertThat(sut2.size(), is(2));
        assertThat(sut2.get(1), is("\"bar baz\""));
    }
    
    @Test
    public void 角括弧を含む引数のsplit() throws Exception {
        List<String> sut1 = parser.splitElements("foo [bar] baz");
        assertThat(sut1.size(), is(3));
        
        List<String> sut2 = parser.splitElements("foo [bar baz]");
        assertThat(sut2.size(), is(2));
        assertThat(sut2.get(1), is("[bar baz]"));
    }

}
