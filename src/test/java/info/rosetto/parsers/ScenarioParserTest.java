package info.rosetto.parsers;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.base.scenario.Unit;
import info.rosetto.models.base.values.ActionCall;
import info.rosetto.parsers.rosetto.RosettoNormalizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import org.frows.lilex.token.Token;
import org.junit.Before;
import org.junit.Test;

public class ScenarioParserTest {
    private ScenarioParser sut;
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
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
        ParserState ps = new ParserState();
        Unit s1 = sut.createUnit("吾輩は猫である。[hoge fuga=piyo]", ps);
        assertThat(s1.getContent(), is("吾輩は猫である。"));
        assertThat(s1.getAction().getFunctionName(), is("hoge"));
        assertThat(s1.getAction().getArgs().get("fuga").asString(), is("piyo"));
        Unit s2 = sut.createUnit("テスト。", ps);
        assertThat(s2.getContent(), is("テスト。"));
        assertThat(s2.getAction(), is(ActionCall.EMPTY));
    }
    
    @Test
    public void createUnitListで空行は無視() throws Exception {
        List<String> list = Arrays.asList(
                new String[]{"test[br]", "", null, "[if exp=\"test==0\"][end]", "test[p]"});
        List<Token> result = sut.tokenize(list);
        assertThat(result.size(), is(4));
    }


}
