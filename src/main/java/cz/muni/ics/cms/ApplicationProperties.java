package cz.muni.ics.cms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Configuration
@ConfigurationProperties(prefix = "app")
@Slf4j
public class ApplicationProperties {

    @NotEmpty
    private Set<String> languages;

    private String templatesLocation = "";

    private String messagesLocation = "";

    private List<ApiBasicAuth> apiBasicAuths = new ArrayList<>();

    @PostConstruct
    public void init() {
        log.info("Initialized " + ApplicationProperties.class.getSimpleName());
        log.debug("{}", this);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ApiBasicAuth {
        private String username;
        private String password;
    }

}
