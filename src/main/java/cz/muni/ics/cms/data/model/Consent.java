package cz.muni.ics.cms.data.model;

import cz.muni.ics.cms.data.converters.ConsentAttributeSetConverter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@Table(name = "consents")
public class Consent {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Column(name = "user_identifier")
    private String userId;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "identifier")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RelyingParty relyingParty;

    @NotEmpty
    @Column(name = "attributes")
    @Convert(converter = ConsentAttributeSetConverter.class)
    @Lob
    private Set<ConsentAttribute> attributes = new HashSet<>();

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Transient
    public Set<String> getAttributeNames() {
        return attributes.stream().map(ConsentAttribute::getName).collect(Collectors.toSet());
    }

}
