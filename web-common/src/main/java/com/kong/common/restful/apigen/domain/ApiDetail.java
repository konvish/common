package com.kong.common.restful.apigen.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kong on 2016/2/2.
 */
public class ApiDetail implements Serializable {
    private String url;
    private String name;
    private String desc;
    private List<Param> pathVar;
    private List<Param> params;
    private String requestDesc;
    private String requestType;
    private List<Param> request;
    private String returnDesc;
    private String responseType;
    private List<Param> response;

    public ApiDetail() {
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Param> getPathVar() {
        return this.pathVar;
    }

    public void setPathVar(List<Param> pathVar) {
        this.pathVar = pathVar;
    }

    public List<Param> getParams() {
        return this.params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }

    public List<Param> getRequest() {
        return this.request;
    }

    public void setRequest(List<Param> request) {
        this.request = request;
    }

    public List<Param> getResponse() {
        return this.response;
    }

    public void setResponse(List<Param> response) {
        this.response = response;
    }

    public String getRequestType() {
        return this.requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getResponseType() {
        return this.responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getReturnDesc() {
        return this.returnDesc;
    }

    public void setReturnDesc(String returnDesc) {
        this.returnDesc = returnDesc;
    }

    public String getRequestDesc() {
        return this.requestDesc;
    }

    public void setRequestDesc(String requestDesc) {
        this.requestDesc = requestDesc;
    }
}
