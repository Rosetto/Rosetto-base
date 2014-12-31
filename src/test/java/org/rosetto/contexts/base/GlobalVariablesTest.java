package org.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.rosetto.contexts.base.Rosetto;
import org.rosetto.contexts.base.GlobalVariables;
import org.rosetto.models.base.elements.RosettoValue;
import org.rosetto.models.system.NameSpace;
import org.rosetto.utils.base.Values;

public class GlobalVariablesTest {
    
    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
    }
    
    @Test
    public void getNameSpaceTest() throws Exception {
        GlobalVariables sut = new GlobalVariables();
        
        //存在しない名前空間はgetの際に生成される
        assertThat(sut.containsNameSpace("foo"), is(false));
        NameSpace got = sut.getNameSpace("foo");
        assertThat(got, is(notNullValue()));
        assertThat(sut.containsNameSpace("foo"), is(true));
        assertThat(sut.getNameSpace("foo"), is(got));
        
        try {
            sut.getNameSpace("");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        try {
            sut.getNameSpace(null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void getValueTest() throws Exception {
        GlobalVariables sut = new GlobalVariables();
        sut.define("foo", Values.create(10.0));
        assertThat(sut.get("foo").asDouble(), is(10.0));
        
        sut.define("foo.bar", Values.create(true));
        assertThat(sut.get("foo.bar").asBool(), is(true));
        assertThat(sut.get("foo", "bar").asBool(), is(true));
        
        //存在しないキーはValues.NULL
        assertThat(sut.get("notfoundkey"), is((RosettoValue)Values.NULL));
        assertThat(sut.get("not.found.pkg.value"), is((RosettoValue)Values.NULL));
    }
    
    @Test
    public void definceValueTest() throws Exception {
        GlobalVariables sut = new GlobalVariables();
        sut.define("foo.bar.baz", Values.create(50L));
        assertThat(sut.get("foo.bar.baz").asLong(), is(50L));
        
        try {
            sut.define(null, Values.create("foo"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        try {
            sut.define("bar", null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        try {
            sut.define("", Values.create("baz"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        
    }
    
    @Test
    public void getEmptyKeyTest() throws Exception {
        GlobalVariables sut = new GlobalVariables();
        
        try {
            sut.get(null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        try {
            sut.get("");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        try {
            sut.get("dotend.");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        try {
            sut.get(null, "hoge");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        try {
            sut.get("", "fuga");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        try {
            sut.get("foo", "");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        try {
            sut.get("bar", null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    

}
