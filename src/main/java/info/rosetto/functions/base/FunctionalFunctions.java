/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.functions.base;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoAction;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.ValueType;
import info.rosetto.models.base.elements.values.ListValue;
import info.rosetto.models.base.elements.values.RosettoFunction;
import info.rosetto.models.system.FunctionPackage;
import info.rosetto.models.system.Scope;
import info.rosetto.utils.base.FunctionUtils;
import info.rosetto.utils.base.Values;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FunctionalFunctions extends FunctionPackage {
    
    private static FunctionalFunctions instance;
    
    private FunctionalFunctions() {
        super(map, range, cond);
    }
    
    public static FunctionalFunctions getInstance() {
        if(instance == null) {
            instance = new FunctionalFunctions();
        }
        return instance;
    }
    
    
    public static final RosettoFunction map = new RosettoFunction("map", 
            "fn", "list") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, ListValue args) {
            RosettoValue f = scope.get("fn");
            RosettoValue l = scope.get("list");
            RosettoAction fn = (f instanceof RosettoFunction) ? 
                    (RosettoFunction) f : Contexts.getAction(f.asString());
            
            if(l instanceof ListValue) {
                List<RosettoValue> list = ((ListValue)l).getList();
                List<RosettoValue> result = new LinkedList<RosettoValue>();
                for(RosettoValue v : list) {
                    result.add(fn.execute(v.asString(), scope).evaluate(scope));
                }
                return new ListValue(result);
            } else {
                return fn.execute(l.asString(), scope).evaluate(scope);
            }
        }
    };
    
    public static final RosettoFunction range = new RosettoFunction("range", 
            "start", "end") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(Scope scope, ListValue args) {
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
        protected Scope createScope(ListValue args, Scope parentScope) {
            Map<String, RosettoValue> parsed = args.bind(this, parentScope);
            Scope scope = new Scope(parentScope);
            scope.set("args", parsed.get("args"));
            return scope;
        }
        
        @Override
        protected RosettoValue run(Scope scope, ListValue args) {
            if(args.first().getType() != ValueType.LIST) {
                RosettoValue condition = args.first().evaluate(scope);
                if(condition.asBool() == true) {
                    RosettoValue actions = args.rest().evaluate(scope);
                    return FunctionUtils.doActions(scope, actions);
                }
            } else {
                for(RosettoValue v : args.getList()) {
                    ListValue l = (ListValue)v;
                    RosettoValue condition = l.first().evaluate(scope);
                    if(condition.asBool() == true) {
                        RosettoValue actions = l.rest().evaluate(scope);
                        return FunctionUtils.doActions(scope, actions);
                    }
                }
            }
            return Values.NULL;
        }
    };

}
