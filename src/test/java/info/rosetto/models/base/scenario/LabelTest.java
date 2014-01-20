package info.rosetto.models.base.scenario;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import info.rosetto.models.base.scenario.Label;

import org.junit.Test;

/**
 * OK(2013/09/09)
 * @author tohhy
 */
public class LabelTest {

    @Test
    public void インスタンス化テスト() {
        Label sut1 = new Label("test", 2, "TestTitle");
        assertThat(sut1, is(notNullValue()));
        assertThat(sut1.getName(), is("test"));
        assertThat(sut1.getIndex(), is(2));
        assertThat(sut1.getTitle(), is("TestTitle"));
        //タイトルを指定しないと空文字
        Label sut2 = new Label("test", 2);
        assertThat(sut2.getTitle(), is(""));
    }
    
    
    @Test
    public void nullや空のidで初期化するとエラー() throws Exception {
        try {
            new Label(null, 1);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        try {
            new Label("", 1);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test
    public void 負のインデックスで初期化するとエラー() throws Exception {
        try {
            new Label("test", -2);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
        try {
            new Label("test", -100);
            fail();
        } catch(Exception e) {
            assertThat(e, instanceOf(IllegalArgumentException.class));
        }
    }

}
