package com.kong.common.restful;

import cz.jirutka.spring.exhandler.RestHandlerExceptionResolver;
import cz.jirutka.spring.exhandler.support.HttpMessageConverterUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.List;


/**
 * Created by kong on 2016/2/2.
 */
@EnableWebMvc
@Configuration
public class RestContextConfig extends WebMvcConfigurerAdapter {
    public RestContextConfig() {
    }

    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(this.exceptionHandlerExceptionResolver());
        resolvers.add(this.exceptionResolver());
    }

    @Bean(
            name = {"exceptionResolver"}
    )
    public RestHandlerExceptionResolver exceptionResolver() {
        return RestHandlerExceptionResolver.builder().messageSource(this.messageSource()).defaultContentType(MediaType.APPLICATION_JSON).addErrorMessageHandler(EmptyResultDataAccessException.class, HttpStatus.NOT_FOUND).addHandler(Exception.class, new BizExceptionHandler()).build();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource m = new ReloadableResourceBundleMessageSource();
        m.setBasename("classpath:org/springframework/security/messages_zh_CN");
        m.setDefaultEncoding("UTF-8");
        return m;
    }

    @Bean
    public ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver() {
        ExceptionHandlerExceptionResolver resolver = new ExceptionHandlerExceptionResolver();
        resolver.setMessageConverters(HttpMessageConverterUtils.getDefaultHttpMessageConverters());
        return resolver;
    }
}