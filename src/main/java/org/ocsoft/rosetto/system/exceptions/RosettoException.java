/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.system.exceptions;

/**
 * Rosetto中の例外の基底クラス.<br>
 * Rosetto中の例外は基本的に全てこのクラスを継承し、非チェック例外にする.
 * @author tohhy
 */
public class RosettoException extends RuntimeException {
    private static final long serialVersionUID = 4570296796102725165L;

    public RosettoException() {
        super();
    }

    public RosettoException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RosettoException(String message, Throwable cause) {
        super(message, cause);
    }

    public RosettoException(String message) {
        super(message);
    }

    public RosettoException(Throwable cause) {
        super(cause);
    }
}
