/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package info.rosetto.utils.base;

/**
 * 文字列操作に関連するユーティリティクラス.
 * @author tohhy
 */
public class StringUtils {
    
    /**
     * 受け取った文字列をInteger.parseIntで解釈して返す.
     * もし受け取った文字列がnullだった場合、解釈できなかった場合はdefaultValueを返す.
     * @param intStr intとして解釈する文字列
     * @param defaultValue nullや例外発生時に返すint値
     * @return 受け取った文字列を解釈したint値、文字列がnullか例外が発生した場合はデフォルト値
     */
    public static int parse(String intStr, int defaultValue) {
        if(intStr == null || intStr.length() == 0) return defaultValue;
        try {
            return Integer.parseInt(intStr);
        } catch (Exception e) {
            RosettoLogger.catchException(e);
        }
        return defaultValue;
    }
    
    /**
     * 受け取った文字列をDouble.parseDoubleで解釈して返す.
     * もし受け取った文字列がnullだった場合、解釈できなかった場合はdefaultValueを返す.
     * @param doubkeStr doubleとして解釈する文字列
     * @param defaultValue nullや例外発生時に返すdouble値
     * @return 受け取った文字列を解釈したdouble値、文字列がnullか例外が発生した場合はデフォルト値
     */
    public static double parse(String doubleStr, double defaultValue) {
        if(doubleStr == null || doubleStr.length() == 0) return defaultValue;
        try {
            return Double.parseDouble(doubleStr);
        } catch (Exception e) {
            RosettoLogger.catchException(e);
        }
        return defaultValue;
    }
    
    /**
     * 受け取った文字列をLong.parseLongで解釈して返す.
     * もし受け取った文字列がnullだった場合、解釈できなかった場合はdefaultValueを返す.
     * @param longStr longとして解釈する文字列
     * @param defaultValue nullや例外発生時に返すlong値
     * @return 受け取った文字列を解釈したlong値、文字列がnullか例外が発生した場合はデフォルト値
     */
    public static long parse(String longStr, long defaultValue) {
        if(longStr == null || longStr.length() == 0) return defaultValue;
        try {
            return Long.parseLong(longStr);
        } catch (Exception e) {
            RosettoLogger.catchException(e);
        }
        return defaultValue;
    }
    
    /**
     * 受け取った文字列をBoolean.parseBooleanで解釈して返す.
     * もし受け取った文字列がnullだった場合、解釈できなかった場合はdefaultValueを返す.
     * @param boolStr booleanとして解釈する文字列
     * @param defaultValue nullや例外発生時に返すbool値
     * @return 受け取った文字列を解釈したbool値、文字列がnullか例外が発生した場合はデフォルト値
     */
    public static boolean parse(String boolStr, boolean defaultValue) {
        if(boolStr == null || boolStr.length() == 0) return defaultValue;
        try {
            return Boolean.parseBoolean(boolStr);
        } catch (Exception e) {
            RosettoLogger.catchException(e);
        }
        return defaultValue;
    }
    
    /**
     * 受け取った文字列をそのまま返す.
     * もし受け取った文字列がnullだった場合はdefaultValueを返す.
     */
    public static String parse(String str, String defaultValue) {
        if(str != null) return str;
        return defaultValue;
    }
    
    
    
    /**
     * 指定文字列を指定クラスに変換できるかどうかを返す.
     * 変換先はint,double,bool,stringの4種を想定し、それぞれパース可能かを確かめる.
     * @param s 変換元の文字列
     * @param c 変換先のクラス
     * @return 指定文字列を指定クラスに変換できるかどうか
     */
    public static boolean canConvert(String s, Class<?> c) {
        if(c.isAssignableFrom(String.class)) {
            //変換先がStringクラスなら変換可能なのは自明
            return true;
        } else if(c.isAssignableFrom(int.class) || c.isAssignableFrom(Integer.class)) {
            //変換先がIntegerクラスならparseInt可能かどうかをチェック
            return isIntValue(s);
        } else if(c.isAssignableFrom(double.class) || c.isAssignableFrom(Double.class)) {
            //変換先がDoubleクラスならparseDouble可能かどうかをチェック
            return isDoubleValue(s);
        } else if(c.isAssignableFrom(boolean.class) || c.isAssignableFrom(Boolean.class)) {
            //変換先がBooleanクラスならparseBoolean可能かどうかをチェック
            return isBooleanValue(s);
        }
        //これ以外のクラスならパース不可
        return false;
    }
    
    
    /**
     * 文字列をInteger.parseIntで変換できるかどうかを返す.
     * @param str チェックする文字列
     * @return int型に変換可能かどうか
     */
    public static boolean isIntValue(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 文字列をLong.parseLongで変換できるかどうかを返す.
     * @param str チェックする文字列
     * @return long型に変換可能かどうか
     */
    public static boolean isLongValue(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 文字列をDouble.parseDoubleで変換できるかどうかを返す.
     * @param str チェックする文字列
     * @return double型に変換可能かどうか
     */
    public static boolean isDoubleValue(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 文字列をFloat.parseFloatで変換できるかどうかを返す.
     * @param str チェックする文字列
     * @return float型に変換可能かどうか
     */
    public static boolean isFloatValue(String str) {
        try {
            Float.parseFloat(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 文字列をBoolean.parseBooleanで変換できるかどうかを返す.
     * @param str チェックする文字列
     * @return boolean型に変換可能かどうか
     */
    public static boolean isBooleanValue(String str) {
        try {
            Boolean.parseBoolean(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
