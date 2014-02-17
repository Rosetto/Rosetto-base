package info.rosetto.models.state.parser;

import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.base.values.ActionCall;

import java.util.List;

public interface Parser {
    
    public Scenario parseScript(String script);
    
    public Scenario parseScript(List<String> scriptLines);
    
    public ActionCall parseActionCall(String actionCall);
    
    public ArgumentSyntax getArgumentSyntax();
    
}
