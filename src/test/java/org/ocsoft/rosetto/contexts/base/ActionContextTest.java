package org.ocsoft.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.ocsoft.rosetto.contexts.base.ActionContext;
import org.ocsoft.rosetto.contexts.base.Rosetto;
import org.ocsoft.rosetto.models.base.elements.RosettoAction;
import org.ocsoft.rosetto.models.base.elements.RosettoValue;
import org.ocsoft.rosetto.models.base.elements.values.ListValue;
import org.ocsoft.rosetto.models.base.elements.values.RosettoFunction;
import org.ocsoft.rosetto.models.system.FunctionPackage;
import org.ocsoft.rosetto.models.system.Scope;
import org.ocsoft.rosetto.utils.base.Values;


@SuppressWarnings("serial")
public class ActionContextTest {
    
    
    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
    }
    
    @Test
    public void getAndDefineTest() throws Exception {
        ActionContext sut = new ActionContext();
        assertThat(sut.get("foo"), is((RosettoAction)Values.NULL));
        assertThat(sut.get("foo.bar"), is((RosettoAction)Values.NULL));
        assertThat(sut.containsNameSpace("foo"), is(false));
        
        RosettoFunction f1 = new RosettoFunction("foo") {
            @Override
            protected RosettoValue run(Scope functionScope, ListValue args) {
                return null;
            }
        };
        sut.defineAction(f1.getName(), f1);
        assertThat(sut.get("foo"), is((RosettoAction)f1));
        assertThat(sut.containsNameSpace("foo"), is(false));
        
        RosettoFunction f2 = new RosettoFunction("bar") {
            @Override
            protected RosettoValue run(Scope functionScope, ListValue args) {
                return null;
            }
        };
        sut.defineAction(f2.getName(), f2, "foo");
        assertThat(sut.get("foo.bar"), is((RosettoAction)f2));
        assertThat(sut.containsNameSpace("foo"), is(true));
    }
    
    @Test
    public void importAndUsePackageTest() throws Exception {
        ActionContext sut = new ActionContext();
        RosettoFunction f = new RosettoFunction("foo") {
            @Override
            protected RosettoValue run(Scope scope, ListValue rawArgs) {
                return null;
            }
        };
        FunctionPackage testPkg = new FunctionPackage(f);
        //この段階では読めない
        assertThat(sut.get("foo"), is((RosettoValue)Values.NULL));
        assertThat(sut.get("testpkg.foo"), is((RosettoValue)Values.NULL));
        assertThat(sut.containsNameSpace("testpkg"), is(false));
        //importするとfullなら読める
        sut.importPackage(testPkg, "testpkg");
        assertThat(sut.get("foo"), is((RosettoValue)Values.NULL));
        assertThat(sut.get("testpkg.foo"), is((RosettoValue)f));
        assertThat(sut.containsNameSpace("testpkg"), is(true));
        //useすると直接読める
        sut.usePackage("testpkg");
        assertThat(sut.get("foo"), is((RosettoValue)f));
        assertThat(sut.get("testpkg.foo"), is((RosettoValue)f));
        assertThat(sut.containsNameSpace("testpkg"), is(true));
    }

}
