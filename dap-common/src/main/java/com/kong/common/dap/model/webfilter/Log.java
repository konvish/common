package com.kong.common.dap.model.webfilter;

import com.kong.common.dap.model.webfilter.Creator;
import com.kong.common.dap.model.webfilter.Entry;
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
        name = "Log",
        propOrder = {"version", "creator", "entries"}
)
public class Log {
    @XmlElement(
            required = true
    )
    protected String version;
    @XmlElement(
            required = true
    )
    protected Creator creator;
    protected List<Entry> entries;

    public Log() {
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String value) {
        this.version = value;
    }

    public Creator getCreator() {
        return this.creator;
    }

    public void setCreator(Creator value) {
        this.creator = value;
    }

    public List<Entry> getEntries() {
        if(this.entries == null) {
            this.entries = new ArrayList();
        }

        return this.entries;
    }
}
