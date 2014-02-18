/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/.
 */

package info.rosetto.parsers;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.base.scenario.Scenario.ScenarioType;
import info.rosetto.models.base.scenario.Unit;
import info.rosetto.models.base.values.ActionCall;
import info.rosetto.models.base.values.ListValue;
import info.rosetto.models.base.values.RosettoAction;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.models.state.parser.ArgumentSyntax;
import info.rosetto.models.state.parser.Parser;
import info.rosetto.parsers.rosetto.RosettoTagParser;
import info.rosetto.utils.base.ParserUtils;
import info.rosetto.utils.base.TextUtils;
import info.rosetto.utils.base.Values;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.frows.lilex.parser.Tokenizer;
import org.frows.lilex.token.Token;

/**
 * パーサーの抽象クラス.
 * ScenarioParser.parse()から設定ファイルに合わせたサブクラスで初期化される.
 * @author tohhy
 */
public class ScenarioParser extends Tokenizer implements Parser {
    /**
     * 正規記法のタグ全体にマッチする正規表現
     */
    protected static final String ALL_TAG = TextUtils.createTagRegex("[", "]");
    
    /**
     * 正規記法のタグを含むかどうかを判定する正規表現
     */
    protected static final String TAG_CONTAINS_REGEX = ".*?(\\[(.*?)\\]).*";
    
    /**
     * スクリプトの正規化を行うノーマライザ.
     */
    private final AbstractNormalizer normalizer;
    
    /**
     * 文法ごとのタグを解釈しRosettoのアクションに変更するタグパーサー.
     */
    private final AbstractTagParser tagParser;


    /**
     * パーサーを初期化する.
     * スクリプトのパースの際には、引数に与えられたノーマライザとタグパーサーを用いる.
     * @param normalizer スクリプトの正規化を行うノーマライザ
     * @param tagParser 文法ごとのタグを解釈しRosettoのアクションに変更するタグパーサー
     */
    public ScenarioParser(AbstractNormalizer normalizer, AbstractTagParser tagParser) {
        if(normalizer == null) throw new IllegalArgumentException("normalizerがnullです");
        if(tagParser == null) throw new IllegalArgumentException("tagparserがnullです");
        this.normalizer = normalizer;
        this.tagParser = tagParser;
    }
    
    /**
     * パーサーを初期化する.
     * スクリプトのパースの際には、引数に与えられたノーマライザとデフォルトのタグパーサーを用いる.
     * @param normalizer スクリプトの正規化を行うノーマライザ
     */
    public ScenarioParser(AbstractNormalizer normalizer) {
        this(normalizer, new RosettoTagParser());
    }
    
    /**
     * 指定した文字列をRosettoValueに変換する.
     * @param element
     * @return
     */
    public RosettoValue parseElement(String element) {
        if(element.startsWith("[") && element.endsWith("]")) {
            return tagParser.parseTag(element);
        } else if(element.startsWith("(") && element.endsWith(")")) {
            return new ListValue(element.substring(1, element.length()-1).split(" "));
        } else if(element.startsWith("@")) {
            return new ActionCall("getlocal", element.substring(1));
        } else if(element.startsWith("$")) {
            return new ActionCall("getglobal", element.substring(1));
        }
        return Values.create(element);
    }
    
    /**
     * 文字列表現のシナリオを解釈してシナリオオブジェクトを作成する.
     * @param scenarioText 文字列表現のシナリオ
     * @return パース後のシナリオ
     */
    public Scenario parseScript(String scenarioText) {
        List<String> scenario = asLines(scenarioText);
        return parseScript(scenario);
    }
    

    /**
     * 行ごとに分割されたシナリオスクリプトをパースして通常シナリオを返す.
     * パースのメイン処理.<br>
     * normalizer.normalizeでテキストと角括弧形式のタグのみに正規化、
     * tokenizeでトークンに分割の順で操作が行われる.
     * @param scenarioLines 行ごとに分割されたシナリオスクリプト
     * @return パース後のシナリオ
     */
    public Scenario parseScript(List<String> scenarioLines) {
        //テキストと角括弧形式のタグのみに正規化
        List<String> normalized = normalizer.normalize(scenarioLines);
        //トークンのリストを作成
        List<? extends Token> tokens = tokenize(normalized);
        //シナリオ作成
        return new Scenario(tokens, ScenarioType.NORMAL);
    }

    @Override
    public ArgumentSyntax getArgumentSyntax() {
        return Contexts.getParser().getArgumentSyntax();
    }

    /**
     * プレーンテキストとタグが１つずつ組になったテキストを受け取り、ユニットを生成して返す.
     * unitStrは<br>
     * <code>
     * 吾輩は猫である。[foo bar=baz]<br>
     * </code>
     * のような形で渡される.これから単一のユニットを生成して返す.
     */
    protected Unit createUnit(String unitStr, ParserState ps) {
        //タグとテキストに分割
        int obIndex = unitStr.indexOf('[');
        String tag = (obIndex == -1) ? null : unitStr.substring(obIndex);
        String text = (obIndex == -1) ? unitStr : unitStr.substring(0, obIndex);
        ActionCall action = tagParser.parseTag(tag);
        return execUnit(new Unit(text, action), ps);
    }
    
    
    /**
     * テキスト+タグの形式のみに正規化された文字列を受け取り、UnitのListに変換して返す.
     * @param normalized 正規化された文字列をすべて結合したもの
     * @return
     */
    @Override
    public List<ScriptToken> tokenize(String normalized) {
        ParserState ps = new ParserState();
        for(String unitStr : ParserUtils.splitScript(normalized)) {
            //テキストをコンパイルしてユニットにする
            Unit u = createUnit(unitStr, ps);
            //ユニットを追加
            ps.addUnit(u);
        }
        return ps.getTokens();
    }
    
    /**
     * ユニットがパース時に実行する関数を持っていれば実行する.
     * @param u
     */
    private Unit execUnit(Unit u, ParserState ps) {
        //パース状態を編集
        ps.setCurrentUnit(u);
        
        //変換処理
        ActionCall action = u.getAction();
        String func = action.getFunctionName();
        RosettoAction f = info.rosetto.contexts.base.Contexts.getAction(func);
        if(f != null) {
//            return f.execOnParse(u);
        }
        return u;
    }

    /**
     * 複数行からなる文字列を行ごとのリストへ変換する.
     * @param scenarioText 
     * @return 
     */
    private List<String> asLines(String scenarioText) {
        //行を読み出す
        Scanner scanner = null;
        try {
            scanner = new Scanner(scenarioText);
            List<String> lines = new LinkedList<String>();
            while(scanner.hasNext()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        } finally {
            if(scanner != null)
                scanner.close();
        }
    }

}
