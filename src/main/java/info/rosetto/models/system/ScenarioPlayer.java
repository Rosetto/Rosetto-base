/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.models.system;

import info.rosetto.models.base.scenario.Scenario;

/**
 * Rosettoを再生するプレイヤーのモデル.
 * @author tohhy
 */
public interface ScenarioPlayer {
    
    /**
     * シナリオスタックに新しくシナリオをプッシュする.
     * @param playingScope シナリオの再生時に参照するスコープ
     */
    public void pushScenario(Scenario scenario, Scope playingScope);

}
