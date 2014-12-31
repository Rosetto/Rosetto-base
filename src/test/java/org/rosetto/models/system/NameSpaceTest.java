package org.rosetto.models.system;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.rosetto.contexts.base.Rosetto;
import org.rosetto.models.base.elements.RosettoValue;
import org.rosetto.models.system.NameSpace;
import org.rosetto.system.exceptions.VariableSealedException;
import org.rosetto.utils.base.Values;

public class NameSpaceTest {
    @Before
    public void setUp() {
        //Contextsを初期化した状態からテスト.
        if(Rosetto.isInitialized()) Rosetto.dispose();
        Rosetto.initialize();
    }
    
    @Test
    public void constructorTest() throws Exception {
        NameSpace sut1 = new NameSpace("foo");
        assertThat(sut1.getName(), is("foo"));
        assertThat(sut1.toString(), is("[namespace:foo variables:{} sealed:[]]"));
        
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
    public void getAndDefineValueTest() throws Exception {
        NameSpace sut = new NameSpace("foo");
        
        sut.define("bar", Values.create("baz"));
        assertThat(sut.get("bar").asString(), is("baz"));
        
        NameSpace sut2 = Rosetto.getNameSpace("testspace");
        NameSpace sut3 = Rosetto.getNameSpace("testspace.hoge");
        
        sut3.define("fuga", Values.create("piyo"));
        assertThat(sut2.get("hoge.fuga").asString(), is("piyo"));
        
        //存在しないキーはnull
        assertThat(sut.get("some not found key"), is((RosettoValue)Values.NULL));
        
        //nullキーでsutするとエラー
        try {
            sut.define(null, Values.create("foo"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        //nullキーでgetするとnull
        assertThat(sut.get(null), is((RosettoValue)Values.NULL));
        //空文字でputするとエラー
        try {
            sut.define("", Values.create("bar"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        //空文字でgetするとnull
        assertThat(sut.get(""), is((RosettoValue)Values.NULL));
        
        //キーにdotを含むとエラー（パッケージ階層と区別するため）
        try {
            sut.define("some.package-like.key", Values.create("foo"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        //null値を与えるとエラー
        try {
            sut.define("baz", null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    
    
    @Test
    public void includeTest() throws Exception {
        NameSpace sut = new NameSpace("foo");
        
        //既に値を入れた変数を用意しておく
        NameSpace include = new NameSpace("org.example");
        include.define("bar", Values.create("baz"));
        include.define("hoge", Values.create("fuga"));
        include.define("piyo", Values.create(100));
        include.seal("piyo");
        
        //include時に指定したパッケージの全変数がputされる
        sut.include(include);
        
        //直接参照可能
        assertThat(sut.get("bar").asString(), is("baz"));
        assertThat(sut.get("hoge").asString(), is("fuga"));
        assertThat(sut.get("piyo").asInt(), is(100));
        
        //sealされた変数はrequireしてもseal
        assertThat(sut.isSealed("hoge"), is(false));
        assertThat(sut.isSealed("piyo"), is(true));
        
        //同名のパッケージで上書きuseしたケース
        NameSpace include2 = new NameSpace("org.example");
        include2.define("hoge", Values.create(true));
        include2.define("piyo", Values.create(12.345));
        sut.include(include2);
        
        //sealされていればそのまま、されていなければ上書き
        assertThat(sut.get("hoge").asString(), is("true"));
        assertThat(sut.get("piyo").asString(), is("100"));
        
        try {
            sut.include(null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void sealTest() throws Exception {
        NameSpace sut = new NameSpace("foo");
        
        sut.define("bar", Values.create("baz"));
        sut.define("hoge", Values.create("fuga"));
        
        //barをsealする
        sut.seal("bar");
        
        //barに代入するとエラー
        try {
            sut.define("bar", Values.create(100));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(VariableSealedException.class));
        }
        assertThat(sut.get("bar").asString(), is("baz"));
        //hogeには代入できる
        sut.define("hoge", Values.create(1000));
        assertThat(sut.get("hoge").asString(), is("1000"));
        
        //barをunsealする
        sut.unSeal("bar");
        
        //barに代入できるようになる
        sut.define("bar", Values.create(10000));
        assertThat(sut.get("bar").asInt(), is(10000));
    }

}
