/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.system.errors;

/**
 * システムで定義される各種のエラー.
 * @author tohhy
 */
public enum RosettoError {
    E1000_GLOBAL_VAR_NOT_FOUND(1000),
    
    E7000_NULL_ACTION_CALLED(7000);
    ;
    private final int errorCode;
    
    private RosettoError(int errorCode) {
        this.errorCode = errorCode;
    }
    
    @Override
    public String toString() {
        return String.valueOf(errorCode);
    }

    public int getErrorCode() {
        return errorCode;
    }

}
