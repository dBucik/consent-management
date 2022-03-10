package cz.muni.ics.cms.web.exception;

public class RelyingPartyNotFoundException extends Exception {

    public RelyingPartyNotFoundException(String message) {
        super(message);
    }

    public RelyingPartyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public RelyingPartyNotFoundException(Throwable cause) {
        super(cause);
    }

    protected RelyingPartyNotFoundException(String message, Throwable cause,
                                            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
