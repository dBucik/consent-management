package cz.muni.ics.cms.web;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import cz.muni.ics.cms.web.exception.BadInputParametersException;
import cz.muni.ics.cms.web.exception.ConsentNotFoundException;
import cz.muni.ics.cms.web.exception.InvalidRelyingPartyReferenceException;
import cz.muni.ics.cms.web.exception.RelyingPartyNotFoundException;
import cz.muni.ics.cms.web.ui.exception.NoUserSubAvailableException;
import cz.muni.ics.cms.web.ui.exception.NotAuthenticatedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ExceptionHandlingAdvices {

    @ResponseBody
    @ExceptionHandler(BadInputParametersException.class)
    @ResponseStatus(BAD_REQUEST)
    public String badInputParametersHandler(BadInputParametersException ex) {
        log.trace("Handling BadInputParametersException, returning BAD_REQUEST({}) code",
            BAD_REQUEST, ex);
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(InvalidRelyingPartyReferenceException.class)
    @ResponseStatus(BAD_REQUEST)
    public String invalidRelyingPartyReferenceHandler(InvalidRelyingPartyReferenceException ex) {
        log.trace("Handling InvalidRelyingPartyReferenceException, returning BAD_REQUEST({}) code",
            BAD_REQUEST, ex);
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ConsentNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public String consentNotFoundHandler(ConsentNotFoundException ex) {
        log.trace("Handling ConsentNotFoundException, returning NOT_FOUND({}) code",
            NOT_FOUND, ex);
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(RelyingPartyNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public String relyingPartyNotFoundHandler(RelyingPartyNotFoundException ex) {
        log.trace("Handling RelyingPartyNotFoundException, returning NOT_FOUND({}) code",
            NOT_FOUND, ex);
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NotAuthenticatedException.class)
    @ResponseStatus(UNAUTHORIZED)
    public String notAuthenticatedExceptionHandler(NotAuthenticatedException ex) {
        log.trace("Handling NotAuthenticatedException, returning UNAUTHORIZED({}) code",
            UNAUTHORIZED, ex);
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NoUserSubAvailableException.class)
    @ResponseStatus(UNAUTHORIZED)
    public String noUserSubAvailableExceptionHandler(NoUserSubAvailableException ex) {
        log.trace("Handling NoUserSubAvailableException, returning UNAUTHORIZED({}) code",
            UNAUTHORIZED, ex);
        return ex.getMessage();
    }

}