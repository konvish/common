package com.kong.common.dap.model.webfilter;

import com.kong.common.dap.model.webfilter.Har;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 * Created by kong on 2016/1/3.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "Message",
        propOrder = {"serviceToken", "har"}
)
public class Message {
    @XmlElement(
            required = true
    )
    protected String serviceToken;
    @XmlElement(
            required = true
    )
    protected Har har;
    @XmlElement(
            required = true
    )
    protected Map userContext;

    public Message() {
    }

    public String getServiceToken() {
        return this.serviceToken;
    }

    public void setServiceToken(String value) {
        this.serviceToken = value;
    }

    public Har getHar() {
        return this.har;
    }

    public void setHar(Har value) {
        this.har = value;
    }

    public Map getUserContext() {
        return this.userContext;
    }

    public void setUserContext(Map userContext) {
        this.userContext = userContext;
    }

}
