package info.rosetto.parsers.rosetto;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.base.elements.values.ActionCall;
import info.rosetto.models.base.elements.values.ListValue;
import info.rosetto.models.base.elements.values.OptionableList;
import info.rosetto.utils.base.Values;

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
    public void parseElementでタグをActionCallに変換できる() throws Exception {
        ActionCall s1 = (ActionCall)parser.parseElement("[hoge]");
        assertThat(s1.getFunctionName(), is("hoge"));
        assertThat(s1.getArgs().size(), is(0));
        ActionCall s2 = (ActionCall)parser.parseElement("[print text=\"test\"]");
        assertThat(s2.getFunctionName(), is("print"));
        assertThat(s2.getArgs().optionSize(), is(1));
        assertThat(s2.getArgs().get("text").asString(), is("test"));
        ActionCall s3 = (ActionCall)parser.parseElement("[wait 1000]");
        assertThat(s3.getFunctionName(), is("wait"));
        assertThat(s3.getArgs().size(), is(1));
        assertThat(s3.getArgs().getAt(0).asString(), is("1000"));
        //nullだとActionはEMPTYになる
        RosettoValue s4 = parser.parseElement(null);
        assertThat(s4, is((RosettoValue)Values.NULL));
    }
    
    @Test
    public void リストリテラルのパース() throws Exception {
        RosettoValue sut1 = parser.parseElement("(foo bar baz)");
        assertThat(sut1.getType(), is(ValueType.LIST));
        assertThat(((ListValue)sut1).first().asString(), is("foo"));
        
        RosettoValue sut2 = parser.parseElement("([eq? 1 2] foo)");
        assertThat(sut2.getType(), is(ValueType.LIST));
        assertThat(((ListValue)sut2).first().asString(), is("[eq? 1 2]"));
    }
    
    @Test
    public void ハッシュドリストリテラルのパース() throws Exception {
        RosettoValue sut1 = parser.parseElement("(foo=bar baz)");
        assertThat(sut1.getType(), is(ValueType.COLLECTION));
        assertThat(((OptionableList)sut1).get("foo").asString(), is("bar"));
        assertThat(((OptionableList)sut1).first().asString(), is("baz"));
        
        RosettoValue sut2 = parser.parseElement("(foo=bar hoge baz=100 fuga)");
        assertThat(sut2.getType(), is(ValueType.COLLECTION));
        assertThat(((OptionableList)sut2).get("foo").asString(), is("bar"));
        assertThat(((OptionableList)sut2).getAt(0).asString(), is("hoge"));
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
