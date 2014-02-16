package info.rosetto.models.base.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.utils.base.Values;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("serial")
public class ExpandedArgumentsTest {
    
    private static RosettoFunction plus = new RosettoFunction("+",
            "a", "b") {
        
        @Override
        protected RosettoValue run(ExpandedArguments args) {
            return Values.create(args.get("a").asInt() + args.get("b").asInt());
        }
    };
    
    private static RosettoFunction func1 = new RosettoFunction("func1",
            "x", "y") {
        
        @Override
        protected RosettoValue run(ExpandedArguments args) {
            return null;
        }
    };
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
        Contexts.defineFunction(plus);
    }

    @Test
    public void 関数呼び出し引数の展開() throws Exception {
         ExpandedArguments sut1 = new ExpandedArguments(new RosettoArguments("100 [+ 1 3]"), func1);
         assertThat(sut1.get("x").asString(), is("100"));
         assertThat(sut1.get("y").asString(), is("4"));
         ExpandedArguments sut2 = new ExpandedArguments(new RosettoArguments("[+ 1 [+ 2 4]] [+ 1 [+ 1 [+ 3 5]]]"), func1);
         assertThat(sut2.get("x").asString(), is("7"));
         assertThat(sut2.get("y").asString(), is("10"));
    }

}
