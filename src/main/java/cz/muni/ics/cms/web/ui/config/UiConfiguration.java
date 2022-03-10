package cz.muni.ics.cms.web.ui.config;

import cz.muni.ics.cms.ApplicationProperties;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

@Configuration
public class UiConfiguration implements WebMvcConfigurer {

    public static final String HOME_URL = "/";
    public static final String MANAGER_URL = "/manage";
    public static final String LOGOUT_URL = "/logout";

    public static final String PARAM_LANG = "lang";

    private final ApplicationProperties applicationProperties;

    @Autowired
    public UiConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource baseMs = new ResourceBundleMessageSource();
        baseMs.setDefaultEncoding(StandardCharsets.UTF_8.name());
        baseMs.setBasename("i18n/messages");
        baseMs.setDefaultLocale(Locale.ENGLISH);
        baseMs.setUseCodeAsDefaultMessage(true);
        if (StringUtils.hasText(applicationProperties.getMessagesLocation())) {
            ReloadableResourceBundleMessageSource localMs =
                new ReloadableResourceBundleMessageSource();
            localMs.setBasename("file:" + applicationProperties.getMessagesLocation() + "messages");
            localMs.setDefaultLocale(Locale.ENGLISH);
            localMs.setDefaultEncoding(StandardCharsets.UTF_8.name());
            localMs.setUseCodeAsDefaultMessage(true);
            localMs.setParentMessageSource(baseMs);
            return localMs;
        }
        return baseMs;
    }

    @Bean
    public SpringResourceTemplateResolver fallbackTemplateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setOrder(1);
        templateResolver.setCheckExistence(true);

        return templateResolver;
    }

    @Bean
    @ConditionalOnProperty(value = "app.templatesLocation")
    public FileTemplateResolver brandingTemplateResolver() {
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setPrefix(applicationProperties.getTemplatesLocation());
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templateResolver.setOrder(0);
        templateResolver.setCheckExistence(true);

        return templateResolver;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver clr = new SessionLocaleResolver();
        clr.setDefaultLocale(Locale.ENGLISH);
        return clr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName(PARAM_LANG);
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

}
