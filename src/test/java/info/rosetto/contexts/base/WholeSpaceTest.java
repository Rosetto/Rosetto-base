package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

public class WholeSpaceTest {
    
    @Test
    public void getNameSpaceTest() throws Exception {
        VariableContext sut = new VariableContext();
        //初期状態では空
        assertThat(sut.getCreatedNameSpaceCount(), is(0));
        //fooをgetすると生成されるのでcountは1
        assertThat(sut.getNameSpace("foo.bar").getName(), is("foo.bar"));
        assertThat(sut.getCreatedNameSpaceCount(), is(1));
        
        try {
            sut.getNameSpace("");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        assertThat(sut.getCreatedNameSpaceCount(), is(1));
        
        try {
            sut.getNameSpace(null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        assertThat(sut.getCreatedNameSpaceCount(), is(1));
    }
    

}
