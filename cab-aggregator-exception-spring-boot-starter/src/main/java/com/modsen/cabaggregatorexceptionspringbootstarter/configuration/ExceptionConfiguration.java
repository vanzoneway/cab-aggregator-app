package com.modsen.cabaggregatorexceptionspringbootstarter.configuration;

import com.modsen.cabaggregatorexceptionspringbootstarter.exception.BasicGlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionConfiguration {

    @Bean
    @ConditionalOnMissingBean(BasicGlobalExceptionHandler.class)
    public BasicGlobalExceptionHandler basicGlobalExceptionHandler() {
        return new BasicGlobalExceptionHandler();
    }

}
