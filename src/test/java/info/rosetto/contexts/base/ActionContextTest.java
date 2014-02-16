package info.rosetto.contexts.base;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.models.base.values.RosettoAction;
import info.rosetto.utils.base.Values;

import org.junit.Test;

public class ActionContextTest {
    

    @Test
    public void getAndDefineTest() throws Exception {
         ActionContext sut = new ActionContext();
         assertThat(sut.get("foo"), is((RosettoAction)Values.NULL));
    }

}
