package info.rosetto.contexts.base;

import info.rosetto.models.base.parser.RosettoEvaluator;

public class Contexts {
    private static final Contexts instance = new Contexts();
    
    private RosettoEvaluator evaluator;
    private WholeSpace wholeSpace;
    private NameSpace currentNameSpace = new NameSpace("story");
    
    private Contexts() {}
    
    public static RosettoEvaluator getEvaluator() {
        return instance.evaluator;
    }
    
    public static void setEvaluator(RosettoEvaluator evaluator) {
        instance.evaluator = evaluator;
    }
    
    public static WholeSpace getWholeSpace() {
        return instance.wholeSpace;
    }
    
    public static void setWholeSpace(WholeSpace wholeSpace) {
        instance.wholeSpace = wholeSpace;
    }

    public static NameSpace getCurrentNameSpace() {
        return instance.currentNameSpace;
    }

    public static void setNameSpaceAsCurrent(String name) {
        instance.currentNameSpace = instance.wholeSpace.contains(name) ? 
                instance.wholeSpace.get(name) : instance.wholeSpace.create(name);;
    }
    
}
