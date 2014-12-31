package org.ocsoft.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.ocsoft.rosetto.BaseTestUtils;
import org.ocsoft.rosetto.contexts.base.ActionContext;
import org.ocsoft.rosetto.contexts.base.GlobalVariables;
import org.ocsoft.rosetto.contexts.base.Rosetto;
import org.ocsoft.rosetto.contexts.base.SystemContext;
import org.ocsoft.rosetto.models.base.elements.RosettoAction;
import org.ocsoft.rosetto.models.base.elements.RosettoValue;
import org.ocsoft.rosetto.models.base.elements.ValueType;
import org.ocsoft.rosetto.models.base.elements.values.LambdaFunction;
import org.ocsoft.rosetto.models.base.elements.values.ListValue;
import org.ocsoft.rosetto.models.base.elements.values.RosettoFunction;
import org.ocsoft.rosetto.models.base.elements.values.ScriptValue;
import org.ocsoft.rosetto.models.base.scenario.Scenario;
import org.ocsoft.rosetto.models.system.FunctionPackage;
import org.ocsoft.rosetto.models.system.ScenarioPlayer;
import org.ocsoft.rosetto.models.system.Scope;
import org.ocsoft.rosetto.utils.base.Values;

public class ContextsTest {
    
    @Before
    public void setUp() {
        //Contextsを削除した状態からテスト.
        if(Rosetto.isInitialized()) Rosetto.dispose();
    }
    
    @Test
    public void InitializeAndDisposeTest() throws Exception {
        //setupで破棄済み
        assertThat(Rosetto.isInitialized(), is(false));
        
        //破棄されている状態で各種操作するとエラー
        try {
            Rosetto.get("foo");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalStateException.class));
        }
        
        //初期状態ではinitialize可能
        Rosetto.initialize();
        assertThat(Rosetto.isInitialized(), is(true));
        
        //既にinitializeされていると例外
        try {
            Rosetto.initialize();
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalStateException.class));
        }
        assertThat(Rosetto.isInitialized(), is(true));
        
        //破棄するとinitialize可能
        Rosetto.dispose();
        assertThat(Rosetto.isInitialized(), is(false));
        Rosetto.initialize();
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
        Rosetto.initialize(sut1, sut2, sut3);
        assertThat(Rosetto.getVariableContext(), is(sut1));
        assertThat(Rosetto.getAction("foo"), is(sut2.get("foo")));
        assertThat(Rosetto.getParser(), is(sut3.getParser()));
    }
    
    @Test
    public void getAndDefineValueTest() throws Exception {
        Rosetto.initialize();
        
        //存在しない変数の値はnull
        assertThat(Rosetto.get("foobar"), is((RosettoValue)Values.NULL));
        
        assertThat(Rosetto.get("hoge.fuga"), is((RosettoValue)Values.NULL));
        
        //値をセットする
        Rosetto.define("foobar", "baz");
        assertThat(Rosetto.get("foobar").asString(), is("baz"));
        
        Rosetto.define("hoge.fuga", 100);
        assertThat(Rosetto.get("hoge.fuga").asInt(), is(100));
        
        Rosetto.define("hoge.fuga", 100.0);
        assertThat(Rosetto.get("hoge.fuga").getType(), is(ValueType.DOUBLE));
        
        //値を上書きする
        Rosetto.define("foobar", true);
        assertThat(Rosetto.get("foobar").asBool(), is(true));
        
        Rosetto.define("hoge.fuga", 100L);
        assertThat(Rosetto.get("hoge.fuga").asLong(), is(100L));
        assertThat(Rosetto.get("hoge.fuga").asInt(), is(100));
        assertThat(Rosetto.get("hoge.fuga").asDouble(), is(100.0));
        
        //nullキーでsetするとエラー
        try {
            Rosetto.define(null, Values.create("hoge"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        //nullキーでgetするとnull
        assertThat(Rosetto.get(null), is((RosettoValue)Values.NULL));
        
        //空文字キーでsetするとエラー
        try {
            Rosetto.define("", Values.create("hoge"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        //nullvalueでsetするとエラー
        try {
            Rosetto.define("fuga", (RosettoValue)null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        //dotで終わるキーでsetするとエラー
        try {
            Rosetto.define("foo.", Values.create("hoge"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void getAndDefineFunctionTest() throws Exception {
        Rosetto.initialize();
        RosettoFunction f1 = new RosettoFunction("func") {
            private static final long serialVersionUID = 1694180059203694661L;
            @Override
            protected RosettoValue run(Scope functionScope, ListValue args) {
                return Values.VOID;
            }
        };
        Rosetto.defineFunction(f1);
        assertThat(Rosetto.getAction("func"), is((RosettoValue)f1));
        
        LambdaFunction f2 = new LambdaFunction() {
            private static final long serialVersionUID = -5525955984024609753L;

            @Override
            protected RosettoValue run(Scope scope, ListValue rawArgs) {
                // TODO Auto-generated method stub
                return null;
            }
        };
        
        Rosetto.defineFunction("func2", f2);
        assertThat(Rosetto.getAction("func2"), is((RosettoValue)f2));
        
        assertThat(Rosetto.getAction("org.example.not-found-func"), is((RosettoAction)Values.NULL));
    }
    
    @Test
    public void getAndDefineMacroTest() throws Exception {
        Rosetto.initialize();
        Rosetto.defineMacro("foo", ListValue.EMPTY, (ScriptValue)Values.create("{Hello, World!}"));
        
        assertThat(Rosetto.getAction("foo"), is(not((RosettoValue)Values.NULL)));
        assertThat(Rosetto.getAction("org.example.not-found-macro"), is((RosettoAction)Values.NULL));
    }
    
    
    @Test
    public void getAndSetParserTest() throws Exception {
        Rosetto.initialize();
        //初期状態のパーサーは通常のRosettoのもの
        Scenario sut1 = Rosetto.getParser().parseScript("foobar[br]");
        assertThat(sut1.getUnitAt(0).getText(), is("foobar"));
        assertThat(sut1.getUnitAt(0).getAction().toString(), is("[br]"));
        //ダミーパーサーを追加
        Rosetto.setParser(BaseTestUtils.createDummyParser());
        Scenario sut2 = Rosetto.getParser().parseScript("foobar[br]");
        assertThat(sut2.getUnitAt(0).getText(), is("TestParser"));
        assertThat(sut2.getUnitAt(0).getAction().toString(), is("[pass]"));
    }
    
    @SuppressWarnings("serial")
    @Test
    public void importAndUsePackageTest() throws Exception {
        Rosetto.initialize();
        
        RosettoFunction f1 = new RosettoFunction("bar") {
            @Override
            protected RosettoValue run(Scope scope, ListValue rawArgs) {
                return null;
            }
        };
        FunctionPackage sut = new FunctionPackage(f1);
        
        assertThat(Rosetto.getAction("foo.bar"), is((RosettoValue)Values.NULL));
        assertThat(Rosetto.getAction("bar"), is((RosettoValue)Values.NULL));
        
        Rosetto.importPackage(sut, "foo");
        assertThat(Rosetto.getAction("foo.bar"), is((RosettoValue)f1));
        assertThat(Rosetto.getAction("bar"), is((RosettoValue)Values.NULL));
        
        Rosetto.usePackage("foo");
        assertThat(Rosetto.getAction("foo.bar"), is((RosettoValue)f1));
        assertThat(Rosetto.getAction("bar"), is((RosettoValue)f1));
    }
    
    @Test
    public void getAndSetPlayerTest() throws Exception {
        Rosetto.initialize();
        //初期状態でPlayerは空
        assertThat(Rosetto.getPlayer(), is(nullValue()));
        //Playerを追加
        Rosetto.setPlayer(new ScenarioPlayer() {
            @Override
            public void pushScenario(Scenario scenario, Scope playingScope) {}
        });
        assertThat(Rosetto.getPlayer(), is(notNullValue()));
    }
    
    @Test
    public void getAndSetWholeSpaceTest() throws Exception {
        Rosetto.initialize();
        
        //変更できる
        GlobalVariables ws = new GlobalVariables();
        ws.createNameSpace("foo");
        Rosetto.setVariableContext(ws);
        assertThat(Rosetto.getVariableContext().containsNameSpace("foo"), is(true));
        assertThat(Rosetto.getVariableContext().containsNameSpace("bar"), is(false));
        
        //getNameSpaceで名前空間が生成される
        assertThat(Rosetto.getNameSpace("bar"), is(notNullValue()));
        assertThat(Rosetto.getVariableContext().containsNameSpace("foo"), is(true));
        assertThat(Rosetto.getVariableContext().containsNameSpace("bar"), is(true));
        
        //nullセットでエラー
        try {
            Rosetto.setVariableContext(null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    

}
