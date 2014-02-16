package info.rosetto.contexts.observers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.contexts.observers.VariableObservatory;
import info.rosetto.contexts.observers.VariableObserver;
import info.rosetto.models.base.values.RosettoValue;

import org.junit.Before;
import org.junit.Test;

public class VariableObservatoryTest {
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
        VariableObservatory.getInstance().clear();
    }

    @Test
    public void notifyTest() throws Exception {
        final StringBuilder sb = new StringBuilder();
        VariableObservatory.getInstance().addObserver(new VariableObserver() {
            @Override
            public void valueChanged(String nameSpace, String variableName,
                    RosettoValue newValue) {
                sb.append(nameSpace).append(".").append(variableName).append(":")
                .append(newValue.asString(""));
            }
        });
        Contexts.define("foo", "bar");
        assertThat(sb.toString(), is(".foo:bar"));
        
        sb.delete(0, sb.length());
        Contexts.define("this.is.test", 12345);
        assertThat(sb.toString(), is("this.is.test:12345"));
    }
    
    @Test
    public void addNameSpaceObserverTest() throws Exception {
        final StringBuilder sb = new StringBuilder();
        VariableObservatory.getInstance().addNameSpaceObserver("hoge", 
                new VariableObserver() {
            @Override
            public void valueChanged(String nameSpace, String variableName,
                    RosettoValue newValue) {
                sb.append(nameSpace).append(".").append(variableName).append(":")
                .append(newValue.asString(""));
            }
        });
        //指定している名前空間には反応
        Contexts.define("hoge.foo", "bar");
        assertThat(sb.toString(), is("hoge.foo:bar"));
        
        //指定外の名前空間には反応しない
        sb.delete(0, sb.length());
        Contexts.define("this.is.test", 12345);
        assertThat(sb.toString(), is(""));
    }

}
