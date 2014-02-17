package info.rosetto.functions.base;

import info.rosetto.models.base.function.FunctionPackage;

public class ArithmeticFunctions extends FunctionPackage {

    private static ArithmeticFunctions instance;
    
    public static ArithmeticFunctions getInstance() {
        if(instance == null) {
            instance = new ArithmeticFunctions();
        }
        return instance;
    }
    
    
    public ArithmeticFunctions() {
        super();
    }
    
    

}
