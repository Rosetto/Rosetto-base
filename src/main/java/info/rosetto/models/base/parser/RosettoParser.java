package info.rosetto.models.base.parser;

import info.rosetto.models.base.scenario.Scenario;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

public interface RosettoParser {
    
    public Scenario parseString(String script);
    
    public Scenario parseFile(URL url);
    
    public Scenario parseFile(File file) throws FileNotFoundException;
    
    public ArgumentSyntax getArgumentSyntax();
    
}
