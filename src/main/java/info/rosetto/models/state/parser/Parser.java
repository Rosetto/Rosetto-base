package info.rosetto.models.state.parser;

import info.rosetto.models.base.scenario.Scenario;

import java.util.List;

public interface Parser {
    
    public Scenario parseScript(String script);
    
    public Scenario parseScript(List<String> scriptLines);
    
    public ArgumentSyntax getArgumentSyntax();
    
}
