/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.observers;

import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.base.scenario.Unit;
import info.rosetto.models.text.LineProgress;

/**
 * Rosetto中のシナリオの進行を監視するためのObserver.
 * @author tohhy
 */
public interface ProgressObserver {
    
    /**
     * updateメソッドが呼ばれるたびに呼び出される
     * @param frameCount
     */
    public void updated(long frameCount);
    
    /**
     * シナリオ中のユニットが次に進められたときに呼び出される.
     * @param scenario 変更されたシナリオ
     * @param newUnit 進んだ先のユニット
     */
    public void unitChanged(Scenario scenario, Unit newUnit);
    
    /**
     * 文字送りがされたときに呼び出される.
     */
    public void charNexted(LineProgress l);
    
    /**
     * 改行されたときに呼び出される.
     */
    public void lineFeedExecuted();
    
    /**
     * 改行されたときに呼び出される.
     */
    public void pageFeedExecuted();
    
}
