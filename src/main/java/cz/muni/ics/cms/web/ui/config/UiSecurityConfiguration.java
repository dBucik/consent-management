package cz.muni.ics.cms.web.ui.config;

import static cz.muni.ics.cms.web.ui.config.UiConfiguration.HOME_URL;
import static cz.muni.ics.cms.web.ui.config.UiConfiguration.LOGOUT_URL;
import static cz.muni.ics.cms.web.ui.config.UiConfiguration.MANAGER_URL;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
@Order(1)
public class UiSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.mvcMatcher(HOME_URL + "**")
            .authorizeRequests()
                .antMatchers(MANAGER_URL).authenticated()
                .anyRequest().permitAll()
                .and()
            .oauth2Login()
                .and()
            .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutUrl(LOGOUT_URL)
                .logoutSuccessUrl(HOME_URL);
    }

}
