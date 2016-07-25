/*
 * Copyright 2014-2015 Jakub Jirutka <jakub@jirutka.cz>.
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
import cz.jirutka.spring.exhandler.support.HttpMessageConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.FixedContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.HttpEntityMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RestHandlerExceptionResolver extends AbstractHandlerExceptionResolver implements InitializingBean {
    private static final Logger LOG = LoggerFactory.getLogger(RestHandlerExceptionResolver.class);
    private List<HttpMessageConverter<?>> messageConverters = HttpMessageConverterUtils.getDefaultHttpMessageConverters();
    private Map<Class<? extends Exception>, RestExceptionHandler> handlers = new LinkedHashMap();
    private MediaType defaultContentType;
    private ContentNegotiationManager contentNegotiationManager;
    HandlerMethodReturnValueHandler responseProcessor;
    HandlerMethodReturnValueHandler fallbackResponseProcessor;

    public RestHandlerExceptionResolver() {
        this.defaultContentType = MediaType.APPLICATION_XML;
    }

    public static RestHandlerExceptionResolverBuilder builder() {
        return new RestHandlerExceptionResolverBuilder();
    }

    public void afterPropertiesSet() {
        if(this.contentNegotiationManager == null) {
            this.contentNegotiationManager = new ContentNegotiationManager(new ContentNegotiationStrategy[]{new HeaderContentNegotiationStrategy(), new FixedContentNegotiationStrategy(this.defaultContentType)});
        }

        this.responseProcessor = new HttpEntityMethodProcessor(this.messageConverters, this.contentNegotiationManager);
        this.fallbackResponseProcessor = new HttpEntityMethodProcessor(this.messageConverters, new ContentNegotiationManager(new ContentNegotiationStrategy[]{new FixedContentNegotiationStrategy(this.defaultContentType)}));
    }

    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) {
        ResponseEntity entity;
        try {
            entity = this.handleException(exception, request);
        } catch (RestHandlerExceptionResolver.NoExceptionHandlerFoundException var8) {
            LOG.warn("No exception handler found to handle exception: {}", exception.getClass().getName());
            return null;
        }

        try {
            this.processResponse(entity, new ServletWebRequest(request, response));
        } catch (Exception var7) {
            LOG.error("Failed to process error response: {}", entity, var7);
            return null;
        }

        return new ModelAndView();
    }

    protected ResponseEntity<?> handleException(Exception exception, HttpServletRequest request) {
        request.removeAttribute(HandlerMapping.PRODUCIBLE_MEDIA_TYPES_ATTRIBUTE);
        RestExceptionHandler handler = this.resolveExceptionHandler(exception.getClass());
        LOG.debug("Handling exception {} with response factory: {}", exception.getClass().getName(), handler);
        return handler.handleException(exception, request);
    }

    protected RestExceptionHandler<Exception, ?> resolveExceptionHandler(Class<? extends Exception> exceptionClass) {
        for(Class clazz = exceptionClass; clazz != Throwable.class; clazz = clazz.getSuperclass()) {
            if(this.handlers.containsKey(clazz)) {
                return (RestExceptionHandler)this.handlers.get(clazz);
            }
        }

        throw new RestHandlerExceptionResolver.NoExceptionHandlerFoundException();
    }

    protected void processResponse(ResponseEntity<?> entity, NativeWebRequest webRequest) throws Exception {
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();

        try {
            this.responseProcessor.handleReturnValue(entity, (MethodParameter)null, mavContainer, webRequest);
        } catch (HttpMediaTypeNotAcceptableException var4) {
            LOG.debug("Requested media type is not supported, falling back to default one");
            this.fallbackResponseProcessor.handleReturnValue(entity, (MethodParameter)null, mavContainer, webRequest);
        }

    }

    public List<HttpMessageConverter<?>> getMessageConverters() {
        return this.messageConverters;
    }

    public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
        Assert.notNull(messageConverters, "messageConverters must not be null");
        this.messageConverters = messageConverters;
    }

    public ContentNegotiationManager getContentNegotiationManager() {
        return this.contentNegotiationManager;
    }

    public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager) {
        this.contentNegotiationManager = contentNegotiationManager != null?contentNegotiationManager:new ContentNegotiationManager();
    }

    public MediaType getDefaultContentType() {
        return this.defaultContentType;
    }

    public void setDefaultContentType(MediaType defaultContentType) {
        this.defaultContentType = defaultContentType;
    }

    public Map<Class<? extends Exception>, RestExceptionHandler> getExceptionHandlers() {
        return this.handlers;
    }

    public void setExceptionHandlers(Map<Class<? extends Exception>, RestExceptionHandler> handlers) {
        this.handlers = handlers;
    }

    public static class NoExceptionHandlerFoundException extends RuntimeException {
        public NoExceptionHandlerFoundException() {
        }
    }
}
