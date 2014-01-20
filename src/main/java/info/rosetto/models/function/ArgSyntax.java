package info.rosetto.models.function;

import java.io.Serializable;

public class ArgSyntax implements Serializable {
    private static final long serialVersionUID = 4249071714150861333L;

    private final String macroExpandPrefix;
    
    private final String expandArgRegex;
    
    private final String macroAllExpandArg;
    
    private final char macroDefaultValueSeparator;
    
    private final char mutableArgNumSeparator;
    
    public ArgSyntax(String macroExpandPrefix, 
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
