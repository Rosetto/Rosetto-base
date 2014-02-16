package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

public class VariableContextTest {
    
    @Test
    public void getNameSpaceTest() throws Exception {
        VariableContext sut = new VariableContext();
        
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
    

}
