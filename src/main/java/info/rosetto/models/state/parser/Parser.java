package info.rosetto.models.state.parser;

import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.base.values.RosettoValue;

import java.util.List;

public interface Parser {
    
    /**
     * スクリプトをパースして解釈後のシナリオオブジェクトを返す.
     * @param script
     * @return
     */
    public Scenario parseScript(String script);
    
    /**
     * スクリプトをパースして解釈後のシナリオオブジェクトを返す.
     * @param scriptLines
     * @return
     */
    public Scenario parseScript(List<String> scriptLines);
    
    /**
     * S式中の単一の要素をパースして解釈後のRosettoValueを返す.
     * @param actionCall
     * @return
     */
    public RosettoValue parseElement(String element);
    
    /**
     * 引数解釈のシンタックスを返す.
     * @return
     */
    public ArgumentSyntax getArgumentSyntax();
    
}
