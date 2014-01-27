package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.models.base.parser.ArgumentSyntax;
import info.rosetto.models.base.parser.RosettoParser;
import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.base.scenario.Unit;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.utils.base.Values;

import java.io.File;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class ContextsTest {
    
    @Before
    public void setUp() {
        //Contextsを削除した状態からテスト.
        if(Contexts.isInitialized()) Contexts.dispose();
    }

    
    @Test
    public void InitializeAndDisposeTest() throws Exception {
        //setupで破棄済み
        assertThat(Contexts.isInitialized(), is(false));
        
        //破棄されている状態で各種操作するとエラー
        try {
            Contexts.get("foo");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalStateException.class));
        }
        
        //初期状態ではinitialize可能
        Contexts.initialize();
        assertThat(Contexts.isInitialized(), is(true));
        
        //既にinitializeされていると例外
        try {
            Contexts.initialize();
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalStateException.class));
        }
        assertThat(Contexts.isInitialized(), is(true));
        
        //破棄するとinitialize可能
        Contexts.dispose();
        assertThat(Contexts.isInitialized(), is(false));
        Contexts.initialize();
    }
    
    @Test
    public void getAndPutValueTest() throws Exception {
        Contexts.initialize();
        
        //存在しない変数の値はnull
        assertThat(Contexts.get("foobar"), is(nullValue()));
        
        //値をセットする
        Contexts.put("foobar", Values.create("baz"));
        assertThat(Contexts.get("foobar").asString(), is("baz"));
        
        //値を上書きする
        Contexts.put("foobar", true);
        assertThat(Contexts.get("foobar").asBool(), is(true));
        
        //nullキーでputするとエラー
        try {
            Contexts.put(null, Values.create("hoge"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        //nullキーでgetするとnull
        assertThat(Contexts.get(null), is(nullValue()));
        
        //空文字キーでputするとエラー
        try {
            Contexts.put("", Values.create("hoge"));
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        //空文字のキーは常に存在しないのでnull
        assertThat(Contexts.get(""), is(nullValue()));
        
        //nullvalueでputするとエラー
        try {
            Contexts.put("fuga", (RosettoValue)null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void getAndPutAbsoluteValueTest() throws Exception {
        Contexts.initialize();
        
        //名前空間指定付きで登録、この段階では絶対参照しかできない
        Contexts.put("org.example.foo", "bar");
        assertThat(Contexts.get("org.example.foo").asString(), is("bar"));
        assertThat(Contexts.get("foo"), is(nullValue()));
        
        //名前空間がcurrentになれば見えるようになる
        Contexts.setNameSpaceAsCurrent("org.example");
        assertThat(Contexts.get("org.example.foo").asString(), is("bar"));
        assertThat(Contexts.get("foo").asString(), is("bar"));
        
        //dotで終わる変数名は不可
        try {
            Contexts.put("org.example.foobar.", "baz");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void getAndSetParserTest() throws Exception {
        Contexts.initialize();
        
        Contexts.setParser(new RosettoParser() {
            @Override
            public Scenario parseString(String script) {
                return new Scenario(new Unit("TestParser"));
            }
            @Override
            public Scenario parseFile(URL url) {
                return null;
            }
            @Override
            public ArgumentSyntax getArgumentSyntax() {
                return null;
            }
            @Override
            public Scenario parseFile(File file) {
                // TODO Auto-generated method stub
                return null;
            }
        });
        assertThat(Contexts.getParser().parseString("foobar")
                .getUnitAt(0).getContent(), is("TestParser"));
        
    }
    
    
    @Test
    public void getAndSetNameSpaceTest() throws Exception {
        Contexts.initialize();
        
        //デフォルトの名前空間は"story"
        assertThat(Contexts.getCurrentNameSpace().getName(), is("story"));
        Contexts.put("namespacenum", 1);
        assertThat(Contexts.get("namespacenum").asInt(), is(1));
        
        //変更できる
        Contexts.setNameSpaceAsCurrent("foobar");
        assertThat(Contexts.getCurrentNameSpace().getName(), is("foobar"));
        Contexts.put("namespacenum", 2.0);
        assertThat(Contexts.get("namespacenum").asInt(), is(2));
        
        //戻せる
        Contexts.setNameSpaceAsCurrent("story");
        assertThat(Contexts.get("namespacenum").asInt(), is(1));
        
        //nullセットでエラー
        try {
            Contexts.setNameSpaceAsCurrent(null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        //空文字セットでエラー
        try {
            Contexts.setNameSpaceAsCurrent("");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void getAndSetWholeSpaceTest() throws Exception {
        Contexts.initialize();
        
        //デフォルトの全体名前空間は"story"だけを持つ
        assertThat(Contexts.getWholeSpace().getCreatedNameSpaceCount(), is(1));
        assertThat(Contexts.getWholeSpace().contains("story"), is(true));
        assertThat(Contexts.getCurrentNameSpace().getName(), is("story"));
        
        //変更できる
        WholeSpace ws = new WholeSpace();
        ws.createNameSpace("foo");
        ws.createNameSpace("bar");
        ws.setCurrentNameSpace("foo");
        Contexts.setWholeSpace(ws);
        assertThat(Contexts.getWholeSpace().getCreatedNameSpaceCount(), is(3));
        assertThat(Contexts.getWholeSpace().contains("story"), is(true));
        assertThat(Contexts.getWholeSpace().contains("foo"), is(true));
        assertThat(Contexts.getWholeSpace().contains("bar"), is(true));
        assertThat(Contexts.getCurrentNameSpace().getName(), is("foo"));
        
        //nullセットでエラー
        try {
            Contexts.setWholeSpace(null);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

}