package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
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
        NameSpaceObservatory.getInstance().addObserver("story", 
                new VariableObserver() {
            @Override
            public void valueChanged(String nameSpace, String variableName,
                    RosettoValue newValue) {
                sb.append(nameSpace).append(".").append(variableName).append(":")
                .append(newValue.asString());
            }
        });
        Contexts.put("foo", "bar");
        assertThat(sb.toString(), is("story.foo:bar"));
        
        sb.delete(0, sb.length());
        Contexts.put("this.is.test", 12345);
        assertThat(sb.toString(), is(""));
        
        Contexts.put("story.characters.hoge.name", "fuga");
        assertThat(sb.toString(), is("story.characters.hoge.name:fuga"));
    }

}
