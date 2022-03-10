package cz.muni.ics.cms.web.api;

import static cz.muni.ics.cms.web.api.config.ApiConfiguration.API_URL;

import cz.muni.ics.cms.service.ConsentService;
import cz.muni.ics.cms.web.api.model.ConsentAttributeRest;
import cz.muni.ics.cms.web.api.model.ConsentRest;
import cz.muni.ics.cms.web.exception.BadInputParametersException;
import cz.muni.ics.cms.web.exception.ConsentNotFoundException;
import cz.muni.ics.cms.web.exception.InvalidRelyingPartyReferenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsentApi {

    public static final String URL = API_URL + "/consent";
    public static final String PARAM_ATTRIBUTE_NAMES = "attrs";

    private final ConsentService consentService;

    // === CONSTRUCTORS ===

    @Autowired
    public ConsentApi(ConsentService consentService) {
        this.consentService = consentService;
    }

    // === STANDARD REST methods ===

    @PostMapping(URL)
    public ConsentRest create(@RequestBody ConsentRest input)
        throws BadInputParametersException, InvalidRelyingPartyReferenceException
    {
        if (input == null) {
            throw new BadInputParametersException("Request body is missing");
        }
        validateBody(input);
        return consentService.createConsent(input);
    }

    @PutMapping(URL + "/{id}")
    public ConsentRest update(@RequestBody ConsentRest input,
                              @PathVariable(name = "id") Long id)
        throws BadInputParametersException, ConsentNotFoundException,
        InvalidRelyingPartyReferenceException
    {
        if (input == null) {
            throw new BadInputParametersException("Request body has to be provided");
        } else if (id == null) {
            throw new BadInputParametersException("Missing ID identifying the consent in the path");
        }
        validateBody(input);

        ConsentRest result = consentService.updateConsent(input, id);
        if (result == null) {
            throw new ConsentNotFoundException(notFoundMessage(id));
        }
        return result;
    }

    @DeleteMapping(URL + "/{id}")
    public void delete(@PathVariable(name = "id") Long id)
        throws ConsentNotFoundException, BadInputParametersException
    {
        if (id == null) {
            throw new BadInputParametersException("Missing ID identifying the RP in the path");
        }

        boolean res = consentService.deleteConsent(id);
        if (!res) {
            throw new ConsentNotFoundException(notFoundMessage(id));
        }
    }

    @GetMapping(URL)
    public List<ConsentRest> getAll() {
        return consentService.getAllConsents();
    }

    @GetMapping(URL + "/{id}")
    public ConsentRest get(@PathVariable(name = "id") Long id)
        throws ConsentNotFoundException, BadInputParametersException
    {
        if (id == null) {
            throw new BadInputParametersException("Missing ID identifying the consent in the path");
        }

        ConsentRest result = consentService.getConsent(id);
        if (result == null) {
            throw new ConsentNotFoundException(notFoundMessage(id));
        }
        return result;
    }

    // == WITHOUT IDs ==

    @PutMapping(URL + "/user/{userId}/rp/{rpId}")
    public ConsentRest update(@RequestBody Set<ConsentAttributeRest> attributes,
                              @PathVariable(name = "userId") String userId,
                              @PathVariable(name = "rpId") String rpId)
        throws BadInputParametersException, ConsentNotFoundException,
        InvalidRelyingPartyReferenceException
    {
        if (attributes == null) {
            throw new BadInputParametersException("Request body has to be provided");
        } else if (!StringUtils.hasText(userId)) {
            throw new BadInputParametersException("Missing user identifier in path");
        } else if (!StringUtils.hasText(rpId)) {
            throw new BadInputParametersException("Missing relying party identifier in path");
        }

        ConsentRest result = consentService.updateConsent(userId, rpId, attributes);
        if (result == null) {
            throw new ConsentNotFoundException(notFoundMessage());
        }
        return result;
    }

    @DeleteMapping(URL + "/user/{userId}/rp/{rpId}")
    public void delete(@RequestBody Set<String> attributeNames,
                       @PathVariable(name = "userId") String userId,
                       @PathVariable(name = "rpId") String rpId)
        throws ConsentNotFoundException, BadInputParametersException,
        InvalidRelyingPartyReferenceException
    {
        if (attributeNames == null || attributeNames.isEmpty()) {
            throw new BadInputParametersException("Request body has to be provided");
        } else if (!StringUtils.hasText(userId)) {
            throw new BadInputParametersException("Missing user identifier in path");
        } else if (!StringUtils.hasText(rpId)) {
            throw new BadInputParametersException("Missing relying party identifier in path");
        }

        boolean res = consentService.deleteConsent(userId, rpId, attributeNames);
        if (!res) {
            throw new ConsentNotFoundException(notFoundMessage());
        }
    }

    @GetMapping(URL + "/user/{userId}/rp/{rpId}")
    public List<ConsentRest> get(@PathVariable(name = "userId") String userId,
                                 @PathVariable(name = "rpId") String rpId)
        throws BadInputParametersException, InvalidRelyingPartyReferenceException
    {
        if (!StringUtils.hasText(userId)) {
            throw new BadInputParametersException("Missing user identifier in path");
        } else if (!StringUtils.hasText(rpId)) {
            throw new BadInputParametersException("Missing relying party identifier in path");
        }

        return consentService.getConsentsForUserAndRelyingParty(userId, rpId);
    }

    @GetMapping(URL + "/user/{userId}/rp/{rpId}")
    public ConsentRest get(@PathVariable(name = "userId") String userId,
                           @PathVariable(name = "rpId") String rpId,
                           @RequestParam(name = PARAM_ATTRIBUTE_NAMES ) Set<String> attributeNames)
        throws BadInputParametersException, InvalidRelyingPartyReferenceException,
        ConsentNotFoundException
    {
        if (!StringUtils.hasText(userId)) {
            throw new BadInputParametersException("Missing user identifier in path");
        } else if (!StringUtils.hasText(rpId)) {
            throw new BadInputParametersException("Missing relying party identifier in path");
        } else if (attributeNames == null || attributeNames.isEmpty()) {
            throw new BadInputParametersException(
                "Missing list of attributes (" + PARAM_ATTRIBUTE_NAMES + ") parameter in query");
        }

        ConsentRest result = consentService.getConsentForUserAndRelyingPartyWithAttributes(
            userId, rpId, attributeNames);
        if (result == null) {
            throw new ConsentNotFoundException(notFoundMessage());
        }
        return result;
    }

    // === PRIVATE METHODS ===

    private String notFoundMessage(Long id) {
        return "Consent with id '" + id + "' has not been found!";
    }

    private String notFoundMessage() {
        return "Consent for specified parameters has not been found!";
    }

    private void validateBody(ConsentRest input) throws BadInputParametersException {
        if (!StringUtils.hasText(input.getUserIdentifier())) {
            throw new BadInputParametersException(missingBodyParameterMessage("userIdentifier"));
        } else if (!StringUtils.hasText(input.getRelyingPartyIdentifier())) {
            throw new BadInputParametersException(missingBodyParameterMessage("relyingPartyIdentifier"));
        } else if (input.getAttributes() == null) {
            throw new BadInputParametersException(missingBodyParameterMessage("attributes"));
        } else if (input.getAttributes().isEmpty()) {
            throw new BadInputParametersException(
                "Required body parameter 'attributes' cannot be empty");
        }
        if (input.getCreatedAt() == null) {
            input.setCreatedAt(LocalDateTime.now());
        }
        if (input.getLastUsedAt() == null) {
            input.setLastUsedAt(LocalDateTime.now());
        }
    }

    private String missingBodyParameterMessage(String parameterName) {
        return "Request body is missing required parameter '" + parameterName + "'!";
    }

}
