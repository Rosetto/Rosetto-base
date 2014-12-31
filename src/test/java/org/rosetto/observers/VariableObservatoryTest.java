package org.rosetto.observers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.rosetto.contexts.base.Rosetto;
import org.rosetto.models.base.elements.RosettoValue;
import org.rosetto.observers.RosettoObservatories;
import org.rosetto.observers.VariableObserver;

public class VariableObservatoryTest {
    
    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
    }

    @Test
    public void notifyTest() throws Exception {
        final StringBuilder sb = new StringBuilder();
        RosettoObservatories.getVariable().addObserver(new VariableObserver() {
            @Override
            public void valueChanged(String nameSpace, String variableName,
                    RosettoValue newValue) {
                sb.append(nameSpace).append(".").append(variableName).append(":")
                .append(newValue.asString(""));
            }
        });
        Rosetto.define("foo", "bar");
        assertThat(sb.toString(), is(".foo:bar"));
        
        sb.delete(0, sb.length());
        Rosetto.define("this.is.test", 12345);
        assertThat(sb.toString(), is("this.is.test:12345"));
    }
    
    @Test
    public void addNameSpaceObserverTest() throws Exception {
        final StringBuilder sb = new StringBuilder();
        RosettoObservatories.getVariable().addNameSpaceObserver("hoge", 
                new VariableObserver() {
            @Override
            public void valueChanged(String nameSpace, String variableName,
                    RosettoValue newValue) {
                sb.append(nameSpace).append(".").append(variableName).append(":")
                .append(newValue.asString(""));
            }
        });
        //指定している名前空間には反応
        Rosetto.define("hoge.foo", "bar");
        assertThat(sb.toString(), is("hoge.foo:bar"));
        
        //指定外の名前空間には反応しない
        sb.delete(0, sb.length());
        Rosetto.define("this.is.test", 12345);
        assertThat(sb.toString(), is(""));
    }

}
