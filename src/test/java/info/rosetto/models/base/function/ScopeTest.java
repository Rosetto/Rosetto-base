package info.rosetto.models.base.function;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.values.RosettoValue;
import info.rosetto.models.state.variables.Scope;
import info.rosetto.utils.base.Values;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("serial")
public class ScopeTest {
    
    private static RosettoFunction plus = new RosettoFunction("+",
            "a", "b") {
        
        @Override
        protected RosettoValue run(Scope args) {
            return Values.create(args.get("a").asInt() + args.get("b").asInt());
        }
    };
    
    private static RosettoFunction func1 = new RosettoFunction("func1",
            "x", "y") {
        
        @Override
        protected RosettoValue run(Scope args) {
            return null;
        }
    };
    
    private Scope testScope;
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
        Contexts.defineFunction(plus);
        testScope = new Scope();
    }

    @Test
    public void 関数呼び出し引数の展開() throws Exception {
         Scope sut1 = new Scope(new RosettoArguments("100 [+ 1 3]"), func1, testScope);
         assertThat(sut1.get("x").asString(), is("100"));
         assertThat(sut1.get("y").asString(), is("4"));
         Scope sut2 = new Scope(new RosettoArguments("[+ 1 [+ 2 4]] [+ 1 [+ 1 [+ 3 5]]]"), 
                 func1, testScope);
         assertThat(sut2.get("x").asString(), is("7"));
         assertThat(sut2.get("y").asString(), is("10"));
    }

}