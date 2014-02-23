package info.rosetto.models.blocks;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import info.rosetto.models.base.blocks.Block;

import org.junit.Test;

public class BlockTest {

    @Test
    @SuppressWarnings("serial")
    public void インスタンス化テスト() throws Exception {
        Block sut = new Block("test") {}; 
        assertThat(sut.getContent(), is("test"));
        try {
            new Block(null){};
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

}
