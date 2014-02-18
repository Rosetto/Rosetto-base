/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.parsers.rosetto;

import info.rosetto.models.base.values.ActionCall;
import info.rosetto.parsers.AbstractTagParser;

/**
 * RosettoScriptのアクション呼び出しを解釈するパーサー.
 * @author tohhy
 */
public class RosettoTagParser extends AbstractTagParser {
    
    @Override
    public ActionCall convertTag(String tagName, String args) {
        return new ActionCall(tagName, args);
    }
    
}
