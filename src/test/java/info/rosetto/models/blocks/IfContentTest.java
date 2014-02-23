package info.rosetto.models.blocks;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.blocks.IfBranch;
import info.rosetto.models.base.blocks.IfContent;
import info.rosetto.models.base.scenario.Scenario;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class IfContentTest {
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
    }

    @Test
    public void インスタンス化テスト() throws Exception {
        
        //listがnullだとエラー
        try {
            new IfContent(null, "");
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        
        //elseはnullでも可
        List<IfBranch> list = new ArrayList<IfBranch>();
        new IfContent(list, null);
    }
    
    
    @Test
    public void getTrueContentで正しく評価できる() throws Exception {
        List<IfBranch> list1 = new ArrayList<IfBranch>();
        list1.add(new IfBranch("true", "foo"));
        IfContent sut1 = new IfContent(list1, "bar");
        assertThat(sut1.getTrueBranch(), is("foo"));
        
        List<IfBranch> list2 = new ArrayList<IfBranch>();
        list2.add(new IfBranch("false", "foo"));
        IfContent sut2 = new IfContent(list2, "bar");
        assertThat(sut2.getTrueBranch(), is("bar"));
    }
    
    @Test
    public void createでシナリオ化できる() throws Exception {
        List<IfBranch> list1 = new ArrayList<IfBranch>();
        list1.add(new IfBranch("true", "foo[br]bar[p]baz"));
        IfContent sut1 = new IfContent(list1, null);
        Scenario s = sut1.create();
        assertThat(s.getLength(), is(3));
        assertThat(s.getUnitAt(0).getContent(), is("foo"));
        assertThat(s.getUnitAt(1).getContent(), is("bar"));
        assertThat(s.getUnitAt(2).getContent(), is("baz"));
    }

}
