package org.ocsoft.rosetto.models.system;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.ocsoft.rosetto.models.base.elements.RosettoValue;
import org.ocsoft.rosetto.models.base.elements.values.ListValue;
import org.ocsoft.rosetto.models.base.elements.values.RosettoFunction;
import org.ocsoft.rosetto.models.system.FunctionPackage;
import org.ocsoft.rosetto.models.system.Scope;

@SuppressWarnings("serial")
public class FunctionPackageTest {
    
    @Test
    public void constructorTest() throws Exception {
        FunctionPackage sut1 = new FunctionPackage();
        assertThat(sut1.getFunctionCount(), is(0));
        assertThat(sut1.getFunctionNames().size(), is(0));
        
        RosettoFunction f1 = new RosettoFunction("foo") {
            @Override
            protected RosettoValue run(Scope functionScope, ListValue args) {
                return null;
            }
        };
        FunctionPackage sut2 = new FunctionPackage(f1);
        assertThat(sut2.getFunctionCount(), is(1));
        assertThat(sut2.getFunctionNames().get(0), is("foo"));
        assertThat(sut2.getFunctions().get(0), is(f1));
        assertThat(sut2.toString(), is("FunctionPackage[" + f1.getName() + "]"));
        
        //引数がnullだとエラー
        try {
            new FunctionPackage((RosettoFunction[])null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        //引数にnullを含むとエラー
        try {
            new FunctionPackage(f1, null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    
    

}
