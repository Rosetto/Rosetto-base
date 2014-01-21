/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.models.base.function;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

/**
 * rosetto中の関数呼び出しを表すモデル.
 * 実行する関数の種類と実行の際に渡す引数を保持する.
 * @author tohhy
 */
@Immutable
public class FunctionCall implements Serializable {
    private static final long serialVersionUID = 2814186388967644971L;
    
    /**
     * このアクションが実行する関数名.
     */
    @Nonnull
    private final String functionName;
    
    /**
     * 関数実行時の引数リスト.
     */
    @Nonnull
    private final RosettoArguments args;
    
    /**
     * 何もしないFunctionCall.空の関数呼び出しを行う.
     */
    public static final FunctionCall EMPTY = 
            new FunctionCall(RosettoFunction.pass.getFullName(), RosettoArguments.EMPTY);
    
    /**
     * 引数なしで指定関数を呼び出すFunctionCallを生成する.
     * @param functionName 呼び出す関数名
     */
    public FunctionCall(String functionName) {
        this(functionName, RosettoArguments.EMPTY);
    }
    
    /**
     * 引数なしで指定関数を呼び出すFunctionCallを生成する.
     * @param functionName 呼び出す関数名
     */
    public FunctionCall(FunctionName functionName) {
        this(functionName.getFullName(), RosettoArguments.EMPTY);
    }
    
    /**
     * 指定引数で指定関数を呼び出すFunctionCallを生成する.
     * @param functionName 呼び出す関数名
     * @param args 適用する引数
     */
    public FunctionCall(String functionName, String args) {
        this(functionName, new RosettoArguments(args));
    }
    
    /**
     * 指定引数で指定関数を呼び出すFunctionCallを生成する.
     * @param functionName 呼び出す関数名
     * @param args 適用する引数
     */
    public FunctionCall(FunctionName functionName, String args) {
        this(functionName.getFullName(), new RosettoArguments(args));
    }
    
    /**
     * 指定引数で指定関数を呼び出すFunctionCallを生成する.
     * @param functionName 呼び出す関数名
     * @param args 適用する引数
     */
    public FunctionCall(String functionName, List<RosettoArgument> args) {
        if(functionName == null)
            throw new IllegalArgumentException("関数オブジェクトがnullです");
        this.functionName = functionName;
        this.args = (args != null) ? new RosettoArguments(args) : RosettoArguments.EMPTY;
    }
    
    /**
     * 指定引数で指定関数を呼び出すFunctionCallを生成する.
     * @param functionName 呼び出す関数名
     * @param args 適用する引数
     */
    public FunctionCall(String functionName, RosettoArguments args) {
        if(functionName == null)
            throw new IllegalArgumentException("関数オブジェクトがnullです");
        this.functionName = functionName;
        this.args = (args != null) ? args : RosettoArguments.EMPTY;
    }
    
    /**
     * 指定引数で指定関数を呼び出すFunctionCallを生成する.
     * @param functionName 呼び出す関数名
     * @param args 適用する引数
     */
    public FunctionCall(FunctionName functionName, RosettoArguments args) {
        this(functionName.getFullName(), args);
    }
    
    /**
     * このRosettoActionのtypeの文字列表現を返す.
     * この関数呼び出しを表現する正規記法のタグ.
     */
    @Override
    public String toString() {
        return toTag();
    }

    /**
     * この関数呼び出しを表現する正規記法のタグに再度変換する.
     */
    public String toTag() {
        if(args.getSize() == 0) return "[" + functionName + "]";
        return "[" + functionName + " " + args.getStringArgs() + "]";
    }
    
    /**
     * このFunctionCallで呼び出される関数名を返す.
     * @return このFunctionCallで呼び出される関数名
     */
    public String getFunctionName() {
        return functionName;
    }
    
    /**
     * この呼び出しにおいて関数に渡される引数リストを返す.
     * @return この呼び出しにおいて関数に渡される引数リスト
     */
    public RosettoArguments getArgs() {
        return args;
    }
}
