package org.ocsoft.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.ocsoft.rosetto.contexts.base.SystemContext;
import org.ocsoft.rosetto.models.base.scenario.Scenario;
import org.ocsoft.rosetto.models.system.Parser;
import org.ocsoft.rosetto.models.system.ScenarioPlayer;
import org.ocsoft.rosetto.models.system.Scope;
import org.ocsoft.rosetto.parsers.AbstractNormalizer;
import org.ocsoft.rosetto.parsers.ScenarioParser;
import org.ocsoft.rosetto.parsers.rosetto.RosettoParser;

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
