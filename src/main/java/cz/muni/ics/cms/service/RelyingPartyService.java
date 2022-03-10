package cz.muni.ics.cms.service;

import cz.muni.ics.cms.web.api.model.RelyingPartyRest;
import cz.muni.ics.cms.web.exception.InvalidRelyingPartyReferenceException;
import java.util.List;

public interface RelyingPartyService {

    RelyingPartyRest createRelyingParty(RelyingPartyRest input);

    RelyingPartyRest updateRelyingParty(RelyingPartyRest input, String id)
        throws InvalidRelyingPartyReferenceException;

    boolean deleteRelyingParty(String id);

    RelyingPartyRest getRelyingParty(String id);

    List<RelyingPartyRest> getAllRelyingParties();

}
