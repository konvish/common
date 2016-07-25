package com.kong.common.dap.model.webfilter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 * Created by Administrator on 2016/1/3.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "Creator",
        propOrder = {"name", "version"}
)
public class Creator {
    @XmlElement(
            required = true
    )
    protected String name;
    @XmlElement(
            required = true
    )
    protected String version;

    public Creator() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String value) {
        this.version = value;
    }
}
