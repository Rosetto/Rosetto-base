package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import info.rosetto.functions.base.BaseFunctions;
import info.rosetto.models.base.function.RosettoFunction;

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
            sut.setCurrentNameSpace((String)null);
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
    
    @Test
    public void baseFunctionTest() throws Exception {
        WholeSpace sut = new WholeSpace();
        assertThat((RosettoFunction)sut.getCurrentNameSpace().get("rosetto.base.pass"), is(BaseFunctions.pass));
        
    }

}
