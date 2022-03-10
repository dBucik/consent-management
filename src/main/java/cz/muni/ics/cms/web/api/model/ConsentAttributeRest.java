package cz.muni.ics.cms.web.api.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsentAttributeRest {

    @NotBlank
    private String name;

    @NotNull
    private JsonNode value = JsonNodeFactory.instance.nullNode();

}
