package info.rosetto.exceptions;

public class NotConvertibleException extends Exception {
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
