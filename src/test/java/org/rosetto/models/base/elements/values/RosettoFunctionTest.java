package org.rosetto.models.base.elements.values;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.rosetto.contexts.base.Rosetto;
import org.rosetto.models.base.elements.RosettoValue;
import org.rosetto.models.base.elements.ValueType;
import org.rosetto.models.base.elements.values.ListValue;
import org.rosetto.models.base.elements.values.RosettoFunction;
import org.rosetto.models.system.Scope;
import org.rosetto.system.exceptions.NotConvertibleException;
import org.rosetto.utils.base.Values;

@SuppressWarnings("serial")
public class RosettoFunctionTest {
    
    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
    }
    
    
    @Test
    public void constructorTest() throws Exception {
        RosettoFunction f1 = new RosettoFunction("f1") {
            @Override
            protected RosettoValue run(Scope functionScope, ListValue args) {
                return Values.VOID;
            }
        };
        assertThat(f1.getName(), is("f1"));
        assertThat(f1.getType(), is(ValueType.FUNCTION));
        assertThat(f1.getValue(), is(f1));
        assertThat(f1.evaluate(new Scope()), is((RosettoValue)f1));
        assertThat(f1.getArguments().size(), is(0));
        assertThat(f1.asString(), is("[f1]"));
        
        RosettoFunction f2 = new RosettoFunction("f2",
                "hoge", "fuga", "foo=bar") {
            @Override
            protected RosettoValue run(Scope functionScope, ListValue args) {
                return Values.VOID;
            }
        };
        assertThat(f2.getName(), is("f2"));
        assertThat(f2.getArguments().size(), is(3));
        assertThat(f2.asString(), is("[f2 hoge fuga foo=bar]"));
        
        RosettoFunction f3 = new RosettoFunction("f3", 
                (String[])null) {
            @Override
            protected RosettoValue run(Scope functionScope, ListValue args) {return Values.VOID;}
        };
        assertThat(f3.getName(), is("f3"));
        assertThat(f3.getArguments().size(), is(0));
        assertThat(f3.asString(), is("[f3]"));
        
        try {
            new RosettoFunction(null, "foo") {
                @Override
                protected RosettoValue run(Scope scope, ListValue rawArgs) {
                    return null;
                }
            };
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void executeTest() throws Exception {
        final StringBuilder sb = new StringBuilder();
        RosettoFunction sut = new RosettoFunction("f1", "text") {
            @Override
            protected RosettoValue run(Scope functionScope, ListValue args) {
                sb.append(functionScope.get("text").asString());
                return Values.VOID;
            }
        };
        //初期状態は空
        assertThat(sb.length(), is(0));
        //引数を与えると読める
        sut.execute("text=foo", new Scope());
        assertThat(sb.toString(), is("foo"));
    }
    
    @Test
    public void valueConvertTest() throws Exception {
        RosettoFunction sut = new RosettoFunction("f1") {
            @Override
            protected RosettoValue run(Scope functionScope, ListValue args) {
                return Values.VOID;
            }
        };
        //asStringは関数呼び出し表記が返る
        assertThat(sut.asString(), is("[f1]"));
        assertThat(sut.asString("dummy"), is("[f1]"));
        
        //リストとしては単一要素扱い
        ValueTestUtils.isSingleValue(sut);
        
        //数値や真偽値への変換は常にデフォルト値が返る
        assertThat(sut.asBool(true), is(true));
        assertThat(sut.asInt(100), is(100));
        assertThat(sut.asLong(100L), is(100L));
        assertThat(sut.asDouble(100.0), is(100.0));
        
        try {
            sut.asBool();
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(NotConvertibleException.class));
        }
        
        try {
            sut.asInt();
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(NotConvertibleException.class));
        }
        
        try {
            sut.asDouble();
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(NotConvertibleException.class));
        }
        
        try {
            sut.asLong();
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(NotConvertibleException.class));
        }
        
    }
    

}
