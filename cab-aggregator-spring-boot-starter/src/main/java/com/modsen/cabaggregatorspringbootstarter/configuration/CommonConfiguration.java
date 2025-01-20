package com.modsen.cabaggregatorspringbootstarter.configuration;

import com.modsen.cabaggregatorspringbootstarter.exception.BasicGlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class CommonConfiguration {

    @Bean
    @ConditionalOnMissingBean(MessageSource.class)
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:/messages/validation/messages", "classpath:/messages/exception/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    @ConditionalOnMissingBean(BasicGlobalExceptionHandler.class)
    public BasicGlobalExceptionHandler basicGlobalExceptionHandler() {
        return new BasicGlobalExceptionHandler();
    }

}


