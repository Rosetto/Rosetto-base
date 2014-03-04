/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.functions.base;

import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.base.elements.values.ListValue;
import info.rosetto.models.base.elements.values.RosettoFunction;
import info.rosetto.models.system.FunctionPackage;
import info.rosetto.models.system.Scope;
import info.rosetto.system.RosettoLogger;
import info.rosetto.system.exceptions.UnExpectedTypeValueException;
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
            "*nums") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
            long l = 0;
            double d = 0.0;
            boolean isDouble = false;
            RosettoValue nums = scope.get("nums");
            while(true) {
                RosettoValue num = nums.first();
                if(num.getType() == ValueType.DOUBLE) {
                    //double値が入れば以降の計算はすべてdouble
                    isDouble = true;
                    d += l;
                    l = 0;
                    d += num.asDouble();
                } else if(num.getType() == ValueType.INTEGER) {
                    if(isDouble) {
                        d += num.asDouble();
                    } else {
                        l += num.asLong();
                    }
                } else {
                    throw new UnExpectedTypeValueException();
                }
                nums = nums.rest();
                if(nums.getType() == ValueType.NULL) break;
            }
            return isDouble ? Values.create(d) : Values.create(l);
        }
    };
    
    public static final RosettoFunction minus = new RosettoFunction("-", 
            "*nums") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
            long l = 0;
            double d = 0.0;
            boolean isDouble = false;
            boolean isFirstItem = true;
            RosettoValue nums = scope.get("nums");
            while(true) {
                RosettoValue num = nums.first();
                if(num.getType() == ValueType.DOUBLE) {
                    //double値が入れば以降の計算はすべてdouble
                    isDouble = true;
                    d += l;
                    l = 0;
                    d = (isFirstItem) ? num.asDouble() : d-num.asDouble();
                } else if(num.getType() == ValueType.INTEGER) {
                    if(isDouble) {
                        d = d-num.asDouble();
                    } else {
                        l = (isFirstItem) ? num.asLong() : l-num.asLong();
                    }
                } else {
                    throw new UnExpectedTypeValueException();
                }
                isFirstItem = false;
                nums = nums.rest();
                if(nums.getType() == ValueType.NULL) break;
            }
            return isDouble ? Values.create(d) : Values.create(l);
        }
    };
    
    public static final RosettoFunction multiple = new RosettoFunction("*", 
            "*nums") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
            long l = 0;
            double d = 0.0;
            boolean isDouble = false;
            boolean isFirstItem = true;
            RosettoValue nums = scope.get("nums");
            while(true) {
                RosettoValue num = nums.first();
                if(num.getType() == ValueType.DOUBLE) {
                    //double値が入れば以降の計算はすべてdouble
                    isDouble = true;
                    d += l;
                    l = 0;
                    d = (isFirstItem) ? num.asDouble() : d*num.asDouble();
                } else if(num.getType() == ValueType.INTEGER) {
                    if(isDouble) {
                        d = d*num.asDouble();
                    } else {
                        l = (isFirstItem) ? num.asLong() : l*num.asLong();
                    }
                } else {
                    throw new UnExpectedTypeValueException();
                }
                isFirstItem = false;
                nums = nums.rest();
                if(nums.getType() == ValueType.NULL) break;
            }
            return isDouble ? Values.create(d) : Values.create(l);
        }
    };
    
    public static final RosettoFunction division = new RosettoFunction("/", 
            "*nums") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
            long l = 0;
            double d = 0.0;
            boolean isDouble = false;
            boolean isFirstItem = true;
            RosettoValue nums = scope.get("nums");
            while(true) {
                RosettoValue num = nums.first();
                if(num.getType() == ValueType.DOUBLE) {
                    //double値が入れば以降の計算はすべてdouble
                    isDouble = true;
                    d += l;
                    l = 0;
                    d = (isFirstItem) ? num.asDouble() : d/num.asDouble();
                } else if(num.getType() == ValueType.INTEGER) {
                    if(isDouble) {
                        d = d/num.asDouble();
                    } else {
                        l = (isFirstItem) ? num.asLong() : l/num.asLong();
                    }
                } else {
                    throw new UnExpectedTypeValueException();
                }
                isFirstItem = false;
                nums = nums.rest();
                if(nums.getType() == ValueType.NULL) break;
            }
            return isDouble ? Values.create(d) : Values.create(l);
        }
    };
    
    public static final RosettoFunction mod = new RosettoFunction("mod", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
            return Values.create(scope.get("x").asDouble() % scope.get("y").asDouble());
        }
    };
    
    
    public static final RosettoFunction eq = new RosettoFunction("eq?", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
            RosettoValue x = scope.get("x");
            RosettoValue y = scope.get("y");
            boolean result = false;
            if((x.getType() == ValueType.INTEGER || x.getType() == ValueType.DOUBLE) &&
               (y.getType() == ValueType.INTEGER || y.getType() == ValueType.DOUBLE)) {
                //数値比較の場合はdoubleで比較
                result = scope.get("x").asDouble() == scope.get("y").asDouble();
            } else {
                //それ以外は文字列表現で比較
                result = x.asString().equals(y.asString());
            }
            return Values.create(result);
        }
    };
    
    public static final RosettoFunction lt = new RosettoFunction("<", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
            return Values.create(scope.get("x").asDouble() < scope.get("y").asDouble());
        }
    };
    
    public static final RosettoFunction gt = new RosettoFunction(">", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
            return Values.create(scope.get("x").asDouble() > scope.get("y").asDouble());
        }
    };
    
    public static final RosettoFunction leq = new RosettoFunction("leq?", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
            return Values.create(scope.get("x").asDouble() <= scope.get("y").asDouble());
        }
    };
    
    public static final RosettoFunction geq = new RosettoFunction("geq?", 
            "x", "y") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
            return Values.create(scope.get("x").asDouble() >= scope.get("y").asDouble());
        }
    };

}
