package info.rosetto.utils.base;

import info.rosetto.contexts.base.Contexts;

public class TimeUtils {
    
    /**
     * ミリ秒を現在のFPSを使ってフレーム数に変換する.
     * @param timeMs
     * @return
     */
    public static int timeMsToFrames(int timeMs) {
        double fps = Contexts.get("settings.targetFPS").asDouble(50.0);
        return (int) ((fps * timeMs) / 1000);
    }
    
    /**
     * フレーム数を現在のFPSを使ってミリ秒に変換する.
     * @param frames
     * @return
     */
    public static int framesToTimeMs(int frames) {
        double fps = Contexts.get("settings.targetFPS").asDouble(50.0);
        return (int) (((double)frames / fps) * 1000);
    }

}
