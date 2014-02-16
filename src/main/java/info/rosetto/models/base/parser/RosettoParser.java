package info.rosetto.models.base.parser;

import info.rosetto.models.base.scenario.Scenario;

public interface RosettoParser {
    
    public Scenario parseScript(String script);
    
    public ArgumentSyntax getArgumentSyntax();
    
}
