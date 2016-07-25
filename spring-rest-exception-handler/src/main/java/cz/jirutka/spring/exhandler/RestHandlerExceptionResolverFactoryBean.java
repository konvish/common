/*
 * Copyright 2014 Jakub Jirutka <jakub@jirutka.cz>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cz.jirutka.spring.exhandler;

import cz.jirutka.spring.exhandler.handlers.RestExceptionHandler;
import cz.jirutka.spring.exhandler.interpolators.MessageInterpolator;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.accept.ContentNegotiationManager;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RestHandlerExceptionResolverFactoryBean implements FactoryBean<RestHandlerExceptionResolver> {
    private ContentNegotiationManager contentNegotiationManager;
    private String defaultContentType;
    private Map<Class<? extends Exception>, ?> exceptionHandlers = Collections.emptyMap();
    private List<HttpMessageConverter<?>> httpMessageConverters;
    private MessageInterpolator messageInterpolator;
    private MessageSource messageSource;
    private boolean withDefaultHandlers = true;
    private boolean withDefaultMessageSource = true;

    public RestHandlerExceptionResolverFactoryBean() {
    }

    public RestHandlerExceptionResolver getObject() {
        RestHandlerExceptionResolverBuilder builder = this.createBuilder().messageSource(this.messageSource).messageInterpolator(this.messageInterpolator).httpMessageConverters(this.httpMessageConverters).contentNegotiationManager(this.contentNegotiationManager).defaultContentType(this.defaultContentType).withDefaultHandlers(this.withDefaultHandlers).withDefaultMessageSource(this.withDefaultMessageSource);
        Iterator var3 = this.exceptionHandlers.entrySet().iterator();

        while(var3.hasNext()) {
            Entry entry = (Entry)var3.next();
            Class exceptionClass = (Class)entry.getKey();
            Object value = entry.getValue();
            if(value instanceof RestExceptionHandler) {
                builder.addHandler(exceptionClass, (RestExceptionHandler)value);
            } else {
                builder.addErrorMessageHandler(exceptionClass, this.parseHttpStatus(value));
            }
        }

        return builder.build();
    }

    public Class<?> getObjectType() {
        return RestHandlerExceptionResolver.class;
    }

    public boolean isSingleton() {
        return false;
    }

    RestHandlerExceptionResolverBuilder createBuilder() {
        return RestHandlerExceptionResolver.builder();
    }

    HttpStatus parseHttpStatus(Object value) {
        Assert.notNull(value, "Values of the exceptionHandlers map must not be null");
        if(value instanceof HttpStatus) {
            return (HttpStatus)value;
        } else if(value instanceof Integer) {
            return HttpStatus.valueOf(((Integer)value).intValue());
        } else if(value instanceof String) {
            return HttpStatus.valueOf(Integer.valueOf((String)value).intValue());
        } else {
            throw new IllegalArgumentException(String.format("Values of the exceptionHandlers maps must be instance of ErrorResponseFactory, HttpStatus, String, or int, but %s given", new Object[]{value.getClass()}));
        }
    }

    public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager) {
        this.contentNegotiationManager = contentNegotiationManager;
    }

    public void setDefaultContentType(String defaultContentType) {
        this.defaultContentType = defaultContentType;
    }

    public void setExceptionHandlers(Map<Class<? extends Exception>, ?> exceptionHandlers) {
        this.exceptionHandlers = exceptionHandlers;
    }

    public void setHttpMessageConverters(List<HttpMessageConverter<?>> httpMessageConverters) {
        this.httpMessageConverters = httpMessageConverters;
    }

    public void setMessageInterpolator(MessageInterpolator messageInterpolator) {
        this.messageInterpolator = messageInterpolator;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setWithDefaultHandlers(boolean withDefaultHandlers) {
        this.withDefaultHandlers = withDefaultHandlers;
    }

    public void setWithDefaultMessageSource(boolean withDefaultMessageSource) {
        this.withDefaultMessageSource = withDefaultMessageSource;
    }
}
