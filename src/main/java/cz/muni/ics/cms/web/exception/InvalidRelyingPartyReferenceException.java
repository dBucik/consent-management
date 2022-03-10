package cz.muni.ics.cms.web.exception;

public class InvalidRelyingPartyReferenceException extends Exception {

    public InvalidRelyingPartyReferenceException(String message) {
        super(message);
    }

    public InvalidRelyingPartyReferenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRelyingPartyReferenceException(Throwable cause) {
        super(cause);
    }

    protected InvalidRelyingPartyReferenceException(String message, Throwable cause,
                                                    boolean enableSuppression,
                                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
