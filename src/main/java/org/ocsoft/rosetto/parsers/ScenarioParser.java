/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.parsers;

import java.util.List;
import java.util.Map;

import org.frows.lilex.parser.Tokenizer;
import org.frows.lilex.token.Token;
import org.ocsoft.rosetto.functions.base.BaseFunctions;
import org.ocsoft.rosetto.models.base.elements.RosettoValue;
import org.ocsoft.rosetto.models.base.elements.values.ActionCall;
import org.ocsoft.rosetto.models.base.elements.values.ScriptValue;
import org.ocsoft.rosetto.models.base.scenario.Label;
import org.ocsoft.rosetto.models.base.scenario.Scenario;
import org.ocsoft.rosetto.models.base.scenario.ScenarioToken;
import org.ocsoft.rosetto.models.base.scenario.Unit;
import org.ocsoft.rosetto.models.system.Parser;
import org.ocsoft.rosetto.models.system.Scope;
import org.ocsoft.rosetto.parsers.rosetto.RosettoElementParser;
import org.ocsoft.rosetto.utils.base.TextUtils;

/**
 * パーサーの抽象クラス.複数の文法構造に対応可能にする.
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
    private final AbstractElementParser elementParser;


    /**
     * パーサーを初期化する.
     * スクリプトのパースの際には、引数に与えられたノーマライザとタグパーサーを用いる.
     * @param normalizer スクリプトの正規化を行うノーマライザ
     * @param tagParser 文法ごとのタグを解釈しRosettoのアクションに変更するタグパーサー
     */
    public ScenarioParser(AbstractNormalizer normalizer, AbstractElementParser tagParser) {
        if(normalizer == null) throw new IllegalArgumentException("normalizerがnullです");
        if(tagParser == null) throw new IllegalArgumentException("tagparserがnullです");
        this.normalizer = normalizer;
        this.elementParser = tagParser;
    }
    
    /**
     * パーサーを初期化する.
     * スクリプトのパースの際には、引数に与えられたノーマライザとデフォルトのタグパーサーを用いる.
     * @param normalizer スクリプトの正規化を行うノーマライザ
     */
    public ScenarioParser(AbstractNormalizer normalizer) {
        this(normalizer, new RosettoElementParser());
    }
    
    /**
     * 指定した文字列をRosettoValueに変換する.<br>
     * @param element
     * @return
     */
    public RosettoValue parseElement(String element) {
        return elementParser.parseElement(element);
    }
    
    @Override
    public List<String> splitElements(String elements) {
        return elementParser.splitElements(elements);
    }

    /**
     * 文字列表現のシナリオを解釈してシナリオオブジェクトを作成する.
     * @param scenarioText 文字列表現のシナリオ
     * @return パース後のシナリオ
     */
    public Scenario parseScript(String scenarioText) {
        List<String> scenario = ParseUtils.asLines(scenarioText);
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
        return new Scenario(tokens);
    }

    @Override
    public Scenario parseScript(ScriptValue script) {
        List<String> scenarioLines = ParseUtils.asLines(script.getScript());
        //テキストと角括弧形式のタグのみに正規化
        List<String> normalized = normalizer.normalize(scenarioLines);
        //トークンのリストを作成
        List<? extends Token> tokens = tokenize(normalized);
        //シナリオ作成
        //TODO スコープ継承
        return new Scenario(tokens);
    }
    
    /**
     * プレーンテキストとタグが１つずつ組になったテキストを受け取り、ユニットを生成して返す.
     * unitStrは<br>
     * <code>
     * 吾輩は猫である。[foo bar=baz]<br>
     * </code>
     * のような形で渡される.これから単一のユニットを生成して返す.
     */
    protected Unit createUnit(String unitStr) {
        //タグとテキストに分割
        int obIndex = unitStr.indexOf('[');
        String tag = (obIndex == -1) ? "[pass]" : unitStr.substring(obIndex);
        String text = (obIndex == -1) ? unitStr : unitStr.substring(0, obIndex);
        ActionCall action = (ActionCall)elementParser.parseElement(tag);
        return new Unit(text, action);
    }
    
    /**
     * テキスト+タグの形式のみに正規化された文字列を受け取り、UnitのListに変換して返す.
     * @param normalized 正規化された文字列をすべて結合したもの
     * @return
     */
    @Override
    public List<ScenarioToken> tokenize(String normalized) {
        ParserState ps = new ParserState();
        for(String unitStr : ParseUtils.splitScript(normalized)) {
            //テキストをコンパイルしてユニットにする
            Unit u = createUnit(unitStr);
            //ユニットを追加
            ps.addUnit(u);
            //ユニットがラベルならラベルも追加（パース時唯一の例外的処理）
            if(u.getAction().getActionName().equals("label")) {
                ps.addLabel(createLabel(ps, u.getAction()));
            }
        }
        return ps.getTokens();
    }
    
    private static Label createLabel(ParserState ps, ActionCall labelAction) {
        Map<String, RosettoValue> args = 
                labelAction.getArgs().bind(BaseFunctions.label, new Scope());
        
        String labelName = args.get("name").asString();
        String labelTitle = args.get("title").asString();
        return new Label(labelName,  ps.getUnitIndex(), labelTitle);
    }
    
}
