package cz.muni.ics.cms.data.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "cz.muni.ics.cms.data")
@EnableTransactionManagement
public class DataConfig {
}
