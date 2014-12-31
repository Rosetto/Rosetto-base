/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.utils.base;

import org.ocsoft.rosetto.contexts.base.Rosetto;

/**
 * フレーム数と時間の処理に関わるユーティリティクラス.
 * @author tohhy
 */
public class TimeUtils {
    /**
     * ミリ秒を現在のFPSを使ってフレーム数に変換する.
     * @param timeMs
     * @return
     */
    public static int timeMsToFrames(int timeMs) {
        double fps = Rosetto.get("settings.targetFPS").asDouble(50.0);
        return (int) ((fps * timeMs) / 1000);
    }
    
    /**
     * フレーム数を現在のFPSを使ってミリ秒に変換する.
     * @param frames
     * @return
     */
    public static int framesToTimeMs(int frames) {
        double fps = Rosetto.get("settings.targetFPS").asDouble(50.0);
        return (int) (((double)frames / fps) * 1000);
    }

}
