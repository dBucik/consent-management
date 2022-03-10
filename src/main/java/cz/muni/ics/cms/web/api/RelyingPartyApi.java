package cz.muni.ics.cms.web.api;

import static cz.muni.ics.cms.web.api.config.ApiConfiguration.API_URL;

import cz.muni.ics.cms.service.RelyingPartyService;
import cz.muni.ics.cms.web.api.model.RelyingPartyRest;
import cz.muni.ics.cms.web.exception.BadInputParametersException;
import cz.muni.ics.cms.web.exception.InvalidRelyingPartyReferenceException;
import cz.muni.ics.cms.web.exception.RelyingPartyNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RelyingPartyApi {

    public static final String URL = API_URL + "/rp";

    private final RelyingPartyService relyingPartyService;

    @Autowired
    public RelyingPartyApi(RelyingPartyService relyingPartyService) {
        this.relyingPartyService = relyingPartyService;
    }

    @PostMapping(URL)
    public RelyingPartyRest create(@RequestBody RelyingPartyRest input)
        throws BadInputParametersException
    {
        if (input == null) {
            throw new BadInputParametersException("Request body has to be provided");
        }
        return relyingPartyService.createRelyingParty(input);
    }

    @PutMapping(URL + "/{id}")
    public RelyingPartyRest update(@RequestBody RelyingPartyRest input,
                                   @PathVariable(name = "id") String id)
        throws BadInputParametersException, InvalidRelyingPartyReferenceException,
        RelyingPartyNotFoundException
    {
        if (input == null) {
            throw new BadInputParametersException("Request body has to be provided");
        } else if (!StringUtils.hasText(id)) {
            throw new BadInputParametersException("Missing ID identifying the RP in the path");
        }
        RelyingPartyRest result = relyingPartyService.updateRelyingParty(input, id);
        if (result == null) {
            throw new RelyingPartyNotFoundException(notFoundMessage(id));
        }
        return result;
    }

    @DeleteMapping(URL + "/{id}")
    public void delete(@PathVariable(name = "id") String id)
        throws BadInputParametersException, RelyingPartyNotFoundException
    {
        if (!StringUtils.hasText(id)) {
            throw new BadInputParametersException("Missing ID identifying the RP in the path");
        }

        boolean res = relyingPartyService.deleteRelyingParty(id);
        if (!res) {
            throw new RelyingPartyNotFoundException(notFoundMessage(id));
        }
    }

    @GetMapping(URL)
    public List<RelyingPartyRest> getAll() {
        return relyingPartyService.getAllRelyingParties();
    }

    @GetMapping(URL + "/{id}")
    public RelyingPartyRest get(@PathVariable(name = "id") String id)
        throws BadInputParametersException, RelyingPartyNotFoundException
    {
        if (!StringUtils.hasText(id)) {
            throw new BadInputParametersException("Missing ID identifying the RP in the path");
        }
        RelyingPartyRest result = relyingPartyService.getRelyingParty(id);
        if (result == null) {
            throw new RelyingPartyNotFoundException(notFoundMessage(id));
        }
        return result;
    }

    private String notFoundMessage(String id) {
        return "Relying party with id '" + id + "' has not been found!";
    }

}
