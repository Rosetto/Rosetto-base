package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.functions.base.BaseFunctions;
import info.rosetto.functions.base.FunctionalFunctions;
import info.rosetto.models.base.elements.RosettoAction;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.values.ListValue;
import info.rosetto.models.base.function.FunctionPackage;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.system.Scope;
import info.rosetto.utils.base.Values;

import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("serial")
public class ActionContextTest {
    
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
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
        sut.defineAction(f1);
        assertThat(sut.get("foo"), is((RosettoAction)f1));
        assertThat(sut.containsNameSpace("foo"), is(false));
        
        RosettoFunction f2 = new RosettoFunction("bar") {
            @Override
            protected RosettoValue run(Scope functionScope, ListValue args) {
                return null;
            }
        };
        sut.defineAction(f2, "foo");
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
