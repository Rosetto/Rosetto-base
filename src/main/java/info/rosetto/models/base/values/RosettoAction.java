package info.rosetto.models.base.values;

import info.rosetto.models.base.function.RosettoArguments;

/**
 * 関数やマクロなど、返り値を伴う実行が可能なRosettoValueを示すインタフェース.
 * @author tohhy
 */
public interface RosettoAction extends RosettoValue {
    
    public String getName();
    
    /**
     * このアクションを引数なしで実行する.
     * @return 実行した結果の返り値
     */
    public RosettoValue execute();
    
    /**
     * このアクションを実行する.
     * @param args 実行時引数
     * @return 実行した結果の返り値
     */
    public RosettoValue execute(RosettoArguments args);

}
