package info.rosetto.observers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoValue;

import org.junit.Before;
import org.junit.Test;

public class NameSpaceObservatoryTest {

    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
        Observatories.getVariable().clear();
    }
    
    @Test
    public void notifyTest() throws Exception {
        final StringBuilder sb = new StringBuilder();
        Observatories.getNameSpace().addObserver("hoge", 
                new VariableObserver() {
            @Override
            public void valueChanged(String nameSpace, String variableName,
                    RosettoValue newValue) {
                sb.append(nameSpace).append(".").append(variableName).append(":")
                .append(newValue.asString(""));
            }
        });
        Contexts.define("hoge.foo", "bar");
        assertThat(sb.toString(), is("hoge.foo:bar"));
        
        sb.delete(0, sb.length());
        Contexts.define("this.is.test", 12345);
        assertThat(sb.toString(), is(""));
        
        Contexts.define("hoge.characters.hoge.name", "fuga");
        assertThat(sb.toString(), is("hoge.characters.hoge.name:fuga"));
    }

}
