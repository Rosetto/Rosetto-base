package info.rosetto.models.base.parser;

import info.rosetto.models.base.values.ActionCall;
import info.rosetto.models.base.values.RosettoValue;

public interface RosettoEvaluator {
    
    /**
     * 指定された関数呼び出しを評価し、結果を返す.
     * @param fc 評価する関数呼び出し
     * @return 評価結果
     */
    public RosettoValue evaluate(ActionCall fc);

}
