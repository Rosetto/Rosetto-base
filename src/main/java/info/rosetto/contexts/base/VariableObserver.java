package info.rosetto.contexts.base;

import info.rosetto.models.base.values.RosettoValue;

/**
 * 全ての変数の変更を監視するオブザーバー.
 * @author tohhy
 */
public interface VariableObserver {
    
    public void valueChanged(String nameSpace, String variableName, RosettoValue newValue);

}
