package org.rosetto;

import java.util.List;

import org.rosetto.models.base.elements.RosettoValue;
import org.rosetto.models.base.elements.values.ScriptValue;
import org.rosetto.models.base.scenario.Scenario;
import org.rosetto.models.base.scenario.Unit;
import org.rosetto.models.system.Parser;

public class BaseTestUtils {
    
    
    public static Parser createDummyParser() {
        return new Parser() {
            @Override
            public Scenario parseScript(String script) {
                return new Scenario(new Unit("TestParser"));
            }
            @Override
            public Scenario parseScript(List<String> scriptLines) {
                return null;
            }
            @Override
            public RosettoValue parseElement(String actionCall) {
                return null;
            }
            @Override
            public List<String> splitElements(String elements) {
                return null;
            }
            @Override
            public Scenario parseScript(ScriptValue script) {
                return null;
            }
        };
    }

}
