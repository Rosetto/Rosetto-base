package info.rosetto.models.blocks;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import info.rosetto.models.base.blocks.ScriptBlock;

import org.junit.Test;

public class ScriptBlockTest {

    @Test
    public void 簡易重複テスト() {
        ScriptBlock sut = new ScriptBlock("");
        for(int i=0; i<10000; i++) {
            assertThat(sut.getUid(), not(new ScriptBlock("").getUid()));
        }
    }

}
