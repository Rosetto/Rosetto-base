package org.rosetto.functions.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.rosetto.contexts.base.Rosetto;
import org.rosetto.functions.base.BaseFunctions;
import org.rosetto.models.base.elements.RosettoAction;
import org.rosetto.models.base.elements.RosettoValue;
import org.rosetto.models.base.elements.ValueType;
import org.rosetto.models.base.elements.values.ListValue;
import org.rosetto.models.base.elements.values.RosettoFunction;
import org.rosetto.models.base.scenario.Scenario;
import org.rosetto.models.system.FunctionPackage;
import org.rosetto.models.system.Parser;
import org.rosetto.models.system.Scope;
import org.rosetto.utils.base.Values;

public class BaseFunctionsTest {
    
    private Scope testScope;
    
    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
        testScope = new Scope();
    }
    
    @Test
    public void getInstanceTest() throws Exception {
         assertThat(BaseFunctions.getInstance(), is(notNullValue()));
    }
    
    @Test
    public void passTest() throws Exception {
        assertThat(BaseFunctions.pass.execute(testScope), is((RosettoValue)Values.VOID));
    }
    
    @Test
    public void labelTest() throws Exception {
        //実行内容はpassと同じ 
        assertThat(BaseFunctions.label.execute("name=foo title=bar", testScope), 
                    is((RosettoValue)Values.VOID));
        //パースするとlabelになる
        Parser parser = Rosetto.getParser();
        Scenario s = parser.parseScript("Hello, [label foo]World![br]Hello World [label bar]again!");
        assertThat(s.getLabelAt(0), is(nullValue()));
        assertThat(s.getLabelAt(1).getName(), is("foo"));
        assertThat(s.getLabelAt(2).getName(), is("foo"));
        assertThat(s.getLabelAt(3).getName(), is("bar"));
    }
    
    
    @Test
    public void doTest() throws Exception {
        RosettoValue sut1 = BaseFunctions.doActions.execute("[* 2 2]", testScope);
        assertThat(sut1.getType(), is(ValueType.INTEGER));
        assertThat(sut1.asInt(), is(4));
        RosettoValue sut2 = BaseFunctions.doActions.execute("1 2 3 4 5", testScope);
        assertThat(sut2.getType(), is(ValueType.INTEGER));
        assertThat(sut2.asInt(), is(5));
    }
    
    @Test
    public void useTest() throws Exception {
        RosettoFunction f1 = new RosettoFunction("testfunc") {
            private static final long serialVersionUID = 1L;
            @Override
            protected RosettoValue run(Scope scope, ListValue rawArgs) {
                return null;
            }
        };
        FunctionPackage p = new FunctionPackage(f1);
        Rosetto.importPackage(p, "foo");
        assertThat(Rosetto.getAction("testfunc"), is((RosettoAction)Values.NULL));
        assertThat(Rosetto.getAction("foo.testfunc"), is((RosettoAction)f1));
        RosettoValue sut1 = BaseFunctions.use.execute("foo", testScope);
        assertThat(sut1, is((RosettoValue)Values.VOID));
        assertThat(Rosetto.getAction("testfunc"), is((RosettoAction)f1));
        assertThat(Rosetto.getAction("foo.testfunc"), is((RosettoAction)f1));
    }
    
    @Test
    public void firstTest() throws Exception {
         RosettoValue sut1 = BaseFunctions.first.execute("(1 2 3 4 5)", testScope);
         assertThat(sut1.asInt(), is(1));
         RosettoValue sut2 = BaseFunctions.first.execute("()", testScope);
         assertThat(sut2, is((RosettoValue)Values.NULL));
         RosettoValue sut3 = BaseFunctions.first.execute("1", testScope);
         assertThat(sut3.asInt(), is(1));
    }
    
    @Test
    public void restTest() throws Exception {
        RosettoValue sut1 = BaseFunctions.rest.execute("(1 2 3 4 5)", testScope);
        assertThat(sut1.asString(), is("(2 3 4 5)"));
        RosettoValue sut2 = BaseFunctions.rest.execute("(1)", testScope);
        assertThat(sut2, is((RosettoValue)Values.NULL));
        RosettoValue sut3 = BaseFunctions.rest.execute("1", testScope);
        assertThat(sut3, is((RosettoValue)Values.NULL));
        RosettoValue sut4 = BaseFunctions.rest.execute("()", testScope);
        assertThat(sut4, is((RosettoValue)Values.NULL));
    }
    

    @Test
    public void defAndGetGlobalTest() throws Exception {
        assertThat(Rosetto.get("foo"), is((RosettoValue)Values.NULL));
        assertThat(BaseFunctions.getglobal.execute("foo", testScope), is((RosettoValue)Values.NULL));
        
        assertThat(BaseFunctions.def.execute("foo bar", testScope), 
                is((RosettoValue)Values.VOID));
        assertThat(Rosetto.get("foo").asString(), is("bar"));
        assertThat(BaseFunctions.getglobal.execute("foo", testScope).asString(), is("bar"));
        
        assertThat(BaseFunctions.def.execute("bar.baz 100", testScope), 
                is((RosettoValue)Values.VOID));
        assertThat(Rosetto.get("bar.baz").asInt(), is(100));
        assertThat(BaseFunctions.getglobal.execute("bar.baz", testScope).asInt(), is(100));
        
        assertThat(BaseFunctions.def.execute("fuga.hoge 1.234", testScope), 
                is((RosettoValue)Values.VOID));
        assertThat(Rosetto.get("fuga.hoge").asDouble(), is(1.234));
        assertThat(BaseFunctions.getglobal.execute("fuga.hoge", testScope).asDouble(), is(1.234));
    }
    
    @Test
    public void setAndGetLocalTest() throws Exception {
        Scope s = new Scope();
        assertThat(s.get("foo"), is((RosettoValue)Values.NULL));
        assertThat(BaseFunctions.getlocal.execute("foo", s), is((RosettoValue)Values.NULL));
        
        assertThat(BaseFunctions.set.execute("foo bar", s), 
                is((RosettoValue)Values.VOID));
        assertThat(s.get("foo").asString(), is("bar"));
        assertThat(BaseFunctions.getlocal.execute("foo", s).asString(), is("bar"));
        
        assertThat(BaseFunctions.set.execute("bar.baz 100", s), 
                is((RosettoValue)Values.VOID));
        assertThat(s.get("bar.baz").asInt(), is(100));
        assertThat(BaseFunctions.getlocal.execute("bar.baz", s).asInt(), is(100));
        
        assertThat(BaseFunctions.set.execute("fuga.hoge 1.234", s), 
                is((RosettoValue)Values.VOID));
        assertThat(s.get("fuga.hoge").asDouble(), is(1.234));
        assertThat(BaseFunctions.getlocal.execute("fuga.hoge", s).asDouble(), is(1.234));
    }
    
    @Test
    public void defnTest() {
        RosettoValue sut1 = BaseFunctions.defn.execute("foo (x) [* @x 2]", testScope);
        assertThat(sut1.getType(), is(ValueType.FUNCTION));
        assertThat(Rosetto.getAction("foo").execute("2", testScope).asInt(), is(4));
        
        //第二引数にアクション呼び出し以外が渡るとnullが返る
        RosettoValue sut2 = BaseFunctions.defn.execute("foo (x) bar", testScope);
        assertThat(sut2.getType(), is(ValueType.NULL));
        assertThat(Rosetto.getAction("foo").execute(testScope), is((RosettoValue)Values.NULL));
    }

    @Test
    public void fnTest() {
        RosettoValue sut1 = BaseFunctions.fn.execute("(x) [* @x 2]", testScope);
        assertThat(sut1.getType(), is(ValueType.FUNCTION));
        assertThat(((RosettoFunction)sut1).execute("2", testScope).asInt(), is(4));
        
        RosettoValue sut2 = BaseFunctions.fn.execute("(x) [* @x 10] [* @x 100]", testScope);
        assertThat(sut2.getType(), is(ValueType.FUNCTION));
        assertThat(((RosettoFunction)sut2).execute("5", testScope).asInt(), is(500));
    }
    
    @Test
    public void defMacroTest() throws Exception {
        RosettoValue sut1 = BaseFunctions.defmacro.execute("foo (x) {Hello, [write @x]!}", testScope);
        assertThat(sut1.getType(), is(ValueType.SCRIPT));
        
        RosettoValue sut2 = BaseFunctions.defmacro.execute("foo (x) not_script", testScope);
        assertThat(sut2.getType(), is(ValueType.NULL));
    }
    
    @Test
    public void macroTest() throws Exception {
        RosettoValue sut1 = BaseFunctions.macro.execute("(x) {Hello, [write @x]!}", testScope);
        assertThat(sut1.getType(), is(ValueType.SCRIPT));
        
        RosettoValue sut2 = BaseFunctions.macro.execute("(x) not_script", testScope);
        assertThat(sut2.getType(), is(ValueType.NULL));
    }

}
