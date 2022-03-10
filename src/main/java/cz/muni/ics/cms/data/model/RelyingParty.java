package cz.muni.ics.cms.data.model;

import cz.muni.ics.cms.data.converters.MapToJsonConverter;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "relying_parties")
public class RelyingParty {

    @Id
    @Column(name = "identifier")
    @NotBlank
    private String identifier;

    @Column(name = "name")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, String> name = new HashMap<>();

    @Column(name = "description")
    @Convert(converter = MapToJsonConverter.class)
    private Map<String, String> description = new HashMap<>();

}
