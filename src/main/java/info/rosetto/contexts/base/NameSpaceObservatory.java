package info.rosetto.contexts.base;

import info.rosetto.models.base.values.RosettoValue;

import java.util.Map.Entry;
import java.util.Set;

import org.frows.observatories.ObjectObservatory;

public class NameSpaceObservatory extends ObjectObservatory<String, VariableObserver> 
    implements VariableObserver {
    
    private static final NameSpaceObservatory instance = new NameSpaceObservatory();

    @Override
    public void valueChanged(String nameSpace, String variableName, RosettoValue newValue) {
        for(Entry<String, Set<VariableObserver>> os : getObservers().entrySet()) {
            if(nameSpace.startsWith(os.getKey()))
                for(VariableObserver o : os.getValue()) {
                    o.valueChanged(nameSpace, variableName, newValue);
                }
        }
        
    }

    static NameSpaceObservatory getInstance() {
        return instance;
    }
}
