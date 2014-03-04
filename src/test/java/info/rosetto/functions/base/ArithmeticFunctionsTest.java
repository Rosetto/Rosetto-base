package info.rosetto.functions.base;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import info.rosetto.contexts.base.Contexts;
import info.rosetto.models.base.elements.RosettoValue;
import info.rosetto.models.base.elements.values.RosettoFunction;
import info.rosetto.models.system.Scope;
import info.rosetto.system.RosettoLogger;
import info.rosetto.utils.base.Values;

import java.util.logging.Level;

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
    
    @Test
    public void modTest() throws Exception {
        RosettoFunction sut = ArithmeticFunctions.mod;
        Scope s = new Scope();
        assertThat(sut.execute("3 2", s).asInt(), is(1));
        assertThat(sut.execute("10 8", s).asString(), is("2"));
        assertThat(sut.execute("13 5", s).asDouble(), is(3.0));
        assertThat(sut.execute("67 7", s).asLong(), is(4L));
    }
    
    @Test
    public void eqTest() throws Exception {
        RosettoFunction sut = ArithmeticFunctions.eq;
        Scope s = new Scope();
        assertThat(sut.execute("1 1", s).asBool(), is(true));
        assertThat(sut.execute("1.5 1.5", s).asBool(), is(true));
        assertThat(sut.execute("0 0.00", s).asBool(), is(true));
        assertThat(sut.execute("true true", s).asBool(), is(true));
        assertThat(sut.execute("2.0 [+ 1 1]", s).asBool(), is(true));
        assertThat(sut.execute("foo foo", s).asBool(), is(true));
        
        assertThat(sut.execute("1 2", s).asBool(), is(false));
        assertThat(sut.execute("1.0 1.000001", s).asBool(), is(false));
        assertThat(sut.execute("3 3.14", s).asBool(), is(false));
        assertThat(sut.execute("true false", s).asBool(), is(false));
        assertThat(sut.execute("1 one", s).asBool(), is(false));
    }
    
    @Test
    public void ltgtTest() throws Exception {
        RosettoFunction lt = ArithmeticFunctions.lt;
        RosettoFunction gt = ArithmeticFunctions.gt;
        Scope s = new Scope();
        assertThat(lt.execute("1 2", s).asBool(), is(true));
        assertThat(lt.execute("-2.8 -1.5", s).asBool(), is(true));
        assertThat(lt.execute("-5 -10", s).asBool(), is(false));
        assertThat(lt.execute("10.00 3.14", s).asBool(), is(false));
        
        assertThat(lt.execute("100 100", s).asBool(), is(false));
        assertThat(lt.execute("1.0 1.000", s).asBool(), is(false));
        
        assertThat(gt.execute("1 2", s).asBool(), is(false));
        assertThat(gt.execute("-2.8 -1.5", s).asBool(), is(false));
        assertThat(gt.execute("-5 -10", s).asBool(), is(true));
        assertThat(gt.execute("10.00 3.14", s).asBool(), is(true));
        
        assertThat(gt.execute("100 100", s).asBool(), is(false));
        assertThat(gt.execute("1.0 1.000", s).asBool(), is(false));
        
    }
    
    @Test
    public void leqgeqTest() throws Exception {
        RosettoFunction leq = ArithmeticFunctions.leq;
        RosettoFunction geq = ArithmeticFunctions.geq;
        Scope s = new Scope();
        assertThat(leq.execute("1 2", s).asBool(), is(true));
        assertThat(leq.execute("-2.8 -1.5", s).asBool(), is(true));
        assertThat(leq.execute("-5 -10", s).asBool(), is(false));
        assertThat(leq.execute("10.00 3.14", s).asBool(), is(false));
        
        assertThat(leq.execute("100 100", s).asBool(), is(true));
        assertThat(leq.execute("1.0 1.000", s).asBool(), is(true));
        
        assertThat(geq.execute("1 2", s).asBool(), is(false));
        assertThat(geq.execute("-2.8 -1.5", s).asBool(), is(false));
        assertThat(geq.execute("-5 -10", s).asBool(), is(true));
        assertThat(geq.execute("10.00 3.14", s).asBool(), is(true));
        
        assertThat(geq.execute("100 100", s).asBool(), is(true));
        assertThat(geq.execute("1.0 1.000", s).asBool(), is(true));
    }
}
