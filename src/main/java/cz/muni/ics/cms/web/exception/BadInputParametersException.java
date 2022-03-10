package cz.muni.ics.cms.web.exception;

public class BadInputParametersException extends Exception {

    public BadInputParametersException() {
        super();
    }

    public BadInputParametersException(String message) {
        super(message);
    }

    public BadInputParametersException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadInputParametersException(Throwable cause) {
        super(cause);
    }

    protected BadInputParametersException(String message, Throwable cause,
                                          boolean enableSuppression,
                                          boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}