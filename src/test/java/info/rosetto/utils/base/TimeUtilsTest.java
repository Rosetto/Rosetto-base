package info.rosetto.utils.base;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import info.rosetto.contexts.base.Contexts;

import org.junit.Before;
import org.junit.Test;

public class TimeUtilsTest {
    
    @Before
    public void setUp() {
        Contexts.dispose();
        Contexts.initialize();
        //50FPSとする
        Contexts.define("settings.targetFPS", 50.0);
    }
    
    @Test
    public void timeMsToFramesTest() {
        //50FPS : 1000ms = 50Frame
        assertThat(TimeUtils.timeMsToFrames(1000), is(50));
    }
    
    @Test
    public void testName() throws Exception {
        //50FPS : 50Frame = 1000ms
        assertThat(TimeUtils.framesToTimeMs(50), is(1000));
    }

}
