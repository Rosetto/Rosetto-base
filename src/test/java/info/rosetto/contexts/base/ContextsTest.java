package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.models.base.function.ExpandedArguments;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.parser.ArgumentSyntax;
import info.rosetto.models.base.parser.RosettoParser;
import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.base.scenario.Unit;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.utils.base.Values;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class ContextsTest {
    
    @Before
    public void setUp() {
        //Contextsを削除した状態からテスト.
        if(Contexts.isInitialized()) Contexts.dispose();
    }
    
    @Test
    public void InitializeAndDisposeTest() throws Exception {
        //setupで破棄済み
        assertThat(Contexts.isInitialized(), is(false));
        
        //破棄されている状態で各種操作するとエラー
        try {
            Contexts.get("foo");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalStateException.class));
        }
        
        //初期状態ではinitialize可能
        Contexts.initialize();
        assertThat(Contexts.isInitialized(), is(true));
        
        //既にinitializeされていると例外
        try {
            Contexts.initialize();
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalStateException.class));
        }
        assertThat(Contexts.isInitialized(), is(true));
        
        //破棄するとinitialize可能
        Contexts.dispose();
        assertThat(Contexts.isInitialized(), is(false));
        Contexts.initialize();
    }
    
    @Test
    public void getAndSetValueTest() throws Exception {
        Contexts.initialize();
        
        //存在しない変数の値はnull
        assertThat(Contexts.get("foobar"), is(nullValue()));
        
        //値をセットする
        Contexts.set("foobar", Values.create("baz"));
        assertThat(Contexts.get("foobar").asString(), is("baz"));
        
        //値を上書きする
        Contexts.set("foobar", true);
        assertThat(Contexts.get("foobar").asBool(), is(true));
        Contexts.set("foobar", 100L);
        assertThat(Contexts.get("foobar").asLong(), is(100L));
        assertThat(Contexts.get("foobar").asInt(), is(100));
        assertThat(Contexts.get("foobar").asDouble(), is(100.0));
        
        //nullキーでsetするとエラー
        try {
            Contexts.set(null, Values.create("hoge"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        //nullキーでgetするとnull
        assertThat(Contexts.get(null), is(nullValue()));
        
        //空文字キーでsetするとエラー
        try {
            Contexts.set("", Values.create("hoge"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        //空文字のキーは常に存在しないのでnull
        assertThat(Contexts.get(""), is(nullValue()));
        
        //nullvalueでsetするとエラー
        try {
            Contexts.set("fuga", (RosettoValue)null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void getAsFunctionTest() throws Exception {
        Contexts.initialize();
        
        Contexts.set("org.example.foo", new RosettoFunction("func") {
            private static final long serialVersionUID = 1694180059203694661L;
            @Override
            protected RosettoValue run(ExpandedArguments args) {
                return Values.VOID;
            }
        });
        Contexts.set("org.example.bar", "not function value");
        assertThat(Contexts.getAsFunction("org.example.foo").getName(), is("func"));
        assertThat(Contexts.getAsFunction("org.example.bar"), is(nullValue()));
        assertThat(Contexts.getAsFunction("org.example.not-found-value"), is(nullValue()));
    }
    
    @Test
    public void getAndPutAbsoluteValueTest() throws Exception {
        Contexts.initialize();
        
        //名前空間指定付きで登録、この段階では絶対参照しかできない
        Contexts.set("org.example.foo", "bar");
        assertThat(Contexts.get("org.example.foo").asString(), is("bar"));
        assertThat(Contexts.get("foo"), is(nullValue()));
        
        //名前空間がcurrentになれば見えるようになる
        Contexts.setCurrentNameSpace("org.example");
        assertThat(Contexts.get("org.example.foo").asString(), is("bar"));
        assertThat(Contexts.get("foo").asString(), is("bar"));
        
        //dotで終わる変数名は不可
        try {
            Contexts.set("org.example.foobar.", "baz");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void getAndSetParserTest() throws Exception {
        Contexts.initialize();
        
        Contexts.setParser(new RosettoParser() {
            @Override
            public Scenario parseString(String script) {
                return new Scenario(new Unit("TestParser"));
            }
            @Override
            public Scenario parseFile(URL url) {
                return null;
            }
            @Override
            public ArgumentSyntax getArgumentSyntax() {
                return null;
            }
            @Override
            public Scenario parseFile(File file) {
                return null;
            }
        });
        assertThat(Contexts.getParser().parseString("foobar")
                .getUnitAt(0).getContent(), is("TestParser"));
        
    }
    
    
    @Test
    public void getAndSetNameSpaceTest() throws Exception {
        Contexts.initialize();
        
        //デフォルトの名前空間は"story"
        assertThat(Contexts.getCurrentNameSpace().getName(), is("story"));
        Contexts.set("namespacenum", 1);
        assertThat(Contexts.get("namespacenum").asInt(), is(1));
        assertThat(Contexts.getWholeSpace().getCreatedNameSpaceCount(), is(1));
        
        //変更できる
        Contexts.setCurrentNameSpace("foobar");
        assertThat(Contexts.getCurrentNameSpace().getName(), is("foobar"));
        Contexts.set("namespacenum", 2.0);
        assertThat(Contexts.get("namespacenum").asInt(), is(2));
        assertThat(Contexts.getWholeSpace().getCreatedNameSpaceCount(), is(2));
        
        //戻せる
        Contexts.setCurrentNameSpace("story");
        assertThat(Contexts.get("namespacenum").asInt(), is(1));
        assertThat(Contexts.getWholeSpace().getCreatedNameSpaceCount(), is(2));
        
        //getで存在しない名前空間を指定すると生成される
        Contexts.getNameSpace("some.not.found.namespace");
        assertThat(Contexts.getWholeSpace().getCreatedNameSpaceCount(), is(3));
        //既に存在するならそのまま
        Contexts.getNameSpace("story");
        assertThat(Contexts.getWholeSpace().getCreatedNameSpaceCount(), is(3));
        
        //nullセットでエラー
        try {
            Contexts.setCurrentNameSpace(null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        //空文字セットでエラー
        try {
            Contexts.setCurrentNameSpace("");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void getAndSetWholeSpaceTest() throws Exception {
        Contexts.initialize();
        
        //デフォルトの全体名前空間は"story"だけを持つ
        assertThat(Contexts.getWholeSpace().getCreatedNameSpaceCount(), is(1));
        assertThat(Contexts.getWholeSpace().containsNameSpace("story"), is(true));
        assertThat(Contexts.getCurrentNameSpace().getName(), is("story"));
        
        //変更できる
        WholeSpace ws = new WholeSpace();
        ws.createNameSpace("foo");
        ws.createNameSpace("bar");
        ws.setCurrentNameSpace("foo");
        Contexts.setWholeSpace(ws);
        assertThat(Contexts.getWholeSpace().getCreatedNameSpaceCount(), is(3));
        assertThat(Contexts.getWholeSpace().containsNameSpace("story"), is(true));
        assertThat(Contexts.getWholeSpace().containsNameSpace("foo"), is(true));
        assertThat(Contexts.getWholeSpace().containsNameSpace("bar"), is(true));
        assertThat(Contexts.getCurrentNameSpace().getName(), is("foo"));
        
        //nullセットでエラー
        try {
            Contexts.setWholeSpace(null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void referTest() throws Exception {
        Contexts.initialize();
        assertThat(Contexts.getCurrentNameSpace().getName(), is("story"));
        
        NameSpace foo = Contexts.getNameSpace("foo");
        foo.set("bar", Values.create(100L));
        foo.set("baz", Values.create(false));
        
        assertThat(Contexts.get("foo.bar").asLong(), is(100L));
        assertThat(Contexts.get("foo.baz").asBool(), is(false));
        assertThat(Contexts.get("hoge.bar"), is(nullValue()));
        assertThat(Contexts.get("hoge.baz"), is(nullValue()));
        
         Contexts.refer("foo", "hoge");
         assertThat(Contexts.get("foo.bar").asLong(), is(100L));
         assertThat(Contexts.get("foo.baz").asBool(), is(false));
         assertThat(Contexts.get("hoge.bar").asLong(), is(100L));
         assertThat(Contexts.get("hoge.baz").asBool(), is(false));
    }
    
    
    @Test
    public void includeTest() throws Exception {
        Contexts.initialize();
        assertThat(Contexts.getCurrentNameSpace().getName(), is("story"));
        
        NameSpace foo = Contexts.getNameSpace("foo.bar.baz");
        foo.set("hoge", Values.create(12.345));
        foo.set("fuga", Values.create("piyo"));
        assertThat(Contexts.get("foo.bar.baz.hoge").asDouble(), is(12.345));
        assertThat(Contexts.get("foo.bar.baz.fuga").asString(), is("piyo"));
        assertThat(Contexts.get("hoge"), is(nullValue()));
        assertThat(Contexts.get("fuga"), is(nullValue()));
        
        Contexts.include("foo.bar.baz");
        assertThat(Contexts.get("foo.bar.baz.hoge").asDouble(), is(12.345));
        assertThat(Contexts.get("foo.bar.baz.fuga").asString(), is("piyo"));
        assertThat(Contexts.get("hoge").asDouble(), is(12.345));
        assertThat(Contexts.get("fuga").asString(), is("piyo"));
    }

}
