package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.functions.base.BaseFunctions;
import info.rosetto.models.base.function.RosettoFunction;

import org.junit.Test;

public class WholeSpaceTest {

    @Test
    public void getAndSetCurrentNameSpaceTest() throws Exception {
        VariableContext sut = new VariableContext();
        
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
    public void getNameSpaceTest() throws Exception {
        VariableContext sut = new VariableContext();
        //初期状態ではstoryのみ
        assertThat(sut.getCreatedNameSpaceCount(), is(1));
        //storyをgetしても変化なし
        assertThat(sut.getNameSpace("story").getName(), is("story"));
        assertThat(sut.getCreatedNameSpaceCount(), is(1));
        //fooをgetすると生成されるのでcountは2
        assertThat(sut.getNameSpace("foo.bar").getName(), is("foo.bar"));
        assertThat(sut.getCreatedNameSpaceCount(), is(2));
        
        try {
            sut.getNameSpace("");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        assertThat(sut.getCreatedNameSpaceCount(), is(2));
        
        try {
            sut.getNameSpace(null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        assertThat(sut.getCreatedNameSpaceCount(), is(2));
    }
    

}
