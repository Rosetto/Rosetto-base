/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.system;

import info.rosetto.system.messages.SystemMessage;
import info.rosetto.system.messages.SystemMessages;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Rosetto内で使用する単一のロガー.
 * @author tohhy
 */
public class RosettoLogger {
    /**
     * シングルトンインスタンス.
     */
    private static final Logger instance = Logger.getLogger("info.rosetto");
    
    /**
     * ロガーのデフォルトの出力レベル.
     */
    private static final Level defaultLevel = Level.INFO;
    
    /**
     * ロガーに例外が投げられた際のデフォルトの出力レベル.
     */
    private static final Level defaultExceptionLogLevel = Level.WARNING;
    
    /**
     * ロガーが使用するコンソール出力用のハンドラー.
     */
    private static final ConsoleHandler handler = new ConsoleHandler();
    
    /**
     * このロガーの現在の例外出力レベル.
     */
    private static Level exceptionLogLevel = defaultExceptionLogLevel;
    
    static {
        for(Handler h : instance.getHandlers()) {
            if(h instanceof ConsoleHandler)
                instance.removeHandler(h);
        }
        instance.setUseParentHandlers(false);
        instance.addHandler(handler);
        instance.setLevel(defaultLevel);
    }
    
    /**
     * このロガーがログを出力するレベルの閾値を設定する.
     * @param level このロガーがログを出力するレベルの閾値
     */
    public static void setLevel(Level level) {
        instance.setLevel(level);
        handler.setLevel(level);
    }
    
    /**
     * ロガーの出力レベルをデフォルトに戻す.
     */
    public static void resetLevel() {
        instance.setLevel(defaultLevel);
        handler.setLevel(defaultLevel);
    }
    
    /**
     * ゲームの実行が維持できなくなる致命的なエラーに付加するログ.
     * @param body 出力内容
     */
    public static void severe(String body) {
        instance.severe(body);
    }
    
    /**
     * ゲーム中で予期しない動作が発生する可能性がある例外発生等に付加するログ.
     * @param error 出力するエラー
     */
    public static void warning(SystemMessage error, String...args) {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<args.length; i++) {
            sb.append(args[i]);
            if(i != args.length-1) sb.append(",");
        }
        warning(SystemMessages.get(error) + sb.toString());
    }
    
    /**
     * ゲーム中で予期しない動作が発生する可能性がある例外発生等に付加するログ.
     * @param body 出力内容
     */
    public static void warning(String body) {
        instance.warning(body);
    }
    
    /**
     * ゲーム中で予期しない動作が発生する可能性は低いが、何らかの問題に繋がる可能性がある動作等に付加するログ.
     * @param body 出力内容
     */
    public static void info(String body) {
        instance.info(body);
    }
    
    /**
     * 正常な動作のうち、注目するべきチェックポイントになり得る部分に付加するログ.
     * @param body 出力内容
     */
    public static void fine(String body) {
        instance.fine(body);
    }
    
    /**
     * 正常な動作のうち、特に注意するべき内容を含まない部分に付加するログ.
     * @param body 出力内容
     */
    public static void finer(String body) {
        instance.finer(body);
    }
    
    /**
     * 現状使用予定はない.
     * @param body 出力内容
     */
    protected static void finest(String body) {
        instance.finest(body);
    }
    
    /**
     * 現状使用予定はない.
     * @param body 出力内容
     */
    protected static void config(String body) {
        instance.config(body);
    }

    /**
     * 関数内などで例外を捕まえ、ログ出力のみで処理する.
     * 出力するログのレベルはgetExceptionLogLevelの値になる.
     * @param e 処理する例外
     */
    public static void catchException(Throwable e) {
        instance.log(exceptionLogLevel, "Exception occurred", e);
    }
    
    /**
     * 関数内などで例外を捕まえ、指定レベルのログ出力のみで処理する.
     * @param e 処理する例外
     * @param level ログレベル
     */
    public static void catchException(Throwable e, Level level) {
        instance.log(exceptionLogLevel, "Exception occurred", e);
    }
    
    /**
     * 例外をキャッチせずに、例外の通過ログを取る.
     * @param sourceClass 例外発生クラス
     * @param sourceMethod 例外発生メソッド
     * @param thrown スローされる例外
     */
    @SuppressWarnings("rawtypes") 
    public static void throwing(Class sourceClass, String sourceMethod, Throwable thrown) {
        instance.throwing(sourceClass.getName(), sourceMethod, thrown);
    }

    /**
     * catchExceptionでキャッチした例外の出力レベルを取得する.
     * @return catchExceptionでキャッチした例外の出力レベル
     */
    public static Level getExceptionLogLevel() {
        return exceptionLogLevel;
    }

    /**
     * catchExceptionでキャッチした例外の出力レベルを指定する.
     * @param exceptionLogLevel catchExceptionでキャッチした例外の出力レベル
     */
    public static void setExceptionLogLevel(Level exceptionLogLevel) {
        RosettoLogger.exceptionLogLevel = exceptionLogLevel;
    }
    
    /**
     * catchExceptionでキャッチした例外の出力レベルをデフォルトに戻す.
     */
    public static void resetExceptionLogLevel() {
        RosettoLogger.exceptionLogLevel = defaultExceptionLogLevel;
    }
}
