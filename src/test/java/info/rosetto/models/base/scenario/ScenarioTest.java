package info.rosetto.models.base.scenario;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Test;

@SuppressWarnings("serial")
public class ScenarioTest {

    @Test
    public void constructorTest() throws Exception {
         Scenario sut1 = new Scenario();
         assertThat(sut1.getLength(), is(0));
         
         Scenario sut2 = new Scenario(new Unit("foo"));
         assertThat(sut2.getLength(), is(1));
         
        Scenario sut3 = new Scenario(new ArrayList<ScenarioToken>(){
             {this.add(new Unit("bar"));this.add(new Label("baz", 1));}});
         assertThat(sut3.getLength(), is(1));
         
         Scenario sut4 = new Scenario(
                 new ArrayList<Unit>() {{this.add(new Unit("hoge"));}}, 
                 new ArrayList<Label>() {{this.add(new Label("fuga", 0));}});
         assertThat(sut4.getLength(), is(1));
         
         Scenario sut5 = new Scenario((ScenarioToken)null);
         assertThat(sut5.getLength(), is(0));
    }

}
