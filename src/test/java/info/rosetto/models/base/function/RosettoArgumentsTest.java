package info.rosetto.models.base.function;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.parser.ArgumentSyntax;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.utils.base.Values;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;


public class RosettoArgumentsTest {
    private static final ArgumentSyntax SYNTAX = 
            new ArgumentSyntax("%", "*", '|', '-');
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
    }
    
    @Test
    public void インスタンス化の際にnullが渡るとエラー() throws Exception {
        try {
            new RosettoArguments((String)null);
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void インスタンス化の際に属性値を囲んでいるダブルクオートは外される() throws Exception {
        String test = " \"foo\" bar=baz hoge=\"fuga\" ika=\"tako and tako\"";
        RosettoArguments sut = new RosettoArguments(test);
        assertThat(sut.get("hoge").asString(), is("fuga"));
        assertThat(sut.get("ika").asString(), is("tako and tako"));
        assertThat(sut.get(0).asString(), is("foo"));
    }
    
    @Test
    public void toStringで文字列表現が返る() throws Exception {
        ArrayList<String> args = new ArrayList<String>();
        args.add("aaa");
        args.add("bbb");
        TreeMap<String, String> map = new TreeMap<String, String>();
        map.put("foo", "bar");
        map.put("hoge", "fuga");
        RosettoArguments sut = new RosettoArguments("aaa bbb foo=bar hoge=fuga");
        assertThat(sut.toString(), is(args.toString() + map.toString()));
    }
    
    @Test
    public void parseにnull値を渡すとエラー() throws Exception {
        RosettoArguments sut = new RosettoArguments("foo=bar");
        try {
            sut.parse(null);
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void parseでパースできる() throws Exception {
        RosettoFunction f = new RosettoFunction("testfunc", 
                "arg1", "arg2=dummy", "arg3=v3") {
            @Override
            protected RosettoValue run(ExpandedArguments args) {return Values.VOID;}
        };
        RosettoArguments sut = new RosettoArguments("arg1=v1 v2");
        Map<String, RosettoValue> result = sut.parse(f);
        assertThat(result.get("arg1").asString(), is("v1"));
        assertThat(result.get("arg2").asString(), is("v2"));
        assertThat(result.get("arg3").asString(), is("v3"));
        
        RosettoFunction f2 = new RosettoFunction("testfunc", 
                "file", "volume=", "loop=") {
            @Override
            protected RosettoValue run(ExpandedArguments args) {return Values.VOID;}
        };
        
        RosettoArguments sut2 = new RosettoArguments("storage=foo.mp3");
        
        try {
            sut2.parse(f2);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
    }
  
    @Test
    public void 引数の数が合わないとエラー() throws Exception {
        RosettoFunction f = new RosettoFunction("testfunc", 
                "a", "b", "c") {
            @Override
            protected RosettoValue run(ExpandedArguments args) {return Values.VOID;}
        };
        //これは正常に動作
        new RosettoArguments("A B C").parse(f);
        //これは足りないのでエラー
        try {
            new RosettoArguments("A B").parse(f);
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        //これは多いのでエラー
        try {
            new RosettoArguments("A B C D").parse(f);
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void sizeで含まれる属性数が返る() throws Exception {
        RosettoArguments sut1 = new RosettoArguments("hoge fuga=piyo foo=bar");
        assertThat(sut1.getSize(), is(3));
    }

  
    @Test
    public void containsKeyで指定キーを含んでいる場合にtrueが返る() throws Exception {
        RosettoArguments sut = new RosettoArguments("1=1 2=2 3=3");
        assertThat(sut.containsKey("2"), is(true));
        assertThat(sut.containsKey("5"), is(false));
    }

    @Test
    public void getで指定キーに関連づけられた値が返る() throws Exception {
        RosettoArguments sut = new RosettoArguments("%1 2=2 3=3");
        assertThat(sut.get("2").asString(), is("2"));
        assertThat(sut.get("5"), is(nullValue()));
    }

}
