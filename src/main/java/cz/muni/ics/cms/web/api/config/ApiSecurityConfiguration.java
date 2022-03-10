package cz.muni.ics.cms.web.api.config;

import static cz.muni.ics.cms.web.api.config.ApiConfiguration.API_URL;

import cz.muni.ics.cms.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
@Order(0)
public class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ApplicationProperties applicationProperties;

    @Autowired
    public ApiSecurityConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        if (applicationProperties.getApiBasicAuths() != null
            && !applicationProperties.getApiBasicAuths().isEmpty()) {
            InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> configurer =
                auth.inMemoryAuthentication();
            PasswordEncoder passwordEncoder = passwordEncoder();
            for (ApplicationProperties.ApiBasicAuth c : applicationProperties.getApiBasicAuths()) {
                configurer = configurer.withUser(c.getUsername())
                    .password(passwordEncoder.encode(c.getPassword()))
                    .authorities("ROLE_API").and();
            }
        } else {
            throw new RuntimeException("No authentication for API provided");
        }
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher(API_URL + "/**")
            .csrf()
                .disable()
            .authorizeRequests()
                .antMatchers(API_URL + "/**").hasRole("API")
            .and()
                .httpBasic()
                .realmName("CMS_API_REALM")
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
