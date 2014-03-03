package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.system.Parser;
import info.rosetto.models.system.ScenarioPlayer;
import info.rosetto.models.system.Scope;
import info.rosetto.parsers.AbstractNormalizer;
import info.rosetto.parsers.ScenarioParser;
import info.rosetto.parsers.rosetto.RosettoParser;

import org.junit.Test;

public class SystemContextTest {
    

    
    @Test
    public void getAndSetParserTest() throws Exception {
        SystemContext sut = new SystemContext();
        
        //初期状態ではRosettoParser
        assertThat(sut.getParser(), instanceOf(RosettoParser.class));
        
        ScenarioParser p = new ScenarioParser(new AbstractNormalizer() {
        });
        sut.setParser(p);
        assertThat(sut.getParser(), is((Parser)p));
    }
    
    @Test
    public void getAndSetPlayerTest() throws Exception {
        SystemContext sut = new SystemContext();
        
        //初期状態ではnull
        assertThat(sut.getPlayer(), is(nullValue()));
        
        ScenarioPlayer p = new ScenarioPlayer() {
            @Override
            public void pushScenario(Scenario scenario, Scope playingScope) {}
        };
        sut.setPlayer(p);
        assertThat(sut.getPlayer(), is(p));
    }



}
