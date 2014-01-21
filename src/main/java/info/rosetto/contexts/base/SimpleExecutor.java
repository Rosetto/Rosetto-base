package info.rosetto.contexts.base;

import info.rosetto.models.base.function.RosettoArguments;
import info.rosetto.models.base.function.RosettoFunction;
import info.rosetto.models.base.values.ActionCall;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.models.base.values.ValueType;
import info.rosetto.utils.base.RosettoLogger;

public class SimpleExecutor {
    
//
//    /**
//     * 指定したFunctionCallを実行する.
//     * 関数名から関数を生成し、その関数と引数から実行時引数を生成し、
//     * 関数のexecメソッドを呼び出す.
//     * 見つからなかった場合はマクロコンテキストを参照する.
//     * 指定名の関数が両コンテキスト中に存在しなかった場合は処理が省略される.
//     * @return 実行に成功したかどうか
//     */
//    private boolean execute(FunctionCall funcCall) {
//        String functionName = funcCall.getFunctionName();
//        RosettoValue v = Contexts.get(functionName);
//        if(v == null) return false;
//        
//        if(v.getType() == ValueType.FUNCTION) {
//            RosettoFunction f = (RosettoFunction) v;
//            RosettoArguments args = funcCall.getArgs();
//            f.exec(args);
//            Observatories.getAction().functionExecuted(funcCall);
//            return true;
//        }
//        
//        if(v.getType() == ValueType.MACRO) {
//            MacroBlock macro = (MacroBlock) v;
//            Contexts.getProgress().getWhole()
//            .pushScenario(macro.create(args));
//            return true;
//        }
//        
//        //それでもなければ何もしない
//        RosettoLogger.warning("関数 " + functionName + "がコンテキスト中に見つかりません");
//        return false;
//    }
}
