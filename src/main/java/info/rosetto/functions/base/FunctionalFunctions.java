package info.rosetto.functions.base;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.function.ExpandedArguments;
import info.rosetto.models.base.function.FunctionPackage;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.values.ActionCall;
import info.rosetto.models.base.values.ListValue;
import info.rosetto.models.base.values.RosettoAction;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.models.base.values.ValueType;
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
        protected RosettoValue run(ExpandedArguments args) {
            RosettoValue v = args.get("list");
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
        protected RosettoValue run(ExpandedArguments args) {
            RosettoValue v = args.get("list");
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
        protected RosettoValue run(ExpandedArguments args) {
            RosettoValue f = args.get("fn");
            RosettoValue l = args.get("list");
            RosettoAction fn = (f instanceof RosettoAction) ? 
                    (RosettoFunction) f : Contexts.getAction(f.asString());
            if(fn == BaseFunctions.pass || !(l instanceof ListValue)) return Values.NULL;
            
            List<RosettoValue> list = ((ListValue)l).getList();
            List<RosettoValue> result = new LinkedList<RosettoValue>();
            for(RosettoValue v : list) {
                result.add(fn.execute(v.asString()));
            }
            return new ListValue(result);
        }
    };
    
    public static final RosettoFunction range = new RosettoFunction("range", 
            "start", "end") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(ExpandedArguments args) {
            int start = args.get("start").asInt();
            int end = args.get("end").asInt();
            List<RosettoValue> list = new LinkedList<RosettoValue>();
            for(int i=start; i<end; i++) {
                list.add(Values.create(i));
            }
            return new ListValue(list);
        }
    };
    
    public static final RosettoFunction fn = new RosettoFunction("fn", 
            "args", "ac") {
        private static final long serialVersionUID = -411581748747383868L;
        
        @Override
        protected RosettoValue run(ExpandedArguments args) {
            RosettoValue argsValue = args.get("args");
            RosettoValue acValue = args.get("ac");
            if(acValue.getType() == ValueType.ACTION_CALL) {
                ActionCall ac = (ActionCall) acValue;
                if(argsValue.getType() == ValueType.LIST) {
                    //引数がリストの場合
                } else {
                    //引数が単一要素の場合
                }
            }
            List<RosettoValue> list = new LinkedList<RosettoValue>();
            for(int i=start; i<end; i++) {
                list.add(Values.create(i));
            }
            return new ListValue(list);
        }
    };

}
