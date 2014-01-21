package info.rosetto.contexts.base;

import info.rosetto.models.base.parser.RosettoEvaluator;
import info.rosetto.models.base.values.RosettoValue;

public class Contexts {
    private static final Contexts instance = new Contexts();
    
    private RosettoEvaluator evaluator;
    private WholeSpace wholeSpace;
    private NameSpace currentNameSpace;
    private boolean isInitialized = false;
    
    private Contexts() {}
    
    public static void initialize() {
        if(instance.isInitialized)
            throw new IllegalStateException("Contexts already initialized");
        
        instance.wholeSpace = new WholeSpace();
        instance.currentNameSpace = instance.wholeSpace.create("story");
        
        instance.isInitialized = true;
    }
    
    public static void dispose() {
        instance.evaluator = null;
        instance.wholeSpace = null;
        instance.currentNameSpace = null;
        instance.isInitialized = false;
    }
    
    /**
     * 現在のコンテキストでアクティブな名前空間から指定した変数に保存されている値を取得する.
     * @param key
     * @return
     */
    public static RosettoValue get(String key) {
        return instance.currentNameSpace.get(key);
    }
    
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
                instance.wholeSpace.getNameSpace(name) : instance.wholeSpace.create(name);
    }

    public static boolean isInitialized() {
        return instance.isInitialized;
    }
    
}
