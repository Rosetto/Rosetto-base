package info.rosetto.models.blocks;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import info.rosetto.models.base.blocks.IfBranch;

import org.junit.Test;

public class IfBranchTest {

    @Test
    public void インスタンス化テスト() throws Exception {
        IfBranch b = new IfBranch("i == 0", "foo");
        assertThat(b.getExp(), is("i == 0"));
        assertThat(b.getContent(), is("foo"));
        
        try {
            new IfBranch("i == 0", null); 
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        try {
            new IfBranch(null, "foo"); 
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

}
