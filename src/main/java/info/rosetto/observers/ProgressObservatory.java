/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.observers;

import info.rosetto.models.base.scenario.Scenario;
import info.rosetto.models.base.scenario.Unit;
import info.rosetto.models.text.LineProgress;

import org.frows.observatories.Observatory;

/**
 * Rosetto中で保持されているシナリオオブザーバーのリスト.
 * シナリオの進行（ユニット送り）を検知し、それぞれのオブザーバに通知する.
 * 全てのシナリオはこのクラスの唯一のインスタンスに進行通知を送信する.
 * @author tohhy
 */
public final class ProgressObservatory extends Observatory<ProgressObserver> 
    implements ProgressObserver {
    
    ProgressObservatory() {}
    
    
    /**
     * 全てのオブザーバーにユニットの進行を通知する.
     * @param page ユニットが進行したページ
     * @param next 進行先のユニット
     */
    public void unitChanged(Scenario scenario, Unit next) {
        for(ProgressObserver l : getObservers())
            l.unitChanged(scenario, next);
    }

    @Override
    public void charNexted(LineProgress line) {
        for(ProgressObserver l : getObservers())
            l.charNexted(line);
    }

    @Override
    public void lineFeedExecuted() {
        for(ProgressObserver l : getObservers())
            l.lineFeedExecuted();
    }

    @Override
    public void pageFeedExecuted() {
        for(ProgressObserver l : getObservers())
            l.pageFeedExecuted();
    }

    @Override
    public void updated(long frameCount) {
        for(ProgressObserver l : getObservers())
            l.updated(frameCount);
    }

}
