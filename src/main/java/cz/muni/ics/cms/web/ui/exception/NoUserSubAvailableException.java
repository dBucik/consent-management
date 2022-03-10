package cz.muni.ics.cms.web.ui.exception;

public class NoUserSubAvailableException extends Exception {

    public NoUserSubAvailableException() {
        super();
    }

    public NoUserSubAvailableException(String message) {
        super(message);
    }

    public NoUserSubAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoUserSubAvailableException(Throwable cause) {
        super(cause);
    }

    protected NoUserSubAvailableException(String message, Throwable cause,
                                          boolean enableSuppression,
                                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
