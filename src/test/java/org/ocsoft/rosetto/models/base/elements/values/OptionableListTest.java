package org.ocsoft.rosetto.models.base.elements.values;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.ocsoft.rosetto.contexts.base.Rosetto;
import org.ocsoft.rosetto.models.base.elements.RosettoValue;
import org.ocsoft.rosetto.models.base.elements.values.ListValue;
import org.ocsoft.rosetto.models.base.elements.values.RosettoFunction;
import org.ocsoft.rosetto.models.system.Scope;
import org.ocsoft.rosetto.utils.base.Values;

@SuppressWarnings("serial")
public class OptionableListTest {
    
    private Scope testScope;
    
    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
        testScope = new Scope();
    }
    
    @Test
    public void インスタンス化の際にnullが渡るとエラー() throws Exception {
        try {
            ListValue.createFromString((String)null);
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void 括弧付きの文字列からインスタンス化() throws Exception {
        ListValue sut1 = ListValue.createFromString("a [b] (c)");
        assertThat(sut1.size(), is(3));
        
        ListValue sut2 = ListValue.createFromString("a [b c] (d e)");
        assertThat(sut2.size(), is(3));
        
        ListValue sut3 = ListValue.createFromString("a [b [c]] (d (e))");
        assertThat(sut3.size(), is(3));
        
        ListValue sut4 = ListValue.createFromString("a [b [c (d)]] (e (f [g (h)]))");
        assertThat(sut4.size(), is(3));
    }
    
    @Test
    public void インスタンス化の際に属性値を囲んでいるダブルクオートは外される() throws Exception {
        String test = " \"foo\" bar=baz hoge=\"fuga\" ika=\"tako and tako\"";
        ListValue sut = ListValue.createFromString(test);
        assertThat(sut.get("hoge").asString(), is("fuga"));
        assertThat(sut.get("ika").asString(), is("tako and tako"));
        assertThat(sut.getAt(0).asString(), is("foo"));
    }
    
    @Test
    public void toStringで文字列表現が返る() throws Exception {
        ArrayList<String> args = new ArrayList<String>();
        args.add("aaa");
        args.add("bbb");
        TreeMap<String, String> map = new TreeMap<String, String>();
        map.put("foo", "bar");
        map.put("hoge", "fuga");
        ListValue sut = ListValue.createFromString("aaa bbb foo=bar hoge=fuga");
        assertThat(sut.toString(), is("(aaa bbb foo=bar hoge=fuga)"));
    }
    
    @Test
    public void parseにnull値を渡すとエラー() throws Exception {
        ListValue sut = ListValue.createFromString("foo=bar");
        try {
            sut.bind(null, testScope);
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void parseでパースできる() throws Exception {
        RosettoFunction f = new RosettoFunction("testfunc", 
                "arg1", "arg2=dummy", "arg3=v3") {
            @Override
            protected RosettoValue run(Scope functionScope, ListValue args) {return Values.VOID;}
        };
        ListValue sut = ListValue.createFromString("arg1=v1 v2");
        Map<String, RosettoValue> result = sut.bind(f, testScope);
        assertThat(result.get("arg1").asString(), is("v1"));
        assertThat(result.get("arg2").asString(), is("v2"));
        assertThat(result.get("arg3").asString(), is("v3"));
        
        RosettoFunction f2 = new RosettoFunction("testfunc", 
                "file", "volume=", "loop=") {
            @Override
            protected RosettoValue run(Scope functionScope, ListValue args) {return Values.VOID;}
        };
        
        ListValue sut2 = ListValue.createFromString("storage=foo.mp3");
        
        try {
            sut2.bind(f2, testScope);
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
            protected RosettoValue run(Scope functionScope, ListValue args) {return Values.VOID;}
        };
        //これは正常に動作
        ListValue.createFromString("A B C").bind(f, testScope);
        //これは足りないのでエラー
        try {
            ListValue.createFromString("A B").bind(f, testScope);
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        //これは多いのでエラー
        try {
            ListValue.createFromString("A B C D").bind(f, testScope);
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void 可変長引数() throws Exception {
        RosettoFunction f = new RosettoFunction("testfunc", 
                "a", "b", "*rest") {
            @Override
            protected RosettoValue run(Scope functionScope, ListValue args) {return Values.VOID;}
        };
        
        Map<String, RosettoValue> sut1 = ListValue.createFromString("A B C").bind(f, testScope);
        assertThat(sut1.get("a").asString(), is("A"));
        assertThat(sut1.get("b").asString(), is("B"));
        assertThat(sut1.get("rest").asString(), is("(C)"));
        
        Map<String, RosettoValue> sut2 = ListValue.createFromString("A B C D E F").bind(f, testScope);
        assertThat(sut2.get("a").asString(), is("A"));
        assertThat(sut2.get("b").asString(), is("B"));
        assertThat(sut2.get("rest").asString(), is("(C D E F)"));
    }
    
    @Test
    public void sizeで含まれる属性数が返る() throws Exception {
        ListValue sut1 = ListValue.createFromString("hoge fuga=piyo foo=bar");
        assertThat(sut1.size(), is(1));
    }

  
    @Test
    public void containsKeyで指定キーを含んでいる場合にtrueが返る() throws Exception {
        ListValue sut = ListValue.createFromString("1=1 2=2 3=3");
        assertThat(sut.containsKey("2"), is(true));
        assertThat(sut.containsKey("5"), is(false));
    }

    @Test
    public void getで指定キーに関連づけられた値が返る() throws Exception {
        ListValue sut = ListValue.createFromString("%1 2=2 3=3");
        assertThat(sut.get("2").asString(), is("2"));
        assertThat(sut.get("5"), is((RosettoValue)Values.NULL));
    }

}
