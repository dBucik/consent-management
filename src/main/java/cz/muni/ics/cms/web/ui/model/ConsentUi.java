package cz.muni.ics.cms.web.ui.model;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsentUi {

    private Long consentId;
    private RelyingPartyUi relyingParty;
    private Set<String> attributes;
    private LocalDateTime lastUsedAt;

}
