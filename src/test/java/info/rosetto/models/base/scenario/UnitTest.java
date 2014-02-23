package info.rosetto.models.base.scenario;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.functions.base.BaseFunctions;
import info.rosetto.models.base.elements.values.ActionCall;

import org.junit.Before;
import org.junit.Test;

public class UnitTest {
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
    }
    
    @Test
    public void getContentでUnitが保持する文字列が取得できる() throws Exception {
        String expected = "テスト文字列";
        Unit sut = new Unit(expected);
        assertThat(expected, is(sut.getContent()));
    }
    
    @Test
    public void textをnullで初期化した場合getContentは空文字を返す() throws Exception {
        Unit sut = new Unit(null);
        assertThat(sut.getContent(), is(""));
    }
    
    @Test
    public void getActionで生成時に与えたアクションが取得できる() throws Exception {
        ActionCall expected = new ActionCall(BaseFunctions.pass.getName());
        Unit sut = new Unit("test", expected);
        assertThat(sut.getAction(), is(expected));
    }

    @Test
    public void actionをnullで初期化した場合getActionはEMPTYを返す() throws Exception {
        Unit sut = new Unit(null, null);
        assertThat(sut.getAction(), is(ActionCall.EMPTY));
    }
    
    @Test
    public void toStringで要素を角括弧で囲んだ文字列表現が返る() throws Exception {
        String expected = "[text=test, action=" + BaseFunctions.pass.getName() + "]";
        Unit sut = new Unit("test", new ActionCall(BaseFunctions.pass.getName()));
        assertThat(sut.toString(), is(expected));
    }
}
