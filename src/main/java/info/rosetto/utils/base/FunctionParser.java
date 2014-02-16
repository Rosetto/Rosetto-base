package info.rosetto.utils.base;

import info.rosetto.models.base.function.RosettoArgument;
import info.rosetto.models.base.values.ActionCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 
 * @author tohhy
 *
 */
public class FunctionParser {
    
    /**
     * 
     * @param functionCall
     * @return
     */
    public static ActionCall parse(String functionCall) {
        if(functionCall == null)
            throw new IllegalArgumentException("arg must not be null");
        String removeQuoted = TextUtils.removeAllDoubleQuotedStrings(functionCall);
        int lpCount = TextUtils.containsCount(removeQuoted, '(');
        int rpCount = TextUtils.containsCount(removeQuoted, ')');
        if(lpCount != rpCount)
            throw new IllegalArgumentException("parse error: different left/right parensis count");
        //括弧を含む引数ならロジックを分岐
        if(lpCount > 0) return parse(functionCall, lpCount);
        
        //それ以外なら
        String funcName = null;
        ArrayList<RosettoArgument> result = new ArrayList<RosettoArgument>();
        String[] splited = ParserUtils.splitStringArgs(functionCall);
        for(String str : splited) {
            if(funcName == null) {
                funcName = str;
                continue;
            }
            int equalPosition = str.indexOf("=");
            if(equalPosition == -1) {
                result.add(new RosettoArgument(TextUtils.removeDoubleQuote(str)));
            } else {
                String key = str.substring(0, equalPosition);
                String value = TextUtils.removeDoubleQuote(str.substring(equalPosition + 1));
                result.add(new RosettoArgument(key, value));
            }
        }
        return new ActionCall(funcName, result);
    }
    
    private static ActionCall parse(String functionCall, int parensisCount) {
        char[] chars = functionCall.toCharArray();
        int len = functionCall.length();
        Stack<Integer> stack = new Stack<Integer>();
        Map<Integer, Integer> coressMap = new HashMap<Integer, Integer>();
        Map<Integer, ActionCall> fcMap = new HashMap<Integer, ActionCall>();
        coressMap.put(0, -1);
        for(int i=0; i<len; i++) {
            char c = chars[i];
            switch(c) {
            case '(':
                stack.push(i);
                break;
            case ')':
                if(stack.size() == 0)
                    throw new IllegalArgumentException("parse error");
                int start = stack.pop();
                coressMap.put(start, i);
                ActionCall fc = parseFunctionCall(chars, start+1, i-1, coressMap, fcMap);
                fcMap.put(start, fc);
                break;
            }
        }
        return parseFunctionCall(chars, 0, len-1, coressMap, fcMap);
    }
    
    
    private static ActionCall parseFunctionCall(char[] chars, 
            int contentStart, int contentEnd, 
            Map<Integer, Integer> coressMap, Map<Integer, ActionCall> fcMap) {
        String funcName = null;
        List<RosettoArgument> result = new LinkedList<RosettoArgument>();
        StringBuilder buf = new StringBuilder();
        for(int i=contentStart; i<=contentEnd; i++) {
            String key = null;
            switch(chars[i]) {
                case '(':
                    if(buf.length() > 0) {
                        String s = buf.toString();
                        if(s.endsWith("=")) {
                            key = s.substring(0, s.length()-1);
                        } else if(funcName == null) {
                            funcName = buf.toString();
                        } else {
                            result.add(new RosettoArgument(buf.toString()));
                        }
                        buf = new StringBuilder();
                    }
                    result.add(new RosettoArgument(key, fcMap.get(i)));
                    System.out.println(i);
                    i = coressMap.get(i);
                break;
                case ')':
                    throw new IllegalArgumentException("parse error");
                case ' ':
                    if(buf.length() > 0) {
                        if(funcName == null) {
                            funcName = buf.toString();
                        } else {
                            result.add(new RosettoArgument(buf.toString()));
                        }
                        buf = new StringBuilder();
                    }
                break;
                default:
                    buf.append(chars[i]);
            }
        }
        if(buf.length() > 0) {
            if(funcName == null) {
                funcName = buf.toString();
            } else {
                result.add(new RosettoArgument(buf.toString()));
            }
            buf = new StringBuilder();
        }
        return new ActionCall(funcName, result);
    }
}
