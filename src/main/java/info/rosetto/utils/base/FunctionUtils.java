package info.rosetto.utils.base;

import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.base.elements.values.ActionCall;
import info.rosetto.models.system.Scope;

public class FunctionUtils {

    
    /**
     * 引数に与えたスコープを用いて引数に与えたアクションまたはアクションのリストを順に実行する.
     * @param scope
     * @param actions
     * @return
     */
    public static RosettoValue doActions(Scope scope, RosettoValue actions) {
        RosettoValue result = Values.NULL;
        while(true) {
            RosettoValue item = actions.first();
            if(item.getType() == ValueType.ACTION_CALL) {
                ActionCall ac = (ActionCall) item;
                result = ac.evaluate(scope);
            } else {
                result = item;
            }
            if(actions.size() == 1) return result;
            actions = actions.rest();
        }
    }
}
