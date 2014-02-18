/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.functions.base;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.function.FunctionPackage;
import info.rosetto.models.base.function.RosettoArguments;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.values.ListValue;
import info.rosetto.models.base.values.RosettoAction;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.models.state.variables.Scope;
import info.rosetto.utils.base.Values;

import java.util.LinkedList;
import java.util.List;

public class FunctionalFunctions extends FunctionPackage {
    
    private static FunctionalFunctions instance;
    
    private FunctionalFunctions() {
        super(first, rest, map, range);
    }
    
    
    public static FunctionalFunctions getInstance() {
        if(instance == null) {
            instance = new FunctionalFunctions();
        }
        return instance;
    }
    
    
    public static final RosettoFunction first = new RosettoFunction("first", 
            "list") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, RosettoArguments args) {
            RosettoValue v = scope.get("list");
            if(v instanceof ListValue) {
                return ((ListValue)v).first();
            }
            return Values.NULL;
        }
    };
    
    public static final RosettoFunction rest = new RosettoFunction("rest", 
            "list") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, RosettoArguments args) {
            RosettoValue v = scope.get("list");
            if(v instanceof ListValue) {
                return ((ListValue)v).rest();
            }
            return Values.NULL;
        }
    };
    
    public static final RosettoFunction map = new RosettoFunction("map", 
            "fn", "list") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, RosettoArguments args) {
            RosettoValue f = scope.get("fn");
            RosettoValue l = scope.get("list");
            RosettoAction fn = (f instanceof RosettoAction) ? 
                    (RosettoFunction) f : Contexts.getAction(f.asString());
            if(fn == BaseFunctions.pass || !(l instanceof ListValue)) return Values.NULL;
            
            List<RosettoValue> list = ((ListValue)l).getList();
            List<RosettoValue> result = new LinkedList<RosettoValue>();
            for(RosettoValue v : list) {
                result.add(fn.execute(v.asString(), scope));
            }
            return new ListValue(result);
        }
    };
    
    public static final RosettoFunction range = new RosettoFunction("range", 
            "start", "end") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, RosettoArguments args) {
            int start = scope.get("start").asInt();
            int end = scope.get("end").asInt();
            List<RosettoValue> list = new LinkedList<RosettoValue>();
            for(int i=start; i<end; i++) {
                list.add(Values.create(i));
            }
            return new ListValue(list);
        }
    };
    

}
