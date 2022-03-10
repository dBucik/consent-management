package cz.muni.ics.cms.web.api.model;

import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelyingPartyRest {

    @NotBlank
    private String identifier;

    @NotEmpty
    private Map<String, String> name;

    @NotEmpty
    private Map<String, String> description;

}
