package info.rosetto.functions.base;

import info.rosetto.models.base.function.FunctionPackage;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.models.state.variables.Scope;
import info.rosetto.utils.base.Values;

public class ArithmeticFunctions extends FunctionPackage {

    private static ArithmeticFunctions instance;
    
    public static ArithmeticFunctions getInstance() {
        if(instance == null) {
            instance = new ArithmeticFunctions();
        }
        return instance;
    }
    
    
    public ArithmeticFunctions() {
        super(plus, multiple);
    }
    
    public static final RosettoFunction plus = new RosettoFunction("+", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope args) {
            return Values.create(args.get("x").asInt() + args.get("y").asInt());
        }
    };
    
    public static final RosettoFunction multiple = new RosettoFunction("*", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope args) {
            return Values.create(args.get("x").asInt() * args.get("y").asInt());
        }
    };

}
