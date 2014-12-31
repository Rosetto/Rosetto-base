package org.ocsoft.rosetto.models.base.scenario;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.ocsoft.rosetto.contexts.base.Rosetto;
import org.ocsoft.rosetto.functions.base.BaseFunctions;
import org.ocsoft.rosetto.models.base.elements.values.ActionCall;
import org.ocsoft.rosetto.models.base.scenario.Unit;

public class UnitTest {
    
    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
    }
    
    @Test
    public void getContentでUnitが保持する文字列が取得できる() throws Exception {
        String expected = "テスト文字列";
        Unit sut = new Unit(expected);
        assertThat(expected, is(sut.getText()));
    }
    
    @Test
    public void textをnullで初期化した場合getContentは空文字を返す() throws Exception {
        Unit sut = new Unit(null);
        assertThat(sut.getText(), is(""));
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
