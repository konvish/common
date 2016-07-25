package com.kong.common.dap.model.webfilter;

import com.kong.common.dap.model.webfilter.Request;
import com.kong.common.dap.model.webfilter.Response;
import com.kong.common.dap.model.webfilter.Timings;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 * Created by kong on 2016/1/3.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "Entry",
        propOrder = {"serverIPAddress", "clientIPAddress", "startedDateTime", "time", "request", "response", "timings"}
)
public class Entry {
    @XmlElement(
            required = true
    )
    protected String serverIPAddress;
    @XmlElement(
            required = true
    )
    protected String clientIPAddress;
    @XmlElement(
            required = true
    )
    protected String startedDateTime;
    protected long time;
    @XmlElement(
            required = true
    )
    protected Request request;
    @XmlElement(
            required = true
    )
    protected Response response;
    @XmlElement(
            required = true
    )
    protected Timings timings;

    public Entry() {
    }

    public String getServerIPAddress() {
        return this.serverIPAddress;
    }

    public void setServerIPAddress(String value) {
        this.serverIPAddress = value;
    }

    public String getClientIPAddress() {
        return this.clientIPAddress;
    }

    public void setClientIPAddress(String value) {
        this.clientIPAddress = value;
    }

    public String getStartedDateTime() {
        return this.startedDateTime;
    }

    public void setStartedDateTime(String value) {
        this.startedDateTime = value;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long value) {
        this.time = value;
    }

    public Request getRequest() {
        return this.request;
    }

    public void setRequest(Request value) {
        this.request = value;
    }

    public Response getResponse() {
        return this.response;
    }

    public void setResponse(Response value) {
        this.response = value;
    }

    public Timings getTimings() {
        return this.timings;
    }

    public void setTimings(Timings value) {
        this.timings = value;
    }
}
