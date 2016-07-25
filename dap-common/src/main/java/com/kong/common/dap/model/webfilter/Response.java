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
 * Created by kong on 2016/1/3.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "Response",
        propOrder = {"status", "statusText", "httpVersion", "headers", "headersSize", "bodySize", "content"}
)
public class Response {
    @XmlElement(
            required = true
    )
    protected String status;
    @XmlElement(
            required = true
    )
    protected String statusText;
    @XmlElement(
            required = true
    )
    protected String httpVersion;
    protected List<NameValuePair> headers;
    protected int headersSize;
    protected int bodySize;
    protected Content content;

    public Response() {
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public String getStatusText() {
        return this.statusText;
    }

    public void setStatusText(String value) {
        this.statusText = value;
    }

    public String getHttpVersion() {
        return this.httpVersion;
    }

    public void setHttpVersion(String value) {
        this.httpVersion = value;
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
