package info.rosetto.parsers;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.parsers.StdDesignators;

import org.frows.lilex.designator.Designator;
import org.junit.Test;

public class StdDesignatorsTest {

    @Test
    public void 行中コメント指示子() throws Exception {
        Designator sut = StdDesignators.createMiddleComment(";");
        String s1 = sut.process("コメントがないテキストはそのまま");
        assertThat(s1, is("コメントがないテキストはそのまま"));
        String s2 = sut.process("；同じセミコロンでも大文字なのでそのまま");
        assertThat(s2, is("；同じセミコロンでも大文字なのでそのまま"));
        String s3 = sut.process(";行頭コメント");
        assertThat(s3, is(""));
        String s4 = sut.process("ここまで文章;行中コメント");
        assertThat(s4, is("ここまで文章"));
        String s5 = sut.process("コメント;記号が;複数;;;あったら最初の記号まで;;;");
        assertThat(s5, is("コメント"));
    }
    
    
    @Test
    public void 行中置換指示子() throws Exception {
        Designator sut = StdDesignators.createMiddleReplace("@", "[w]", 1000);
        String s1 = sut.process("@");
        assertThat(s1, is("[w]"));
        String s2 = sut.process("行中の@指示子");
        assertThat(s2, is("行中の[w]指示子"));
        String s3 = sut.process("@行頭行末の指示子@");
        assertThat(s3, is("[w]行頭行末の指示子[w]"));
        String s4 = sut.process("A@B@C@D@E@F@G");
        assertThat(s4, is("A[w]B[w]C[w]D[w]E[w]F[w]G"));
    }
    
    
    @Test
    public void 行頭ラベル指示子() throws Exception {
        Designator sut = StdDesignators.createHeadLabel("*", ' ');
        String s1 = sut.process("*labelname title");
        assertThat(s1, is("[label labelname \"title\"]"));
        String s2 = sut.process("*ラベル名のみ");
        assertThat(s2, is("[label ラベル名のみ]"));
        String s3 = sut.process("*ラベルタイトルにスペースを含む場合 This is a pen.");
        assertThat(s3, is("[label ラベルタイトルにスペースを含む場合 \"This is a pen.\"]"));
        String s4 = sut.process("*スペースが     たくさん      ある");
        assertThat(s4, is("[label スペースが \"たくさん ある\"]"));
        String s5 = sut.process("*start 始まり");
        assertThat(s5, is("[label start \"始まり\"]"));
        
        
        //KAG式
        Designator sut2 = StdDesignators.createHeadLabel("*", '|');
        String s21 = sut2.process("*labelname|title");
        assertThat(s21, is("[label labelname \"title\"]"));
        
    }
    
    @Test
    public void 行頭置換指示子() throws Exception {
        Designator sut = StdDesignators.createHeadReplace("---", "[p]");
        String s1 = sut.process("---");
        assertThat(s1, is("[p]"));
        String s2 = sut.process("-----------最初さえ合っていればOK----");
        assertThat(s2, is("[p]"));
        String s3 = sut.process(" ---行頭に別の文字があれば置き換えない---");
        assertThat(s3, is(" ---行頭に別の文字があれば置き換えない---"));
    }
    
    @Test
    public void 行頭タグ変換指示子() throws Exception {
        Designator sut = StdDesignators.createHeadConvertTag("@", "character.select");
        String s1 = sut.process("@foo");
        assertThat(s1, is("[character.select foo]"));
        String s2 = sut.process("@@foo@");
        assertThat(s2, is("[character.select @foo@]"));
        String s3 = sut.process(" @first spaced");
        assertThat(s3, is(" @first spaced"));
        String s4 = sut.process("@middle spaced");
        assertThat(s4, is("[character.select middle spaced]"));
    }
    
    
    @Test
    public void パッケージ呼び出し指示子() throws Exception {
        Designator sut = StdDesignators.createHeadPackageCall("#", "character");
        String s1 = sut.process("#show");
        assertThat(s1, is("[character.show]"));
        String s2 = sut.process("#font color=#fff family=Helvetica");
        assertThat(s2, is("[character.font color=#fff family=Helvetica]"));
    }

}
