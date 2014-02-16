package info.rosetto.contexts.observers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.contexts.observers.NameSpaceObservatory;
import info.rosetto.contexts.observers.VariableObservatory;
import info.rosetto.contexts.observers.VariableObserver;
import info.rosetto.models.base.values.RosettoValue;

import org.junit.Before;
import org.junit.Test;

public class NameSpaceObservatoryTest {

    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
        VariableObservatory.getInstance().clear();
    }
    
    @Test
    public void notifyTest() throws Exception {
        final StringBuilder sb = new StringBuilder();
        NameSpaceObservatory.getInstance().addObserver("hoge", 
                new VariableObserver() {
            @Override
            public void valueChanged(String nameSpace, String variableName,
                    RosettoValue newValue) {
                sb.append(nameSpace).append(".").append(variableName).append(":")
                .append(newValue.asString(""));
            }
        });
        Contexts.set("hoge.foo", "bar");
        assertThat(sb.toString(), is("hoge.foo:bar"));
        
        sb.delete(0, sb.length());
        Contexts.set("this.is.test", 12345);
        assertThat(sb.toString(), is(""));
        
        Contexts.set("hoge.characters.hoge.name", "fuga");
        assertThat(sb.toString(), is("hoge.characters.hoge.name:fuga"));
    }

}
