package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.logging.Level;

import info.rosetto.utils.base.RosettoLogger;
import info.rosetto.utils.base.Values;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NameSpaceTest {
    @Before
    public void setUp() {
        RosettoLogger.setLevel(Level.OFF);
        //Contextsを初期化した状態からテスト.
        if(Contexts.isInitialized()) Contexts.dispose();
        Contexts.initialize();
    }
    
    @After
    public void tearDown() {
        RosettoLogger.resetLevel();
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
        
        //dotで終わる名前空間名で初期化するとエラー
        try {
            new NameSpace("foo.bar.");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        try {
            new NameSpace(".");
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
    
    @Test
    public void requireTest() throws Exception {
        NameSpace sut = new NameSpace("foo");
        //既に値を入れた変数を用意しておく
        
        
        NameSpace include = new NameSpace("org.example");
        include.put("bar", Values.create("baz"));
        include.put("hoge", Values.create("fuga"));
        include.put("piyo", Values.create(100));
        include.seal("piyo");
        
        //require時に指定したパッケージの全変数がputAbsoluteされる
        sut.require(include);
        
        //直接参照はできない
        assertThat(sut.get("bar"), is(nullValue()));
        assertThat(sut.get("hoge"), is(nullValue()));
        assertThat(sut.get("piyo"), is(nullValue()));
        
        //完全名で参照できる
        assertThat(sut.get("org.example.bar").asString(), is("baz"));
        assertThat(sut.get("org.example.hoge").asString(), is("fuga"));
        assertThat(sut.get("org.example.piyo").asString(), is("100"));
        
        //sealされた変数はrequireしてもseal
        assertThat(sut.isSealed("org.example.hoge"), is(false));
        assertThat(sut.isSealed("org.example.piyo"), is(true));
        
        //同名のパッケージで上書きrequireしたケース
        NameSpace include2 = new NameSpace("org.example");
        include2.put("hoge", Values.create(true));
        include2.put("piyo", Values.create(12.345));
        sut.require(include2);
        
        //sealされていればそのまま、されていなければ上書き
        assertThat(sut.get("org.example.hoge").asString(), is("true"));
        assertThat(sut.get("org.example.piyo").asString(), is("100"));
        
    }
    
    @Test
    public void sealTest() throws Exception {
        NameSpace sut = new NameSpace("foo");
        
        sut.put("bar", Values.create("baz"));
        sut.put("hoge", Values.create("fuga"));
        
        //barをsealする
        sut.seal("bar");
        
        //barに代入しても何も起きない
        sut.put("bar", Values.create(100));
        assertThat(sut.get("bar").asString(), is("baz"));
        //hogeには代入できる
        sut.put("hoge", Values.create(1000));
        assertThat(sut.get("hoge").asString(), is("1000"));
        
        //barをunsealする
        sut.unSeal("bar");
        
        //barに代入できるようになる
        sut.put("bar", Values.create(10000));
        assertThat(sut.get("bar").asInt(), is(10000));
    }

}
