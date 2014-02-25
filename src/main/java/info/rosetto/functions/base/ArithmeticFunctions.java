/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.functions.base;

import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.values.OptionableList;
import info.rosetto.models.base.function.FunctionPackage;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.system.Scope;
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
        super(plus, minus, multiple, division, mod,
              eq, lt, gt, leq, geq);
    }
    
    public static final RosettoFunction plus = new RosettoFunction("+", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, OptionableList rawArgs) {
            return Values.create(scope.get("x").asInt() + scope.get("y").asInt());
        }
    };
    
    public static final RosettoFunction minus = new RosettoFunction("-", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, OptionableList rawArgs) {
            return Values.create(scope.get("x").asInt() - scope.get("y").asInt());
        }
    };
    
    public static final RosettoFunction multiple = new RosettoFunction("*", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, OptionableList rawArgs) {
            return Values.create(scope.get("x").asInt() * scope.get("y").asInt());
        }
    };
    
    public static final RosettoFunction division = new RosettoFunction("/", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, OptionableList rawArgs) {
            return Values.create(scope.get("x").asDouble() / scope.get("y").asDouble());
        }
    };
    
    public static final RosettoFunction mod = new RosettoFunction("mod", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, OptionableList rawArgs) {
            return Values.create(scope.get("x").asInt() % scope.get("y").asInt());
        }
    };
    
    
    public static final RosettoFunction eq = new RosettoFunction("eq?", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, OptionableList rawArgs) {
            return Values.create(scope.get("x").asDouble() == scope.get("y").asDouble());
        }
    };
    
    public static final RosettoFunction lt = new RosettoFunction("<", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, OptionableList rawArgs) {
            return Values.create(scope.get("x").asDouble() < scope.get("y").asDouble());
        }
    };
    
    public static final RosettoFunction gt = new RosettoFunction(">", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, OptionableList rawArgs) {
            return Values.create(scope.get("x").asDouble() > scope.get("y").asDouble());
        }
    };
    
    public static final RosettoFunction leq = new RosettoFunction("leq?", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, OptionableList rawArgs) {
            return Values.create(scope.get("x").asDouble() <= scope.get("y").asDouble());
        }
    };
    
    public static final RosettoFunction geq = new RosettoFunction("geq?", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, OptionableList rawArgs) {
            return Values.create(scope.get("x").asDouble() >= scope.get("y").asDouble());
        }
    };

}
