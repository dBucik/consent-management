package cz.muni.ics.cms.web.ui.controller;

import static cz.muni.ics.cms.web.ui.config.UiConfiguration.HOME_URL;
import static cz.muni.ics.cms.web.ui.config.UiConfiguration.MANAGER_URL;

import cz.muni.ics.cms.ApplicationProperties;
import cz.muni.ics.cms.service.ConsentService;
import cz.muni.ics.cms.web.ui.exception.NoUserSubAvailableException;
import cz.muni.ics.cms.web.ui.exception.NotAuthenticatedException;
import cz.muni.ics.cms.web.ui.model.ConsentUi;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@Slf4j
public class UiController {

    private static final String ATTR_CONSENTS = "consents";
    private static final String ATTR_LANGS = "languages";

    private final ConsentService consentService;
    private final ApplicationProperties applicationProperties;

    @Autowired
    public UiController(ConsentService consentService,
                        ApplicationProperties applicationProperties) {
        this.consentService = consentService;
        this.applicationProperties = applicationProperties;
    }

    @GetMapping(HOME_URL)
    public String index() {
        return "index";
    }

    @GetMapping(MANAGER_URL)
    public String consentManager(Model model, @AuthenticationPrincipal OidcUser principal)
        throws NotAuthenticatedException, NoUserSubAvailableException
    {
        if (principal == null) {
            log.warn("No principal provided, user is not authenticated");
            throw new NotAuthenticatedException();
        } else if (!StringUtils.hasText(principal.getSubject())) {
            log.warn("User principal does not contain SUB");
            throw new NoUserSubAvailableException();
        }

        String userId = principal.getSubject();
        List<ConsentUi> consentUis = consentService.getConsentsForUser(userId);

        model.addAttribute(ATTR_CONSENTS, consentUis);
        return "manage";
    }

    @ModelAttribute(ATTR_LANGS)
    public Set<String> langs() {
        return applicationProperties.getLanguages();
    }

}
