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
import info.rosetto.utils.base.Values;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author tohhy
 *
 */
public class MathFunctions extends FunctionPackage {
    
    private static MathFunctions instance;
    
    public static MathFunctions getInstance() {
        if(instance == null) {
            instance = new MathFunctions();
        }
        return instance;
    }
    
    public MathFunctions() {
        super(sqrt, random);
    }
    
    public static final RosettoFunction sqrt = new RosettoFunction("sqrt", 
            "*nums") {
        private static final long serialVersionUID = -521290113842191274L;
        @Override
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
            List<RosettoValue> result = new LinkedList<RosettoValue>();
            RosettoValue args = scope.get("nums");
            
            if(args.size() == 1)
                return Values.create(Math.sqrt(args.first().asDouble()));
            
            while(true) {
                result.add(Values.create(Math.sqrt(args.first().asDouble())));
                args = args.rest();
                if(args.getType() == ValueType.NULL) break;
            }
            return new ListValue(result);
        }
    };
    
    public static final RosettoFunction random = new RosettoFunction("random") {
        
        private static final long serialVersionUID = -2119903387198279981L;
        
        @Override
        protected RosettoValue run(Scope scope, ListValue rawArgs) {
            return Values.create(Math.random());
        }
    };

}
