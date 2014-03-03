package info.rosetto.functions.base;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.logging.Level;

import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.values.RosettoFunction;
import info.rosetto.models.system.Scope;
import info.rosetto.system.RosettoLogger;
import info.rosetto.utils.base.Values;

import org.junit.Before;
import org.junit.Test;

public class ArithmeticFunctionsTest {
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
        RosettoLogger.setLevel(Level.OFF);
    }

    @Test
    public void plusTest() throws Exception {
        RosettoFunction sut = ArithmeticFunctions.plus;
        Scope s = new Scope();
        
        //整数値
        assertThat(sut.execute("1 1", s).asInt(), is(1+1));
        assertThat(sut.execute("1 2 3", s).asInt(), is(1+2+3));
        assertThat(sut.execute(Integer.MAX_VALUE + " 100", s).asLong(), 
                   is((long)Integer.MAX_VALUE + 100));
        
        //double値
        assertThat(sut.execute("1.2 3.4", s).asDouble(), is(1.2+3.4));
        assertThat(sut.execute("1 " + Math.PI + " 2.54321", s).asDouble(), is(1+Math.PI+2.54321));
        assertThat(sut.execute(Double.MAX_VALUE + " 100", s).asDouble(), 
                   is(Double.MAX_VALUE + 100));
        
        //関数展開
        assertThat(sut.execute("1 [+ 1 1] [* 1 3]", s).asInt(), is(1 + 2 + 3));
        
        //数値以外が混じるとNullが返る
        assertThat(sut.execute("1 foo 3", s), is((RosettoValue)Values.NULL));
    }
    
    @Test
    public void minusTest() throws Exception {
        RosettoFunction sut = ArithmeticFunctions.minus;
        Scope s = new Scope();
        
        //整数値
        assertThat(sut.execute("1 1", s).asInt(), is(1-1));
        assertThat(sut.execute("1 2 3", s).asInt(), is(1-2-3));
        assertThat(sut.execute(Integer.MAX_VALUE + " 100", s).asLong(), 
                   is((long)Integer.MAX_VALUE - 100));
        
        //double値
        assertThat(sut.execute("1.2 3.4", s).asDouble(), is(1.2-3.4));
        assertThat(sut.execute("1 " + Math.PI + " 2.54321", s).asDouble(), is(1-Math.PI-2.54321));
        assertThat(sut.execute(Double.MAX_VALUE + " 100", s).asDouble(), 
                   is(Double.MAX_VALUE - 100));
        
      //関数展開
        assertThat(sut.execute("1 [+ 1 1] [* 1 3]", s).asInt(), is(1 - 2 - 3));
        
        //数値以外が混じるとNullが返る
        assertThat(sut.execute("1 foo 3", s), is((RosettoValue)Values.NULL));
    }
    
    @Test
    public void multipleTest() throws Exception {
        RosettoFunction sut = ArithmeticFunctions.multiple;
        Scope s = new Scope();
        
        //整数値
        assertThat(sut.execute("1 1", s).asInt(), is(1*1));
        assertThat(sut.execute("1 2 3", s).asInt(), is(1*2*3));
        assertThat(sut.execute(Integer.MAX_VALUE + " 100", s).asLong(), 
                   is((long)Integer.MAX_VALUE * 100));
        
        //double値
        assertThat(sut.execute("1.2 3.4", s).asDouble(), is(1.2*3.4));
        assertThat(sut.execute("1 " + Math.PI + " 2.54321", s).asDouble(), is(1*Math.PI*2.54321));
        assertThat(sut.execute(Double.MAX_VALUE + " 100", s).asDouble(), 
                   is(Double.MAX_VALUE * 100));
        
      //関数展開
        assertThat(sut.execute("1 [+ 1 1] [* 1 3]", s).asInt(), is(1 * 2 * 3));
        
        //数値以外が混じるとNullが返る
        assertThat(sut.execute("1 foo 3", s), is((RosettoValue)Values.NULL));
    }
    
    @Test
    public void divisionTest() throws Exception {
        RosettoFunction sut = ArithmeticFunctions.division;
        Scope s = new Scope();
        
        //整数値
        assertThat(sut.execute("1 1", s).asInt(), is(1/1));
        assertThat(sut.execute("1 2 3", s).asInt(), is(1/2/3));
        assertThat(sut.execute(Integer.MAX_VALUE + " 100", s).asLong(), 
                   is((long)Integer.MAX_VALUE / 100));
        
        //double値
        assertThat(sut.execute("1.2 3.4", s).asDouble(), is(1.2/3.4));
        assertThat(sut.execute("1 " + Math.PI + " 2.54321", s).asDouble(), is(1/Math.PI/2.54321));
        assertThat(sut.execute(Double.MAX_VALUE + " 100", s).asDouble(), 
                   is(Double.MAX_VALUE / 100));
        
      //関数展開
        assertThat(sut.execute("1 [+ 1 1] [* 1 3]", s).asInt(), is(1 / 2 / 3));
        
        //数値以外が混じるとNullが返る
        assertThat(sut.execute("1 foo 3", s), is((RosettoValue)Values.NULL));
    }

}
