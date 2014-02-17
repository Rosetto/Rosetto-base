/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.state.parser;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;

/**
 * 変数処理の文法を定義したクラス.
 * @author tohhy
 */
@Immutable
public class ArgumentSyntax implements Serializable {
    private static final long serialVersionUID = 4249071714150861333L;

    private final String macroExpandPrefix;
    
    private final String expandArgRegex;
    
    private final String macroAllExpandArg;
    
    private final char macroDefaultValueSeparator;
    
    private final char mutableArgNumSeparator;
    
    public ArgumentSyntax(String macroExpandPrefix, 
            String macroAllExpandArg,
            char macroDefaultValueSeparator,
            char mutableArgNumSeparator) {
        this.macroExpandPrefix = macroExpandPrefix;
        this.expandArgRegex = toMacroRegex(macroExpandPrefix);
        this.macroAllExpandArg = macroAllExpandArg;
        this.macroDefaultValueSeparator = macroDefaultValueSeparator;
        this.mutableArgNumSeparator = mutableArgNumSeparator;
    }
    
    /**
     * マクロ展開引数の接頭詞からマクロ展開引数にマッチする正規表現を生成する.
     * @param prefix マクロ展開引数の接頭詞
     * @return マクロ展開引数にマッチする正規表現
     */
    private String toMacroRegex(String prefix) {
        return "^" + prefix + ".*";
    }

    public String getMacroExpandPrefix() {
        return macroExpandPrefix;
    }

    public char getMacroDefaultValueChar() {
        return macroDefaultValueSeparator;
    }

    public String getMacroAllExpandArg() {
        return macroAllExpandArg;
    }

    public String getExpandArgRegex() {
        return expandArgRegex;
    }

    public char getMutableArgNumSeparator() {
        return mutableArgNumSeparator;
    }
}
