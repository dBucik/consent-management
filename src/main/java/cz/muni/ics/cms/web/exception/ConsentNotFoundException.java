package cz.muni.ics.cms.web.exception;

public class ConsentNotFoundException extends Exception {

    public ConsentNotFoundException(String message) {
        super(message);
    }

    public ConsentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsentNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ConsentNotFoundException(String message, Throwable cause,
                                       boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
