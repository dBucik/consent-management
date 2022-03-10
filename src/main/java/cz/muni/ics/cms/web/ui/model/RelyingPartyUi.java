package cz.muni.ics.cms.web.ui.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelyingPartyUi {

    private Map<String, String> name;
    private Map<String, String> description;

}
