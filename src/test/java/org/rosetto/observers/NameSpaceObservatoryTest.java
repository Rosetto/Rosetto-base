package org.rosetto.observers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.rosetto.contexts.base.Rosetto;
import org.rosetto.models.base.elements.RosettoValue;
import org.rosetto.observers.RosettoObservatories;
import org.rosetto.observers.VariableObserver;

public class NameSpaceObservatoryTest {

    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
        RosettoObservatories.getVariable().clear();
    }
    
    @Test
    public void notifyTest() throws Exception {
        final StringBuilder sb = new StringBuilder();
        RosettoObservatories.getNameSpace().addObserver("hoge", 
                new VariableObserver() {
            @Override
            public void valueChanged(String nameSpace, String variableName,
                    RosettoValue newValue) {
                sb.append(nameSpace).append(".").append(variableName).append(":")
                .append(newValue.asString(""));
            }
        });
        Rosetto.define("hoge.foo", "bar");
        assertThat(sb.toString(), is("hoge.foo:bar"));
        
        sb.delete(0, sb.length());
        Rosetto.define("this.is.test", 12345);
        assertThat(sb.toString(), is(""));
        
        Rosetto.define("hoge.characters.hoge.name", "fuga");
        assertThat(sb.toString(), is("hoge.characters.hoge.name:fuga"));
    }

}
