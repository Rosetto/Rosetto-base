package org.ocsoft.rosetto.parsers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.ocsoft.rosetto.contexts.base.Rosetto;
import org.ocsoft.rosetto.parsers.ParseUtils;

public class ParserUtilsTest {

    @Before
    public void setUp() {
        Rosetto.dispose();
        Rosetto.initialize();
    }
    
    @Test
    public void 単純なスクリプトのパース() throws Exception {
         List<String> sut1 = ParseUtils.splitScript("");
         assertThat(sut1.size(), is(0));
         
         List<String> sut2 = ParseUtils.splitScript("hoge");
         assertThat(sut2.size(), is(1));
         assertThat(sut2.get(0), is("hoge"));
         
         List<String> sut3 = ParseUtils.splitScript("hoge[br]fuga[p]");
         assertThat(sut3.size(), is(2));
         assertThat(sut3.get(0), is("hoge[br]"));
         assertThat(sut3.get(1), is("fuga[p]"));
    }
    
    @Test
    public void ネストした関数呼び出しのパース() throws Exception {
        List<String> sut1 = ParseUtils.splitScript("[print [getg x]]");
        assertThat(sut1.size(), is(1));
        assertThat(sut1.get(0), is("[print [getg x]]"));
        
        List<String> sut2 = ParseUtils.splitScript(
                "[print [getg x]]test[foo (bar baz)][a [b] [c] [d [e]]][print \"te st\"]");
        assertThat(sut2.size(), is(4));
        assertThat(sut2.get(0), is("[print [getg x]]"));
        assertThat(sut2.get(1), is("test[foo (bar baz)]"));
        assertThat(sut2.get(2), is("[a [b] [c] [d [e]]]"));
        assertThat(sut2.get(3), is("[print \"te st\"]"));
        
        List<String> sut3 = ParseUtils.splitScript(
                "[use \"functional\"]"
                + "[println [map ("
                + "[fn (x) [cond "
                + "[eq [rem x 15] 0] fizzbuzz"
                + "[eq [rem x 5]  0] buzz"
                + "[eq [rem x 3]  0] fizz"
                + "true x"
                + "]]"
                + "[range 1 101]"
                + ")"
                + "]"
                + "]");
        assertThat(sut3.size(), is(2));
    }
    
    

    @Test
    public void removeTabsでタブを取り除ける() {
        String s1 = ParseUtils.removeTabs("ここからタブ	ここまでタブ");
        assertThat(s1, is("ここからタブここまでタブ"));
        String s2 = ParseUtils.removeTabs("		複\t\t\t数		のタブを含	む文\t字	\t	列		");
        assertThat(s2, is("複数のタブを含む文字列"));
    }
    
    @Test
    public void addSquareBracketで行を角括弧で囲める() throws Exception {
        String s1 = ParseUtils.addSquareBracket("p");
        assertThat(s1, is("[p]"));
        String s2 = ParseUtils.addSquareBracket("wait 1000");
        assertThat(s2, is("[wait 1000]"));
        String s3 = ParseUtils.addSquareBracket("title text=\"テスト\"");
        assertThat(s3, is("[title text=\"テスト\"]"));
        String s4 = ParseUtils.addSquareBracket("@goto firstlabel".substring(1));
        assertThat(s4, is("[goto firstlabel]"));
    }
    
    @Test
    public void hasUnClosedBracketで閉じられていない角括弧を検出できる() throws Exception {
        boolean s1 = ParseUtils.hasUnClosedBracket("[閉じられていない括弧");
        assertThat(s1, is(true));
        boolean s2 = ParseUtils.hasUnClosedBracket("[閉じられている括弧]");
        assertThat(s2, is(false));
        boolean s3 = ParseUtils.hasUnClosedBracket("括弧がない");
        assertThat(s3, is(false));
        boolean s4 = ParseUtils.hasUnClosedBracket("[print text=\"クオートを含み、次の行へ続く\"");
        assertThat(s4, is(true));
        boolean s5 = ParseUtils.hasUnClosedBracket("[括弧が][たくさん][あっても][対応していればfalse]");
        assertThat(s5, is(false));
        boolean s6 = ParseUtils.hasUnClosedBracket("[括弧の中に\"クオートで[囲まれた[左括弧があっても[無視されるので[\"false]");
        assertThat(s6, is(false));
    }
    
    @Test
    public void removeAllTagsで全てのタグを除去できる() throws Exception {
        String s1 = ParseUtils.removeAllTags("タグ除去テスト[p]");
        assertThat(s1, is("タグ除去テスト"));
        String s2 = ParseUtils.removeAllTags("[wait 1000]");
        assertThat(s2, is(""));
        String s3 = ParseUtils.removeAllTags("[]te[title text=\"テスト\"]st[]");
        assertThat(s3, is("test"));
        String s4 = ParseUtils.removeAllTags("te[][][][][]st");
        assertThat(s4, is("test"));
        //括弧のネストは想定していないので一つのタグとみなされる
        String s5 = ParseUtils.removeAllTags("te[[[[[[]st");
        assertThat(s5, is("test"));
        //括弧の中身はまとめて除去される
        String s6 = ParseUtils.removeAllTags("][][");
        assertThat(s6, is("]["));
        String s7 = ParseUtils.removeAllTags("[eval exp=\"foo[3] = 10;\"]test[br]");
        assertThat(s7, is("test"));
        String s8 = ParseUtils.removeAllTags("[print text=\"途中で終わり、次の行へ続くタグ\"");
        assertThat(s8, is("[print text=\"途中で終わり、次の行へ続くタグ\""));
    }
    

    @Test
    public void isTagOnlyLineでタグのみの行を判別できる() throws Exception {
        boolean s1 = ParseUtils.isTagOnlyLine("[p]");
        assertThat(s1, is(true));
        boolean s2 = ParseUtils.isTagOnlyLine("[p][br][print=test]");
        assertThat(s2, is(true));
        boolean s3 = ParseUtils.isTagOnlyLine("test");
        assertThat(s3, is(false));
        boolean s4 = ParseUtils.isTagOnlyLine("[br]test[eval exp=\"test[100]='hoge';\"]");
        assertThat(s4, is(false));
    }
    

    @Test
    public void toTagNameでタグをタグ名に変換できる() throws Exception {
        assertThat(ParseUtils.toTagName("[hoge]"), is("hoge"));
        assertThat(ParseUtils.toTagName("   [br]   "), is("br"));
        assertThat(ParseUtils.toTagName("[    system.gc    ]"), is("system.gc"));
        assertThat(ParseUtils.toTagName("[print text=\"foo\"]"), is("print"));
        assertThat(ParseUtils.toTagName("print text=hoge"), is("print"));
        assertThat(ParseUtils.toTagName("eval \"hoge=huga\""), is("eval"));
    }

    @Test
    public void removeBracketで再外周の角括弧を外せる() {
        //通常の角括弧は外せる
        assertThat(ParseUtils.removeSBracket("[hoge]"), is("hoge"));
        //外周のスペースは無視
        assertThat(ParseUtils.removeSBracket("    [hoge]    "), is("hoge"));
        //角括弧がなければそのまま
        assertThat(ParseUtils.removeSBracket("hoge"), is("hoge"));
        //括弧が片方ならそのまま
        assertThat(ParseUtils.removeSBracket("[hoge"), is("[hoge"));
        assertThat(ParseUtils.removeSBracket("hoge]"), is("hoge]"));
        //二重になっていても１つしか外さない（仕様上タグに括弧の多重はない、あってもタグ中のスクリプトなどで外す必要が無い）
        assertThat(ParseUtils.removeSBracket("[[hoge]]"), is("[hoge]"));
        assertThat(ParseUtils.removeSBracket("[eval \"a[10] = 100;\"]"), is("eval \"a[10] = 100;\""));
        //文中の角括弧はそのまま
        assertThat(ParseUtils.removeSBracket("h[]o[]g[]e"), is("h[]o[]g[]e"));
    }

}
