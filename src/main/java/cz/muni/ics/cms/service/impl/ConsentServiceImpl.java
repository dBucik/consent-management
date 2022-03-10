package cz.muni.ics.cms.service.impl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import cz.muni.ics.cms.data.ConsentRepository;
import cz.muni.ics.cms.data.RelyingPartyRepository;
import cz.muni.ics.cms.data.model.Consent;
import cz.muni.ics.cms.data.model.ConsentAttribute;
import cz.muni.ics.cms.data.model.RelyingParty;
import cz.muni.ics.cms.service.ConsentService;
import cz.muni.ics.cms.web.api.model.ConsentAttributeRest;
import cz.muni.ics.cms.web.api.model.ConsentRest;
import cz.muni.ics.cms.web.exception.InvalidRelyingPartyReferenceException;
import cz.muni.ics.cms.web.ui.model.ConsentUi;
import cz.muni.ics.cms.web.ui.model.RelyingPartyUi;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class ConsentServiceImpl implements ConsentService {

    private final ConsentRepository consentRepo;
    private final RelyingPartyRepository relyingPartyRepo;

    @Autowired
    public ConsentServiceImpl(ConsentRepository consentRepo,
                              RelyingPartyRepository relyingPartyRepo)
    {
        this.consentRepo = consentRepo;
        this.relyingPartyRepo = relyingPartyRepo;
    }

    @Override
    public ConsentRest createConsent(ConsentRest input)
        throws InvalidRelyingPartyReferenceException
    {
        if (input == null) {
            throw new IllegalArgumentException("Parameter input is null");
        } else if (!StringUtils.hasText(input.getRelyingPartyIdentifier())) {
            throw new IllegalArgumentException(
                "Parameter input does not contain identifier of nested RelyingParty object");
        }

        Consent c = convertFromInput(input);
        c = consentRepo.save(c);
        log.debug("Saved consent: {}", c);
        return convertFromData(c);
    }

    @Override
    public ConsentRest updateConsent(ConsentRest input, Long id)
        throws InvalidRelyingPartyReferenceException
    {
        if (input == null) {
            throw new IllegalArgumentException("Parameter input is null");
        } else if (!StringUtils.hasText(input.getRelyingPartyIdentifier())) {
            throw new IllegalArgumentException(
                "Parameter input does not contain identifier of nested RelyingParty object");
        }

        RelyingParty rp = getRelyingParty(input.getRelyingPartyIdentifier());

        ConsentRest result = consentRepo.findById(id)
            .map(c -> {
                c.setUserId(input.getUserIdentifier());
                c.setRelyingParty(rp);
                c.setAttributes(convertAttributesFromInput(input.getAttributes()));
                c.setCreatedAt(getTimeParameter(input.getCreatedAt(), c.getCreatedAt()));
                c.setLastUsedAt(getTimeParameter(input.getLastUsedAt()));
                c = consentRepo.save(c);
                log.debug("Updated Consent: {}", c);
                return convertFromData(c);
            }).orElse(null);
        if (result != null) {
            log.debug("Found and updated Consent with ID '{}'", id);
        } else {
            log.debug("Consent with ID '{}' has not been found, cannot update it", id);
        }
        return result;
    }

    @Override
    public boolean deleteConsent(Long id) {
        try {
            consentRepo.deleteById(id);
            log.debug("Consent with ID '{}' deleted", id);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            log.debug("Consent with ID '{}' has not been found, cannot delete it", id);
            return false;
        }
    }

    @Override
    public List<ConsentRest> getAllConsents() {
        List<ConsentRest> result = new ArrayList<>();
        consentRepo.findAll().forEach(rp -> result.add(convertFromData(rp)));
        log.debug("Extracted all Consents");
        return result;
    }

    @Override
    public ConsentRest getConsent(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Parameter ID is null");
        }
        ConsentRest result = consentRepo.findById(id)
            .map(this::convertFromData)
            .orElse(null);
        if (result != null) {
            log.debug("Found and extracted Consent with ID '{}'", id);
        } else {
            log.debug("Consent with ID '{}' not found", id);
        }
        return result;
    }

    @Override
    public List<ConsentUi> getConsentsForUser(String userId) {
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("Parameter userId is null or empty: " + userId);
        }
        List<Consent> consents = consentRepo.findAllByUserId(userId);
        log.debug("Extracted all Consents for user with ID '{}'", userId);
        return transformToUiConsents(consents);
    }

    @Override
    public List<ConsentRest> getConsentsForUserAndRelyingParty(String userId,
                                                               String rpId)
        throws InvalidRelyingPartyReferenceException
    {
        validateMethodInput(userId, rpId);
        RelyingParty relyingParty = getRelyingParty(rpId);
        List<ConsentRest> result = new ArrayList<>();
        consentRepo.findByUserIdAndRelyingParty(userId, relyingParty)
            .forEach(c -> result.add(convertFromData(c)));
        log.debug("Extracted all Consents for user with ID '{}' and RelyingParty '{}'",
            userId, rpId);
        return result;
    }

    @Override
    public ConsentRest updateConsent(String userId, String rpId,
                                     Set<ConsentAttributeRest> attributes)
        throws InvalidRelyingPartyReferenceException
    {
        validateMethodInput(userId, rpId);
        if (attributes == null || attributes.isEmpty()) {
            throw new IllegalArgumentException(
                "Parameter attributes is null or empty: " + attributes);
        }
        Set<String> attributeNames = attributes.stream()
            .map(ConsentAttributeRest::getName).collect(Collectors.toSet());
        Consent foundConsent = getConsentByAttributes(userId, rpId, attributeNames);
        ConsentRest result = null;
        if (foundConsent != null) {
            result = consentRepo.findById(foundConsent.getId())
                .map( c -> {
                    c.setUserId(c.getUserId());
                    c.setRelyingParty(c.getRelyingParty());
                    c.setAttributes(convertAttributesFromInput(attributes));
                    c.setCreatedAt(c.getCreatedAt());
                    c.setLastUsedAt(LocalDateTime.now());
                    c = consentRepo.save(c);
                    return convertFromData(c);
                }).orElse(null);
        }
        if (result != null) {
            log.debug("Found and updated Consent for user '{}' and RelyingParty '{}' " +
                "with attributes '{}'", userId, rpId, attributeNames);
        } else {
            log.debug("Consent for user '{}' and RelyingParty '{}' with attributes '{}' " +
                "has not been found, cannot update it", userId, rpId, attributeNames);
        }
        return result;
    }

    @Override
    public boolean deleteConsent(String userId, String rpId, Set<String> attributeNames)
        throws InvalidRelyingPartyReferenceException
    {
        validateMethodInput(userId, rpId);
        if (attributeNames == null || attributeNames.isEmpty()) {
            throw new IllegalArgumentException(
                "Parameter attributeNames is null or empty: " + attributeNames);
        }
        Consent foundConsent = getConsentByAttributes(userId, rpId, attributeNames);
        if (foundConsent != null) {
            try {
                consentRepo.deleteById(foundConsent.getId());
                log.debug("Consent for user '{}' and RelyingParty '{}' with attributes '{}' " +
                        "has been deleted", userId, rpId, attributeNames);
                return true;
            } catch (EmptyResultDataAccessException ex) {
                // handled by method's final code
            }
        }
        log.debug("Consent for user '{}' and RelyingParty '{}' with attributes '{}' " +
            "has not been found, cannot be deleted", userId, rpId, attributeNames);
        return false;
    }

    @Override
    public ConsentRest getConsentForUserAndRelyingPartyWithAttributes(String userId,
                                                                      String rpId,
                                                                      Set<String> attributeNames)
        throws InvalidRelyingPartyReferenceException
    {
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("Parameter userId is null or empty: " + userId);
        } else if (!StringUtils.hasText(rpId)) {
            throw new IllegalArgumentException("Parameter rpId is null or empty: " + rpId);
        } else if (attributeNames == null || attributeNames.isEmpty()) {
            throw new IllegalArgumentException("Parameter attributeNames is null or empty: "
                + attributeNames);
        }

        Consent c = getConsentByAttributes(userId, rpId, attributeNames);
        ConsentRest result = null;
        if (c != null) {
            result = convertFromData(c);
        }

        if (result != null) {
            log.debug("Found and extracted Consent for user '{}' and RelyingParty '{}' " +
                "with attributes '{}'", userId, rpId, attributeNames);
        } else {
            log.debug("Consent for user '{}' and RelyingParty '{}' with attributes '{}' " +
                "has not been found", userId, rpId, attributeNames);
        }
        return result;
    }

    private void validateMethodInput(String userId, String rpId) {
        if (!StringUtils.hasText(userId)) {
            throw new IllegalArgumentException("Parameter userId is null or empty: " + userId);
        } else if (!StringUtils.hasText(rpId)) {
            throw new IllegalArgumentException("Parameter rpId is null or empty: " + rpId);
        }
    }

    private Consent getConsentByAttributes(String userId, String rpId,
                                           Set<String> attributeNames)
        throws InvalidRelyingPartyReferenceException
    {
        RelyingParty relyingParty = getRelyingParty(rpId);
        List<Consent> foundConsents = new ArrayList<>(
            consentRepo.findByUserIdAndRelyingParty(userId, relyingParty)
        );
        if (!foundConsents.isEmpty()) {
            for (Consent c: foundConsents) {
                if (hasMatchingAttributes(attributeNames, c)) {
                    return c;
                }
            }
        }
        return null;
    }

    private boolean hasMatchingAttributes(Set<String> attributeNames, Consent c) {
        Set<String> consentAttributes = c.getAttributeNames();
        return attributeNames.size() == consentAttributes.size()
            && consentAttributes.containsAll(attributeNames);
    }

    private Consent convertFromInput(ConsentRest input) throws
        InvalidRelyingPartyReferenceException
    {
        RelyingParty rp = getRelyingParty(input.getRelyingPartyIdentifier());

        Consent c = new Consent();
        c.setUserId(input.getUserIdentifier());
        c.setRelyingParty(rp);
        c.setAttributes(convertAttributesFromInput(input.getAttributes()));
        c.setCreatedAt(getTimeParameter(input.getCreatedAt()));
        c.setLastUsedAt(getTimeParameter(input.getLastUsedAt()));
        return c;
    }

    private ConsentRest convertFromData(Consent input) {
        ConsentRest c = new ConsentRest();
        c.setUserIdentifier(input.getUserId());
        c.setRelyingPartyIdentifier(input.getRelyingParty().getIdentifier());
        c.setAttributes(convertAttributesFromData(input.getAttributes()));
        c.setCreatedAt(input.getCreatedAt());
        c.setLastUsedAt(input.getLastUsedAt());
        return c;
    }

    private RelyingParty getRelyingParty(String rpIdentifier)
        throws InvalidRelyingPartyReferenceException
    {
        try {
            RelyingParty rp = relyingPartyRepo.getByIdentifier(rpIdentifier);
            if (rp != null) {
                return rp;
            }
        } catch (EmptyResultDataAccessException ex) {
            // handled by method's final code
        }
        throw new InvalidRelyingPartyReferenceException("Relying party with identifier '"
            + rpIdentifier + "' does not exist");
    }

    private LocalDateTime getTimeParameter(LocalDateTime dt) {
        return getTimeParameter(dt, null);
    }

    private LocalDateTime getTimeParameter(LocalDateTime dateTime, LocalDateTime defaultValue) {
        if (defaultValue == null) {
            defaultValue = LocalDateTime.now();
        }
        return dateTime != null ? dateTime : defaultValue;
    }

    private List<ConsentUi> transformToUiConsents(List<Consent> daos) {
        List<ConsentUi> res = new ArrayList<>();
        for (Consent c : daos) {
            RelyingPartyUi rp = new RelyingPartyUi(
                c.getRelyingParty().getName(), c.getRelyingParty().getDescription());
            Set<String> attributes = c.getAttributes().stream()
                .map(ConsentAttribute::getName).collect(Collectors.toSet());
            ConsentUi consent = new ConsentUi(
                c.getId(), rp, attributes, c.getLastUsedAt());
            res.add(consent);
        }
        return res;
    }

    private Set<ConsentAttribute> convertAttributesFromInput(Set<ConsentAttributeRest> attributes) {
        Set<ConsentAttribute> res = new HashSet<>();
        if (attributes != null && !attributes.isEmpty()) {
            for (ConsentAttributeRest car: attributes) {
                if (car == null) {
                    continue;
                }
                if (car.getValue() == null) {
                    car.setValue(JsonNodeFactory.instance.nullNode());
                }
                res.add(new ConsentAttribute(car.getName(), car.getValue()));
            }
        }
        return res;
    }

    private Set<ConsentAttributeRest> convertAttributesFromData(Set<ConsentAttribute> attributes) {
        Set<ConsentAttributeRest> res = new HashSet<>();
        if (attributes != null && !attributes.isEmpty()) {
            for (ConsentAttribute car: attributes) {
                if (car == null) {
                    continue;
                }
                if (car.getValue() == null) {
                    car.setValue(JsonNodeFactory.instance.nullNode());
                }
                res.add(new ConsentAttributeRest(car.getName(), car.getValue()));
            }
        }
        return res;
    }

}
