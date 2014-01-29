/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package info.rosetto.exceptions;

/**
 * sealされた変数に変更を加えようとするとスローされる.
 * @author tohhy
 */
public class VariableSealedException extends RosettoException {
    private static final long serialVersionUID = -7175503997965568569L;

    public VariableSealedException() {
        super();
    }

    public VariableSealedException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public VariableSealedException(String message, Throwable cause) {
        super(message, cause);
    }

    public VariableSealedException(String message) {
        super(message);
    }

    public VariableSealedException(Throwable cause) {
        super(cause);
    }
}
