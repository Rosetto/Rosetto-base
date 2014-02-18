/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.parsers;

import info.rosetto.utils.base.TextUtils;

import org.frows.lilex.designator.Designator;
import org.frows.lilex.designator.HeadDesignator;
import org.frows.lilex.designator.MiddleDesignator;
import org.frows.lilex.designator.TailDesignator;

/**
 * 汎用的な指示子を生成するユーティリティ.
 * @author tohhy
 */
public class StdDesignators {
    
    /**
     * コメントの行中指示子を生成する.<br>
     * 行中にコメント記号を含んでいればそれ以降を取り除いて返す.<br>
     * コメント記号がなければそのまま返す.
     */
    public static MiddleDesignator createMiddleComment(String definition) {
        return createMiddleComment(definition, Designator.DEFAULT_PRIORITY);
    }
    
    /**
     * コメントの行中指示子を生成する.<br>
     * 行中にコメント記号を含んでいればそれ以降を取り除いて返す.<br>
     * コメント記号がなければそのまま返す.
     */
    public static MiddleDesignator createMiddleComment(String definition, int priority) {
        return new MiddleDesignator(definition, priority) {
            
            @Override
            public String processLine(String line, int designatorIndex) {
                //単純に指示子以降を取り除いて返す.
                if(designatorIndex >= 0)
                    return line.substring(0, designatorIndex);
                return line;
            }
        };
    }
    
    /**
     * 行中置換指示子を生成する.
     */
    public static MiddleDesignator createMiddleReplace(
            String definition, final String replace, int priority) {
        return new MiddleDesignator(definition, priority) {
            @Override
            public String processLine(String line, int designatorIndex) {
                return line.replace(getDefinition(), replace);
            }
        };
    }
    
    /**
     * コメントの行頭指示子を生成する.<br>
     * 行頭にコメント記号があれば空行を返す.<br>
     * コメント記号がなければそのまま返す.
     */
    public static HeadDesignator createHeadComment(String definition) {
        return new HeadDesignator(definition) {
            
            @Override
            protected String processLine(String subLine) {
                //実行されている＝すでに行頭にコメント文字があったということなので空文字
                return "";
            }
        };
    }
    
    /**
     * 指定文字で囲う行頭指示子を生成する.
     */
    public static HeadDesignator createHeadWrap(String definition, 
            final String wrapLeft, final String wrapRight) {
        return new HeadDesignator(definition) {
            
            @Override
            protected String processLine(String subLine) {
                return wrapLeft + subLine + wrapRight;
            }
        };
    }
    
    /**
     * 単純置換の行頭指示子を生成する.
     */
    public static HeadDesignator createHeadReplace(String definition, final String replacement) {
        return new HeadDesignator(definition) {
            @Override
            public String processLine(String line) {
                return replacement;
            }
        };
    }
    
    /**
     * タグ変換の行頭指示子を生成する.
     */
    public static HeadDesignator createHeadConvertTag(String definition, final String tagName) {
        return new HeadDesignator(definition) {
            @Override
            public String processLine(String line) {
                if(line.length() == 0) return "[" + tagName + "]";
                return "[" + tagName + " " + line + "]";
            }
        };
    }
    
    /**
     * パッケージ呼び出しの行頭指示子を生成する.
     */
    public static HeadDesignator createHeadPackageCall(String definition, final String packageName) {
        return new HeadDesignator(definition) {
            @Override
            public String processLine(String line) {
                return "[" + packageName + "." + line + "]";
            }
        };
    }
    
    /**
     * ラベル定義の行頭指示子を生成する.
     */
    public static HeadDesignator createHeadLabel(String definition, char seperator) {
        //分割記号、Rosettoではスペース、KAGでは"|"
        final String sep = TextUtils.REGEX_ESCAPE_NEEDED.contains(seperator) ? 
                "\\" + seperator : "" + seperator;
        return new HeadDesignator(definition) {
            @Override
            public String processLine(String line) {
                String[] s = line.split(sep);
                String name = s[0];
                
                if(s.length == 1)
                    return "[label " + name + "]";
                
                //タイトル部分は分割記号を含められるようにクオートで囲んでおく
                StringBuilder b = new StringBuilder("\"");
                for(int i=1; i < s.length; i++) {
                    if(s[i].length() > 0) {
                        b.append(s[i]);
                        if(i != s.length-1) b.append(sep);
                    }
                }
                b.append("\"");
                String title = b.toString();
                return "[label " + name + " " + title + "]";
            }
        };
    }
    
    /**
     * 単純置換の行末指示子を生成する.
     */
    public static TailDesignator createTailReplace(String definition, final String replacement) {
        return new TailDesignator(definition) {
            @Override
            public String processLine(String line) {
                return replacement;
            }
        };
    }
    
    /**
     * 指定文字列を行末に書き加える行末指示子を生成する.
     */
    public static TailDesignator createTailAddition(String definition, final String add) {
        return new TailDesignator(definition) {
            @Override
            public String processLine(String line) {
                return line + add;
            }
        };
    }
}
