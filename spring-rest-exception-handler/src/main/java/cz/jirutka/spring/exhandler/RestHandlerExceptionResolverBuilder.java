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

import cz.jirutka.spring.exhandler.MapUtils;
import cz.jirutka.spring.exhandler.RestHandlerExceptionResolver;
import cz.jirutka.spring.exhandler.handlers.AbstractRestExceptionHandler;
import cz.jirutka.spring.exhandler.handlers.ConstraintViolationExceptionHandler;
import cz.jirutka.spring.exhandler.handlers.ErrorMessageRestExceptionHandler;
import cz.jirutka.spring.exhandler.handlers.HttpMediaTypeNotSupportedExceptionHandler;
import cz.jirutka.spring.exhandler.handlers.HttpRequestMethodNotSupportedExceptionHandler;
import cz.jirutka.spring.exhandler.handlers.MethodArgumentNotValidExceptionHandler;
import cz.jirutka.spring.exhandler.handlers.NoSuchRequestHandlingMethodExceptionHandler;
import cz.jirutka.spring.exhandler.handlers.RestExceptionHandler;
import cz.jirutka.spring.exhandler.interpolators.MessageInterpolator;
import cz.jirutka.spring.exhandler.interpolators.MessageInterpolatorAware;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

public class RestHandlerExceptionResolverBuilder {
    public static final String DEFAULT_MESSAGES_BASENAME = "classpath:/cz/jirutka/spring/exhandler/messages";
    private final Map<Class, RestExceptionHandler> exceptionHandlers = new HashMap();
    private MediaType defaultContentType;
    private ContentNegotiationManager contentNegotiationManager;
    private List<HttpMessageConverter<?>> httpMessageConverters;
    private MessageInterpolator messageInterpolator;
    private MessageSource messageSource;
    private boolean withDefaultHandlers = true;
    private boolean withDefaultMessageSource = true;

    public RestHandlerExceptionResolverBuilder() {
    }

    public RestHandlerExceptionResolver build() {
        if(this.withDefaultMessageSource) {
            if(this.messageSource != null) {
                HierarchicalMessageSource resolver = this.resolveRootMessageSource(this.messageSource);
                if(resolver != null) {
                    resolver.setParentMessageSource(this.createDefaultMessageSource());
                }
            } else {
                this.messageSource = this.createDefaultMessageSource();
            }
        }

        if(this.withDefaultHandlers) {
            MapUtils.putAllIfAbsent(this.exceptionHandlers, this.getDefaultHandlers());
        }

        Iterator var2 = this.exceptionHandlers.values().iterator();

        while(var2.hasNext()) {
            RestExceptionHandler resolver1 = (RestExceptionHandler)var2.next();
            if(this.messageSource != null && resolver1 instanceof MessageSourceAware) {
                ((MessageSourceAware)resolver1).setMessageSource(this.messageSource);
            }

            if(this.messageInterpolator != null && resolver1 instanceof MessageInterpolatorAware) {
                ((MessageInterpolatorAware)resolver1).setMessageInterpolator(this.messageInterpolator);
            }
        }

        RestHandlerExceptionResolver resolver2 = new RestHandlerExceptionResolver();
        resolver2.setExceptionHandlers(this.exceptionHandlers);
        if(this.httpMessageConverters != null) {
            resolver2.setMessageConverters(this.httpMessageConverters);
        }

        if(this.contentNegotiationManager != null) {
            resolver2.setContentNegotiationManager(this.contentNegotiationManager);
        }

        if(this.defaultContentType != null) {
            resolver2.setDefaultContentType(this.defaultContentType);
        }

        resolver2.afterPropertiesSet();
        return resolver2;
    }

    public RestHandlerExceptionResolverBuilder defaultContentType(MediaType mediaType) {
        this.defaultContentType = mediaType;
        return this;
    }

    public RestHandlerExceptionResolverBuilder defaultContentType(String mediaType) {
        this.defaultContentType(StringUtils.hasText(mediaType)?MediaType.parseMediaType(mediaType):null);
        return this;
    }

    public <E extends Exception> RestHandlerExceptionResolverBuilder addHandler(Class<? extends E> exceptionClass, RestExceptionHandler<E, ?> exceptionHandler) {
        this.exceptionHandlers.put(exceptionClass, exceptionHandler);
        return this;
    }

    public <E extends Exception> RestHandlerExceptionResolverBuilder addHandler(AbstractRestExceptionHandler<E, ?> exceptionHandler) {
        return this.addHandler(exceptionHandler.getExceptionClass(), exceptionHandler);
    }

    public RestHandlerExceptionResolverBuilder addErrorMessageHandler(Class<? extends Exception> exceptionClass, HttpStatus status) {
        return this.addHandler(new ErrorMessageRestExceptionHandler(exceptionClass, status));
    }

    HierarchicalMessageSource resolveRootMessageSource(MessageSource messageSource) {
        if(messageSource instanceof HierarchicalMessageSource) {
            MessageSource parent = ((HierarchicalMessageSource)messageSource).getParentMessageSource();
            return parent != null?this.resolveRootMessageSource(parent):(HierarchicalMessageSource)messageSource;
        } else {
            return null;
        }
    }

    private Map<Class, RestExceptionHandler> getDefaultHandlers() {
        HashMap map = new HashMap();
        map.put(NoSuchRequestHandlingMethodException.class, new NoSuchRequestHandlingMethodExceptionHandler());
        map.put(HttpRequestMethodNotSupportedException.class, new HttpRequestMethodNotSupportedExceptionHandler());
        map.put(HttpMediaTypeNotSupportedException.class, new HttpMediaTypeNotSupportedExceptionHandler());
        map.put(MethodArgumentNotValidException.class, new MethodArgumentNotValidExceptionHandler());
        if(ClassUtils.isPresent("javax.validation.ConstraintViolationException", this.getClass().getClassLoader())) {
            map.put(ConstraintViolationException.class, new ConstraintViolationExceptionHandler());
        }

        this.addHandlerTo(map, HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE);
        this.addHandlerTo(map, MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST);
        this.addHandlerTo(map, ServletRequestBindingException.class, HttpStatus.BAD_REQUEST);
        this.addHandlerTo(map, ConversionNotSupportedException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        this.addHandlerTo(map, TypeMismatchException.class, HttpStatus.BAD_REQUEST);
        this.addHandlerTo(map, HttpMessageNotReadableException.class, HttpStatus.UNPROCESSABLE_ENTITY);
        this.addHandlerTo(map, HttpMessageNotWritableException.class, HttpStatus.INTERNAL_SERVER_ERROR);
        this.addHandlerTo(map, MissingServletRequestPartException.class, HttpStatus.BAD_REQUEST);
        this.addHandlerTo(map, Exception.class, HttpStatus.INTERNAL_SERVER_ERROR);

        try {
            Class clazz = Class.forName("org.springframework.web.servlet.NoHandlerFoundException");
            this.addHandlerTo(map, clazz, HttpStatus.NOT_FOUND);
        } catch (ClassNotFoundException var3) {
            ;
        }

        return map;
    }

    private void addHandlerTo(Map<Class, RestExceptionHandler> map, Class exceptionClass, HttpStatus status) {
        map.put(exceptionClass, new ErrorMessageRestExceptionHandler(exceptionClass, status));
    }

    private MessageSource createDefaultMessageSource() {
        ReloadableResourceBundleMessageSource messages = new ReloadableResourceBundleMessageSource();
        messages.setBasename("classpath:/cz/jirutka/spring/exhandler/messages");
        messages.setDefaultEncoding("UTF-8");
        messages.setFallbackToSystemLocale(false);
        return messages;
    }

    public RestHandlerExceptionResolverBuilder contentNegotiationManager(ContentNegotiationManager contentNegotiationManager) {
        this.contentNegotiationManager = contentNegotiationManager;
        return this;
    }

    public RestHandlerExceptionResolverBuilder httpMessageConverters(List<HttpMessageConverter<?>> httpMessageConverters) {
        this.httpMessageConverters = httpMessageConverters;
        return this;
    }

    public RestHandlerExceptionResolverBuilder messageInterpolator(MessageInterpolator messageInterpolator) {
        this.messageInterpolator = messageInterpolator;
        return this;
    }

    public RestHandlerExceptionResolverBuilder messageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
        return this;
    }

    public RestHandlerExceptionResolverBuilder withDefaultHandlers(boolean withDefaultHandlers) {
        this.withDefaultHandlers = withDefaultHandlers;
        return this;
    }

    public RestHandlerExceptionResolverBuilder withDefaultMessageSource(boolean withDefaultMessageSource) {
        this.withDefaultMessageSource = withDefaultMessageSource;
        return this;
    }
}
