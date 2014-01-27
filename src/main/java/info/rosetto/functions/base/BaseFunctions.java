package info.rosetto.functions.base;

import info.rosetto.contexts.base.FunctionPackage;
import info.rosetto.models.base.function.ExpandedArguments;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.utils.base.Values;

/**
 * Rosettoの基本的な関数.
 * @author tohhy
 */
public class BaseFunctions extends FunctionPackage {

    public BaseFunctions() {
        super("rosetto.base", 
                pass, require, use, namespace);
    }
    
    /**
     * 「何もしない」関数.実行すると何もせずにVOIDを返す.
     */
    public static final RosettoFunction pass = new RosettoFunction("pass") {
        private static final long serialVersionUID = 4075950193187972686L;

        @Override
        protected RosettoValue run(ExpandedArguments args) {
            return Values.VOID;
        }
    };
    
    /**
     * 
     */
    public static final RosettoFunction require = new RosettoFunction("require") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(ExpandedArguments args) {
            return Values.VOID;
        }
    };
    
    /**
     * 
     */
    public static final RosettoFunction use = new RosettoFunction("use") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(ExpandedArguments args) {
            return Values.VOID;
        }
    };
    
    /**
     * 
     */
    public static final RosettoFunction namespace = new RosettoFunction("namespace") {
        private static final long serialVersionUID = 4075950193187972686L;
        
        @Override
        protected RosettoValue run(ExpandedArguments args) {
            return Values.VOID;
        }
    };

}
