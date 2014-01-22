package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import info.rosetto.utils.base.Values;

import org.junit.Before;
import org.junit.Test;

public class NameSpaceTest {
    @Before
    public void setUp() {
        //Contextsを初期化した状態からテスト.
        if(Contexts.isInitialized()) Contexts.dispose();
        Contexts.initialize();
    }
    
    @Test
    public void constructorTest() throws Exception {
        NameSpace sut1 = new NameSpace("foo");
        assertThat(sut1.getName(), is("foo"));
        
        //nullで初期化するとエラー
        try {
            new NameSpace(null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        //空文字で初期化するとエラー
        try {
            new NameSpace("");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void getAndPutValueTest() throws Exception {
        NameSpace sut = new NameSpace("foo");
        
        sut.put("bar", Values.create("baz"));
        assertThat(sut.get("bar").asString(), is("baz"));
        
        //存在しないキーはnull
        assertThat(sut.get("some not found key"), is(nullValue()));
        
        //nullキーでputするとエラー
        try {
            sut.put(null, Values.create("foo"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        //nullキーでgetするとnull
        assertThat(sut.get(null), is(nullValue()));
        //空文字でputするとエラー
        try {
            sut.put("", Values.create("bar"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        //空文字でgetするとnull
        assertThat(sut.get(""), is(nullValue()));
        
        //キーにdotを含むとエラー（パッケージ階層と区別するため）
        try {
            sut.put("some.package-like.key", Values.create("foo"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        //null値を与えるとエラー
        try {
            sut.put("baz", null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

}
