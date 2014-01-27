package info.rosetto.contexts.base;

import info.rosetto.models.base.values.RosettoValue;

import org.frows.observatories.Observatory;

/**
 * Rosetto中の全変数の変更を監視する.
 * @author tohhy
 */
public class VariableObservatory extends Observatory<VariableObserver> 
    implements VariableObserver {
    
    /**
     * シングルトンインスタンス.
     */
    private static final VariableObservatory instance = new VariableObservatory();
    
    public static VariableObservatory getInstance() {
        return instance;
    }
    
    /**
     * 特定の名前空間の変数変更のみを監視するオブザーバを登録する.
     * @param nameSpace
     * @param observer
     */
    public void addNameSpaceObserver(String nameSpace, VariableObserver observer) {
        NameSpaceObservatory.getInstance().addObserver(nameSpace, observer);
    }
    
    
    @Override
    public void valueChanged(String nameSpace, String variableName,
            RosettoValue newValue) {
        for(VariableObserver o : getObservers()) {
            o.valueChanged(nameSpace, variableName, newValue);
        }
        NameSpaceObservatory.getInstance().valueChanged(nameSpace, variableName, newValue);
    }

}