package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.functions.base.BaseFunctions;
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
    public void getAndDefineValueTest() throws Exception {
        Contexts.initialize();
        
        //存在しない変数の値はnull
        assertThat(Contexts.get("foobar"), is((RosettoValue)Values.NULL));
        
        assertThat(Contexts.get("hoge.fuga"), is((RosettoValue)Values.NULL));
        
        //値をセットする
        Contexts.define("foobar", "baz");
        assertThat(Contexts.get("foobar").asString(), is("baz"));
        
        Contexts.define("hoge.fuga", 100);
        assertThat(Contexts.get("hoge.fuga").asInt(), is(100));
        
        //値を上書きする
        Contexts.define("foobar", true);
        assertThat(Contexts.get("foobar").asBool(), is(true));
        
        Contexts.define("hoge.fuga", 100L);
        assertThat(Contexts.get("hoge.fuga").asLong(), is(100L));
        assertThat(Contexts.get("hoge.fuga").asInt(), is(100));
        assertThat(Contexts.get("hoge.fuga").asDouble(), is(100.0));
        
        //nullキーでsetするとエラー
        try {
            Contexts.define(null, Values.create("hoge"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        //nullキーでgetするとnull
        assertThat(Contexts.get(null), is((RosettoValue)Values.NULL));
        
        //空文字キーでsetするとエラー
        try {
            Contexts.define("", Values.create("hoge"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        //空文字のキーは常に存在しないのでnull
        assertThat(Contexts.get(""), is((RosettoValue)Values.NULL));
        
        //nullvalueでsetするとエラー
        try {
            Contexts.define("fuga", (RosettoValue)null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        //dotで終わるキーでsetするとエラー
        try {
            Contexts.define("foo.", Values.create("hoge"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void getAsFunctionTest() throws Exception {
        Contexts.initialize();
        Contexts.defineFunction(new RosettoFunction("func") {
            private static final long serialVersionUID = 1694180059203694661L;
            @Override
            protected RosettoValue run(ExpandedArguments args) {
                return Values.VOID;
            }
        });
        assertThat(Contexts.getFunction("func").getName(), is("func"));
        assertThat(Contexts.getFunction("org.example.not-found-func"), is(BaseFunctions.pass));
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
        assertThat(Contexts.getVariableContext().getCreatedNameSpaceCount(), is(0));
        //getで存在しない名前空間を指定すると生成される
        Contexts.getNameSpace("some.not.found.namespace");
        assertThat(Contexts.getVariableContext().getCreatedNameSpaceCount(), is(1));
        //既に存在するならそのまま
        Contexts.getNameSpace("some.not.found.namespace");
        assertThat(Contexts.getVariableContext().getCreatedNameSpaceCount(), is(1));
    }
    
    @Test
    public void getAndSetWholeSpaceTest() throws Exception {
        Contexts.initialize();
        
        //デフォルトの全体名前空間は空
        assertThat(Contexts.getVariableContext().getCreatedNameSpaceCount(), is(0));
        
        //変更できる
        VariableContext ws = new VariableContext();
        ws.createNameSpace("foo");
        ws.createNameSpace("bar");
        Contexts.setVariableContext(ws);
        assertThat(Contexts.getVariableContext().getCreatedNameSpaceCount(), is(2));
        assertThat(Contexts.getVariableContext().containsNameSpace("foo"), is(true));
        assertThat(Contexts.getVariableContext().containsNameSpace("bar"), is(true));
        
        //nullセットでエラー
        try {
            Contexts.setVariableContext(null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    

}
