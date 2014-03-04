package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import info.rosetto.BaseTestUtils;
import info.rosetto.models.base.elements.RosettoAction;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.base.elements.values.LambdaFunction;
import info.rosetto.models.base.elements.values.ListValue;
import info.rosetto.models.base.elements.values.RosettoFunction;
import info.rosetto.models.base.elements.values.ScriptValue;
import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.system.FunctionPackage;
import info.rosetto.models.system.ScenarioPlayer;
import info.rosetto.models.system.Scope;
import info.rosetto.utils.base.Values;

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
    
    @SuppressWarnings("serial")
    @Test
    public void initializeWithArgsTest() throws Exception {
        GlobalVariables sut1 = new GlobalVariables();
        ActionContext sut2 = new ActionContext();
        sut2.defineAction("foo", new RosettoFunction("foo") {
            @Override
            protected RosettoValue run(Scope scope, ListValue rawArgs) {
                return null;
            }
        });
        SystemContext sut3 = new SystemContext();
        Contexts.initialize(sut1, sut2, sut3);
        assertThat(Contexts.getVariableContext(), is(sut1));
        assertThat(Contexts.getAction("foo"), is(sut2.get("foo")));
        assertThat(Contexts.getParser(), is(sut3.getParser()));
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
        
        Contexts.define("hoge.fuga", 100.0);
        assertThat(Contexts.get("hoge.fuga").getType(), is(ValueType.DOUBLE));
        
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
    public void getAndDefineFunctionTest() throws Exception {
        Contexts.initialize();
        RosettoFunction f1 = new RosettoFunction("func") {
            private static final long serialVersionUID = 1694180059203694661L;
            @Override
            protected RosettoValue run(Scope functionScope, ListValue args) {
                return Values.VOID;
            }
        };
        Contexts.defineFunction(f1);
        assertThat(Contexts.getAction("func"), is((RosettoValue)f1));
        
        LambdaFunction f2 = new LambdaFunction() {
            private static final long serialVersionUID = -5525955984024609753L;

            @Override
            protected RosettoValue run(Scope scope, ListValue rawArgs) {
                // TODO Auto-generated method stub
                return null;
            }
        };
        
        Contexts.defineFunction("func2", f2);
        assertThat(Contexts.getAction("func2"), is((RosettoValue)f2));
        
        assertThat(Contexts.getAction("org.example.not-found-func"), is((RosettoAction)Values.NULL));
    }
    
    @Test
    public void getAndDefineMacroTest() throws Exception {
        Contexts.initialize();
        Contexts.defineMacro("foo", ListValue.EMPTY, (ScriptValue)Values.create("{Hello, World!}"));
        
        assertThat(Contexts.getAction("foo").getType(), is(ValueType.SCRIPT));
        assertThat(Contexts.getAction("foo").asString(), is("{Hello, World!}"));
        assertThat(Contexts.getAction("org.example.not-found-macro"), is((RosettoAction)Values.NULL));
    }
    
    
    @Test
    public void getAndSetParserTest() throws Exception {
        Contexts.initialize();
        //初期状態のパーサーは通常のRosettoのもの
        Scenario sut1 = Contexts.getParser().parseScript("foobar[br]");
        assertThat(sut1.getUnitAt(0).getContent(), is("foobar"));
        assertThat(sut1.getUnitAt(0).getAction().toString(), is("[br]"));
        //ダミーパーサーを追加
        Contexts.setParser(BaseTestUtils.createDummyParser());
        Scenario sut2 = Contexts.getParser().parseScript("foobar[br]");
        assertThat(sut2.getUnitAt(0).getContent(), is("TestParser"));
        assertThat(sut2.getUnitAt(0).getAction().toString(), is("[pass]"));
    }
    
    @SuppressWarnings("serial")
    @Test
    public void importAndUsePackageTest() throws Exception {
        Contexts.initialize();
        
        RosettoFunction f1 = new RosettoFunction("bar") {
            @Override
            protected RosettoValue run(Scope scope, ListValue rawArgs) {
                return null;
            }
        };
        FunctionPackage sut = new FunctionPackage(f1);
        
        assertThat(Contexts.getAction("foo.bar"), is((RosettoValue)Values.NULL));
        assertThat(Contexts.getAction("bar"), is((RosettoValue)Values.NULL));
        
        Contexts.importPackage(sut, "foo");
        assertThat(Contexts.getAction("foo.bar"), is((RosettoValue)f1));
        assertThat(Contexts.getAction("bar"), is((RosettoValue)Values.NULL));
        
        Contexts.usePackage("foo");
        assertThat(Contexts.getAction("foo.bar"), is((RosettoValue)f1));
        assertThat(Contexts.getAction("bar"), is((RosettoValue)f1));
    }
    
    @Test
    public void getAndSetPlayerTest() throws Exception {
        Contexts.initialize();
        //初期状態でPlayerは空
        assertThat(Contexts.getPlayer(), is(nullValue()));
        //Playerを追加
        Contexts.setPlayer(new ScenarioPlayer() {
            @Override
            public void pushScenario(Scenario scenario, Scope playingScope) {}
        });
        assertThat(Contexts.getPlayer(), is(notNullValue()));
    }
    
    @Test
    public void getAndSetWholeSpaceTest() throws Exception {
        Contexts.initialize();
        
        //変更できる
        GlobalVariables ws = new GlobalVariables();
        ws.createNameSpace("foo");
        Contexts.setVariableContext(ws);
        assertThat(Contexts.getVariableContext().containsNameSpace("foo"), is(true));
        assertThat(Contexts.getVariableContext().containsNameSpace("bar"), is(false));
        
        //getNameSpaceで名前空間が生成される
        assertThat(Contexts.getNameSpace("bar"), is(notNullValue()));
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
