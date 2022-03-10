package cz.muni.ics.cms.web.api.model;

import java.time.LocalDateTime;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsentRest {

    @NotBlank
    private String userIdentifier;

    @NotBlank
    private String relyingPartyIdentifier;

    @NotEmpty
    private Set<ConsentAttributeRest> attributes;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime lastUsedAt;

}
