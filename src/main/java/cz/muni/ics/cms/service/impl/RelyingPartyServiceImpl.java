package cz.muni.ics.cms.service.impl;

import cz.muni.ics.cms.data.RelyingPartyRepository;
import cz.muni.ics.cms.data.model.RelyingParty;
import cz.muni.ics.cms.service.RelyingPartyService;
import cz.muni.ics.cms.web.api.model.RelyingPartyRest;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class RelyingPartyServiceImpl implements RelyingPartyService {

    private final RelyingPartyRepository repository;

    @Autowired
    public RelyingPartyServiceImpl(RelyingPartyRepository relyingPartyRepository) {
        this.repository = relyingPartyRepository;
    }

    @Override
    public RelyingPartyRest createRelyingParty(RelyingPartyRest input) {
        if (input == null) {
            throw new IllegalArgumentException("Parameter input is null");
        }
        RelyingParty rp = convertFromInput(input);
        rp = repository.save(rp);
        log.debug("Saved RelyingParty: {}", rp);
        return convertFromData(rp);
    }

    @Override
    public RelyingPartyRest updateRelyingParty(RelyingPartyRest input, String id) {
        if (input == null) {
            throw new IllegalArgumentException("Parameter input is null");
        } else if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("Parameter ID is empty or null: " + id);
        }
        RelyingPartyRest result = repository.findById(id)
            .map(rp -> {
                rp.setName(input.getName());
                rp.setDescription(input.getDescription());
                rp = repository.save(rp);
                log.debug("Updated RelyingParty: {}", rp);
                return convertFromData(rp);
            })
            .orElse(null);
        if (result != null) {
            log.debug("Found and updated RelyingParty with ID '{}'", id);
        } else {
            log.debug("RelyingParty with ID '{}' has not been found, cannot update it", id);
        }
        return result;
    }

    @Override
    public boolean deleteRelyingParty(String id) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("Parameter ID is empty or null: " + id);
        }
        try {
            repository.deleteById(id);
            log.debug("RelyingParty with ID '{}' deleted", id);
            return true;
        } catch (EmptyResultDataAccessException ex) {
            log.debug("RelyingParty with ID '{}' has not been found, cannot delete it", id);
            return false;
        }
    }

    @Override
    public List<RelyingPartyRest> getAllRelyingParties() {
        List<RelyingPartyRest> result = new ArrayList<>();
        repository.findAll().forEach(rp -> result.add(convertFromData(rp)));
        log.debug("Extracted all RelyingParties");
        return result;
    }

    @Override
    public RelyingPartyRest getRelyingParty(String id) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("Parameter ID is empty or null: " + id);
        }
        RelyingPartyRest result = repository.findById(id)
            .map(this::convertFromData)
            .orElse(null);
        if (result != null) {
            log.debug("Found and extracted RelyingParty with ID '{}'", id);
        } else {
            log.debug("RelyingParty with ID '{}' not found", id);
        }
        return result;
    }

    private RelyingParty convertFromInput(RelyingPartyRest input) {
        return new RelyingParty(
            input.getIdentifier(),
            input.getName(),
            input.getDescription()
        );
    }

    private RelyingPartyRest convertFromData(RelyingParty input) {
        return new RelyingPartyRest(
            input.getIdentifier(),
            input.getName(),
            input.getDescription()
        );
    }

}
