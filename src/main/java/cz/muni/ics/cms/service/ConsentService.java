package cz.muni.ics.cms.service;

import cz.muni.ics.cms.web.api.model.ConsentAttributeRest;
import cz.muni.ics.cms.web.api.model.ConsentRest;
import cz.muni.ics.cms.web.exception.ConsentNotFoundException;
import cz.muni.ics.cms.web.exception.InvalidRelyingPartyReferenceException;
import cz.muni.ics.cms.web.ui.model.ConsentUi;
import java.util.List;
import java.util.Set;

public interface ConsentService {

    ConsentRest createConsent(ConsentRest input) throws InvalidRelyingPartyReferenceException;

    ConsentRest updateConsent(ConsentRest input, Long id)
        throws InvalidRelyingPartyReferenceException, ConsentNotFoundException;

    boolean deleteConsent(Long id);

    List<ConsentRest> getAllConsents();

    ConsentRest getConsent(Long id);

    List<ConsentUi> getConsentsForUser(String userId);

    List<ConsentRest> getConsentsForUserAndRelyingParty(String userIdentifier, String rpIdentifier)
        throws InvalidRelyingPartyReferenceException;

    ConsentRest getConsentForUserAndRelyingPartyWithAttributes(String userIdentifier,
                                                               String rpIdentifier,
                                                               Set<String> attributeNames)
        throws InvalidRelyingPartyReferenceException;

    ConsentRest updateConsent(String userId, String rpId, Set<ConsentAttributeRest> attributes)
        throws InvalidRelyingPartyReferenceException;

    boolean deleteConsent(String userId, String rpId, Set<String> attributeNames)
        throws InvalidRelyingPartyReferenceException;
}
