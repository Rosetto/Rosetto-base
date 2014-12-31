/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package org.ocsoft.rosetto.system.exceptions;

/**
 * RosettoValueの型変換に失敗した場合にスローされる.
 * @author tohhy
 */
public class NotConvertibleException extends RosettoException {
    private static final long serialVersionUID = 2301662577097233475L;

    public NotConvertibleException() {
        super();
    }

    public NotConvertibleException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public NotConvertibleException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotConvertibleException(String message) {
        super(message);
    }

    public NotConvertibleException(Throwable cause) {
        super(cause);
    }
}
