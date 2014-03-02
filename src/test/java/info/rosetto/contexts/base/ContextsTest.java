package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import info.rosetto.models.base.elements.RosettoAction;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.values.ListValue;
import info.rosetto.models.base.elements.values.ScriptValue;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.base.scenario.Unit;
import info.rosetto.models.system.Parser;
import info.rosetto.models.system.Scope;
import info.rosetto.utils.base.Values;

import java.util.List;

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
            protected RosettoValue run(Scope functionScope, ListValue args) {
                return Values.VOID;
            }
        });
        assertThat(Contexts.getAction("func").getName(), is("func"));
        assertThat(Contexts.getAction("org.example.not-found-func"), is((RosettoAction)Values.NULL));
    }
    
    
    @Test
    public void getAndSetParserTest() throws Exception {
        Contexts.initialize();
        
        Contexts.setParser(new Parser() {
            @Override
            public Scenario parseScript(String script) {
                return new Scenario(new Unit("TestParser"));
            }
            @Override
            public Scenario parseScript(List<String> scriptLines) {
                return null;
            }
            @Override
            public RosettoValue parseElement(String actionCall) {
                return null;
            }
            @Override
            public List<String> splitElements(String elements) {
                return null;
            }
            @Override
            public Scenario parseScript(ScriptValue script) {
                // TODO Auto-generated method stub
                return null;
            }
        });
        assertThat(Contexts.getParser().parseScript("foobar")
                .getUnitAt(0).getContent(), is("TestParser"));
        
    }
    
    @Test
    public void getAndSetWholeSpaceTest() throws Exception {
        Contexts.initialize();
        
        //変更できる
        GlobalVariables ws = new GlobalVariables();
        ws.createNameSpace("foo");
        ws.createNameSpace("bar");
        Contexts.setVariableContext(ws);
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
