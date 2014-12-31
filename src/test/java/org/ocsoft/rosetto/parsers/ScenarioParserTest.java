package org.ocsoft.rosetto.parsers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.frows.lilex.token.Token;
import org.junit.Before;
import org.junit.Test;
import org.ocsoft.rosetto.contexts.base.Rosetto;
import org.ocsoft.rosetto.models.base.elements.values.ActionCall;
import org.ocsoft.rosetto.models.base.scenario.Scenario;
import org.ocsoft.rosetto.models.base.scenario.Unit;
import org.ocsoft.rosetto.parsers.ScenarioParser;
import org.ocsoft.rosetto.parsers.rosetto.RosettoNormalizer;

public class ScenarioParserTest {
    private ScenarioParser sut;
    
    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
        this.sut = new ScenarioParser(new RosettoNormalizer());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void nullのノーマライザで初期化するとエラー() throws Exception {
        new ScenarioParser(null) {};
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void nullのtagParserで初期化するとエラー() throws Exception {
        new ScenarioParser(new RosettoNormalizer(), null) {};
    }
    
    @Test
    public void loadStringでシナリオを読み込める() throws Exception {
        Scenario s = sut.parseScript("テスト。[br]二行目。\n\n\n三行目\n\n\n");
        //ページの取り出し
        int length = s.getLength();
        assertThat(length, is(3));
    }

    @Test
    public void createUnitでユニットを生成できる() throws Exception {
        Unit s1 = sut.createUnit("吾輩は猫である。[hoge fuga=piyo]");
        assertThat(s1.getText(), is("吾輩は猫である。"));
        assertThat(s1.getAction().getActionName(), is("hoge"));
        assertThat(s1.getAction().getArgs().get("fuga").asString(), is("piyo"));
        Unit s2 = sut.createUnit("テスト。");
        assertThat(s2.getText(), is("テスト。"));
        assertThat(s2.getAction(), is(ActionCall.EMPTY));
    }
    
    @Test
    public void ラベルを含むシナリオ() throws Exception {
         Scenario s = sut.parseScript("foo[br]bar[br][label a]baz[br]hoge[label b][br] fuga");
         assertThat(s.getUnits().size(), is(7));
         assertThat(s.getLabels().size(), is(2));
         assertThat(s.getLabelAt(2), is(nullValue()));
         assertThat(s.getLabelAt(3).getName(), is("a"));
         assertThat(s.getLabelAt(5).getName(), is("b"));
    }
    
    @Test
    public void createUnitListで空行は無視() throws Exception {
        List<String> list = Arrays.asList(
                new String[]{"test[br]", "", null, "[if exp=\"test==0\"][end]", "test[p]"});
        List<? extends Token> result = sut.tokenize(list);
        assertThat(result.size(), is(4));
    }


}
