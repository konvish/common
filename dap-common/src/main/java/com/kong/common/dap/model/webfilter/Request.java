package com.kong.common.dap.model.webfilter;

import com.kong.common.dap.model.webfilter.Content;
import com.kong.common.dap.model.webfilter.NameValuePair;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 * 自定义请求类
 * Created by kong on 2016/1/3.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "Request",
        propOrder = {"method", "url", "httpVersion", "queryString", "headers", "headersSize", "bodySize", "content"}
)
public class Request {
    @XmlElement(
            required = true
    )
    protected String method;
    @XmlElement(
            required = true
    )
    protected String url;
    @XmlElement(
            required = true
    )
    protected String httpVersion;
    protected List<NameValuePair> queryString;
    protected List<NameValuePair> headers;
    protected int headersSize;
    protected int bodySize;
    protected Content content;

    public Request() {
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String value) {
        this.method = value;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String value) {
        this.url = value;
    }

    public String getHttpVersion() {
        return this.httpVersion;
    }

    public void setHttpVersion(String value) {
        this.httpVersion = value;
    }

    public List<NameValuePair> getQueryString() {
        if(this.queryString == null) {
            this.queryString = new ArrayList();
        }

        return this.queryString;
    }

    public List<NameValuePair> getHeaders() {
        if(this.headers == null) {
            this.headers = new ArrayList();
        }

        return this.headers;
    }

    public int getHeadersSize() {
        return this.headersSize;
    }

    public void setHeadersSize(int value) {
        this.headersSize = value;
    }

    public int getBodySize() {
        return this.bodySize;
    }

    public void setBodySize(int value) {
        this.bodySize = value;
    }

    public Content getContent() {
        return this.content;
    }

    public void setContent(Content value) {
        this.content = value;
    }
}
