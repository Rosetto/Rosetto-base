package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class WholeSpaceTest {

    @Test
    public void getAndSetCurrentNameSpaceTest() throws Exception {
        WholeSpace sut = new WholeSpace();
        
        //デフォルトの名前空間は'story'
        assertThat(sut.getCurrentNameSpace().getName(), is("story"));
        
        //変更
        sut.setCurrentNameSpace("org.example");
        assertThat(sut.getCurrentNameSpace().getName(), is("org.example"));
        
        //戻す
        sut.setCurrentNameSpace("story");
        assertThat(sut.getCurrentNameSpace().getName(), is("story"));
        
        //null名はエラー
        try {
            sut.setCurrentNameSpace(null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        //空文字はエラー
        try {
            sut.setCurrentNameSpace("");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        //dot終わりの名前空間はエラー
        try {
            sut.setCurrentNameSpace("foo.bar.");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

}
