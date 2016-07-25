package com.kong.common.restful.apigen;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.kong.common.restful.apigen.annotation.ApiDesc;
import com.kong.common.restful.apigen.annotation.ApiParam;
import com.kong.common.restful.apigen.annotation.ApiPropDesc;
import com.kong.common.restful.apigen.domain.ApiDetail;
import com.kong.common.restful.apigen.domain.ApiDoc;
import com.kong.common.restful.apigen.domain.ApiSummary;
import com.kong.common.restful.apigen.domain.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;


/**
 * Created by kong on 2016/2/2.
 */
public class ApiDocCollector {
    private static final Logger logger = LoggerFactory.getLogger(ApiDocCollector.class);
    private String scanPath;
    private String protocolVersion;
    private String sysCode;
    private boolean onOff;
    private ApiDoc apiDoc = new ApiDoc();
    static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
    private String resourcePattern = "**/*.class";
    private Environment environment = new StandardEnvironment();
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private MetadataReaderFactory metadataReaderFactory;

    public ApiDocCollector() {
        this.metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);
    }

    public ApiDoc collectData() {
        if(this.scanPath != null && !"".equals(this.scanPath)) {
            this.doScan();
            return this.apiDoc;
        } else {
            return null;
        }
    }

    private void doScan() {
        if(this.onOff) {
            String[] basePackages = StringUtils.tokenizeToStringArray(this.scanPath, ",; \t\n");
            LinkedHashSet clazzSet = new LinkedHashSet();
            String[] i$ = basePackages;
            int clazz = basePackages.length;

            for(int i$1 = 0; i$1 < clazz; ++i$1) {
                String basePackage = i$[i$1];
                clazzSet.addAll(this.loadAndFilter(basePackage));
            }

            Iterator var7 = clazzSet.iterator();

            while(var7.hasNext()) {
                Class var8 = (Class)var7.next();
                this.detectControllerMethods(var8);
            }

        }
    }

    private void detectControllerMethods(Class<?> controller) {
        RequestMapping controllerAnnotation = (RequestMapping) AnnotationUtils.findAnnotation(controller, RequestMapping.class);
        String prefix = "";
        if(controllerAnnotation != null) {
            String[] arr$ = controllerAnnotation.value();
            if(arr$.length != 0) {
                prefix = arr$[0];
                this.checkUrlisOk(prefix);
            }
        }

        Method[] var15 = controller.getMethods();
        int len$ = var15.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Method method = var15[i$];
            RequestMapping methodAnnotation = (RequestMapping)AnnotationUtils.findAnnotation(method, RequestMapping.class);
            if(methodAnnotation != null) {
                ApiSummary apiSummary = new ApiSummary();
                ApiDetail apiDetail = new ApiDetail();
                StringBuffer url = new StringBuffer();
                String[] methodValue = methodAnnotation.value();
                RequestMethod[] methodType = methodAnnotation.method();
                if(methodValue.length != 0) {
                    url.append(prefix);
                    this.checkUrlisOk(methodValue[0]);
                    url.append(methodValue[0]);
                    apiSummary.setUrl(url.toString());
                    apiDetail.setUrl(url.toString());
                }

                if(methodType.length != 0) {
                    apiSummary.setRequestType(methodType[0].name());
                }

                this.detectRequest(apiDetail, method);
                this.detectResponse(apiDetail, method);
                ApiDesc mockDoc = (ApiDesc)AnnotationUtils.findAnnotation(method, ApiDesc.class);
                if(mockDoc != null) {
                    apiSummary.setName(mockDoc.value());
                    apiSummary.setDesc(mockDoc.value());
                    apiSummary.setOwner(mockDoc.owner());
                    apiDetail.setReturnDesc(mockDoc.returnDesc());
                    apiDetail.setName(mockDoc.value());
                    apiDetail.setDesc(mockDoc.value());
                }

                this.apiDoc.addApiDetail(apiDetail);
                this.apiDoc.addApiSummary(apiSummary);
            }
        }

        this.genApiDoc(this.apiDoc);
    }

    private void genApiDoc(ApiDoc apiDoc) {
    }

    private void checkUrlisOk(String url) {
        if(null != url && !"".equals(url)) {
            if(!url.startsWith("/")) {
                throw new IllegalArgumentException(url + "  请求协议格式错误!");
            }
        }
    }

    private void detectRequest(ApiDetail apiDetail, Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Class[] paramType = method.getParameterTypes();
        ArrayList pathVars = new ArrayList();
        ArrayList params = new ArrayList();
        String paramUrl = "";
        ArrayList request = new ArrayList();

        for(int i = 0; i < parameterAnnotations.length; ++i) {
            Class var10000 = paramType[i];
            Annotation descAnnotation = parameterAnnotations[i][0];
            Annotation annotation = parameterAnnotations[i][1];
            if(annotation instanceof RequestBody) {
                Type param1 = ((ParameterizedTypeImpl)method.getGenericParameterTypes()[i]).getActualTypeArguments()[0];
                Class requestClass = null;
                if(param1 instanceof Class) {
                    requestClass = (Class)param1;
                } else {
                    requestClass = ((ParameterizedTypeImpl)param1).getRawType();
                }

                apiDetail.setRequestType(requestClass.getName());
                apiDetail.setRequestDesc(((ApiParam)descAnnotation).desc());
                if(requestClass.getName().indexOf(".dto.") != -1) {
                    this.handleApiPropDesc(requestClass, request);
                }
            } else {
                Param var15;
                if(annotation instanceof PathVariable) {
                    var15 = new Param();
                    var15.setName(((PathVariable)annotation).value());
                    var15.setType(((Class)method.getGenericParameterTypes()[i]).getName());
                    var15.setDesc(((ApiParam)descAnnotation).desc());
                    pathVars.add(var15);
                } else if(annotation instanceof RequestParam) {
                    paramUrl = paramUrl + ((RequestParam)annotation).value() + "=&";
                    var15 = new Param();
                    var15.setName(((RequestParam)annotation).value());
                    var15.setType(((Class)method.getGenericParameterTypes()[i]).getName());
                    var15.setDesc(((ApiParam)descAnnotation).desc());
                    params.add(var15);
                }
            }
        }

        if(paramUrl.length() > 0) {
            apiDetail.setUrl(apiDetail.getUrl() + "?" + paramUrl.substring(0, paramUrl.length() - 1));
        }

        apiDetail.setParams(params);
        apiDetail.setPathVar(pathVars);
        apiDetail.setRequest(request);
    }

    private String toJson(Object body) {
        return JSON.toJSONString(body, new SerializerFeature[]{SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullStringAsEmpty});
    }

    private void detectResponse(ApiDetail apiDetail, Method method) {
        ResponseBody responseBody = (ResponseBody)AnnotationUtils.findAnnotation(method, ResponseBody.class);
        if(responseBody != null) {
            Type type = method.getGenericReturnType();
            Class clazz = null;
            String clazzName = null;
            if(type instanceof Class) {
                clazz = (Class)type;
                clazzName = clazz.getName();
            } else {
                clazzName = type.toString().replace("<", "&lt").replace(">", "&gt");
                clazz = ((ParameterizedTypeImpl)type).getRawType();
            }

            apiDetail.setResponseType(clazzName);
            ArrayList response = new ArrayList();
            method.getParameterAnnotations();
            if("cn.thinkjoy.common.domain.ListWrapper".equals(clazz.getName())) {
                Type t = ((ParameterizedTypeImpl)type).getActualTypeArguments()[0];
                if(t instanceof Class && ((Class)t).getName().indexOf("java.") == -1) {
                    this.handleApiPropDesc((Class)t, response);
                }
            } else if(clazzName.indexOf(".dto.") == -1) {
                apiDetail.setResponse(response);
            } else {
                this.handleApiPropDesc(clazz, response);
            }

            apiDetail.setResponse(response);
        }

    }

    private void handleApiPropDesc(Class clazz, List<Param> params) {
        Param param1 = null;
        Field[] fields = clazz.getDeclaredFields();
        ApiPropDesc apiPropDesc = null;
        Field[] arr$ = fields;
        int len$ = fields.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Field field = arr$[i$];
            param1 = new Param();
            param1.setName(field.getName());
            param1.setType(field.getType().toString());
            apiPropDesc = (ApiPropDesc)field.getAnnotation(ApiPropDesc.class);
            if(apiPropDesc != null) {
                param1.setDesc(apiPropDesc.value());
            }

            params.add(param1);
        }

    }

    private Set<Class<?>> loadAndFilter(String basePackage) {
        HashSet beanClasses = new HashSet();

        try {
            String ex = "classpath*:" + this.resolveBasePackage(basePackage) + "/" + this.resourcePattern;
            Resource[] resources = this.resourcePatternResolver.getResources(ex);
            Resource[] arr$ = resources;
            int len$ = resources.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Resource resource = arr$[i$];
                if(resource.isReadable()) {
                    try {
                        MetadataReader ex1 = this.metadataReaderFactory.getMetadataReader(resource);
                        ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(ex1);
                        sbd.setResource(resource);
                        sbd.setSource(resource);
                        Class beanClass = Class.forName(sbd.getBeanClassName());
                        if(this.isHandler(beanClass)) {
                            beanClasses.add(beanClass);
                        }
                    } catch (Throwable var12) {
                        throw new BeanDefinitionStoreException("Failed to read candidate component class: " + resource, var12);
                    }
                }
            }

            return beanClasses;
        } catch (IOException var13) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", var13);
        }
    }

    private String resolveBasePackage(String basePackage) {
        return ClassUtils.convertClassNameToResourcePath(this.environment.resolveRequiredPlaceholders(basePackage));
    }

    private boolean isHandler(Class<?> beanType) {
        return AnnotationUtils.findAnnotation(beanType, Controller.class) != null || AnnotationUtils.findAnnotation(beanType, RequestMapping.class) != null;
    }

    public void setScanPath(String scanPath) {
        this.scanPath = scanPath;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public void setOnOff(boolean onOff) {
        this.onOff = onOff;
    }
}
