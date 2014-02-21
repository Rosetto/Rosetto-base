/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.functions.base;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.ActionCall;
import info.rosetto.models.base.elements.MixedStore;
import info.rosetto.models.base.elements.RosettoAction;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.base.elements.values.ListValue;
import info.rosetto.models.base.elements.values.MixedStoreValue;
import info.rosetto.models.base.function.FunctionPackage;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.state.variables.Scope;
import info.rosetto.utils.base.Values;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FunctionalFunctions extends FunctionPackage {
    
    private static FunctionalFunctions instance;
    
    private FunctionalFunctions() {
        super(first, rest, map, range, cond);
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
        protected RosettoValue run(Scope scope, MixedStore args) {
            RosettoValue v = scope.get("list");
            if(v instanceof RosettoValue) {
                return ((RosettoValue)v).first();
            }
            return Values.NULL;
        }
    };
    
    public static final RosettoFunction rest = new RosettoFunction("rest", 
            "list") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, MixedStore args) {
            RosettoValue v = scope.get("list");
            if(v instanceof RosettoValue) {
                return ((RosettoValue)v).rest();
            }
            return Values.NULL;
        }
    };
    
    public static final RosettoFunction map = new RosettoFunction("map", 
            "fn", "list") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, MixedStore args) {
            RosettoValue f = scope.get("fn");
            RosettoValue l = scope.get("list");
            RosettoAction fn = (f instanceof RosettoFunction) ? 
                    (RosettoFunction) f : Contexts.getAction(f.asString());
            if(fn == BaseFunctions.pass) return Values.NULL;
            
            if(l instanceof ListValue) {
                List<RosettoValue> list = ((ListValue)l).getList();
                List<RosettoValue> result = new LinkedList<RosettoValue>();
                for(RosettoValue v : list) {
                    result.add(fn.execute(v.asString(), scope).evaluate(scope));
                }
                return new ListValue(result);
                
            } else if(l instanceof MixedStoreValue) {
                List<RosettoValue> list = ((MixedStoreValue)l).getList();
                List<RosettoValue> result = new LinkedList<RosettoValue>();
                for(RosettoValue v : list) {
                    result.add(fn.execute(v.asString(), scope).evaluate(scope));
                }
                return new ListValue(result);
            }
            return Values.NULL;
        }
    };
    
    public static final RosettoFunction range = new RosettoFunction("range", 
            "start", "end") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, MixedStore args) {
            int start = scope.get("start").asInt();
            int end = scope.get("end").asInt();
            List<RosettoValue> list = new LinkedList<RosettoValue>();
            for(int i=start; i<end; i++) {
                list.add(Values.create(i));
            }
            return new ListValue(list);
        }
    };
    
    

    public static final RosettoFunction cond = new RosettoFunction("cond", 
            "*args") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected Scope createScope(MixedStore args, Scope parentScope) {
            Map<String, RosettoValue> parsed = args.bind(this, parentScope);
            Scope scope = new Scope(parentScope);
            scope.set("args", parsed.get("args"));
            return scope;
        }
        
        @Override
        protected RosettoValue run(Scope scope, MixedStore args) {
            RosettoValue argsValue = scope.get("args");
            if(argsValue.getType() == ValueType.LIST) {
                for(RosettoValue v : ((ListValue)argsValue).getList()) {
                    ListValue l = (ListValue)v;
                    RosettoValue condition = l.first().evaluate(scope);
                    if(condition.asBool() == true) {
                        RosettoValue actions = l.rest().evaluate(scope);
                        RosettoValue result = actions;
                        if(actions instanceof ListValue)
                        for(RosettoValue a : ((ListValue)actions).getList()) {
                            result = a;
                            if(a instanceof ActionCall)
                                result = ((ActionCall)a).evaluate(scope);
                        }
                        return result;
                    }
                    
                }
            }
            return Values.NULL;
        }
    };

}
