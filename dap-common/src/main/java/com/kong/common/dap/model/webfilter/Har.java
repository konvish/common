package com.kong.common.dap.model.webfilter;

import com.kong.common.dap.model.webfilter.Log;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 * Created by kong on 2016/1/3.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "Har",
        propOrder = {"log"}
)
public class Har {
    @XmlElement(
            required = true
    )
    protected Log log;

    public Har() {
    }

    public Log getLog() {
        return this.log;
    }

    public void setLog(Log value) {
        this.log = value;
    }
}
