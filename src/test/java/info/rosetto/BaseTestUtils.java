package info.rosetto;

import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.values.ScriptValue;
import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.base.scenario.Unit;
import info.rosetto.models.system.Parser;

import java.util.List;

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
