package com.kong.common.dap.model.webfilter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
/**
 * Created by kong on 2016/1/3.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "Content",
        propOrder = {"size", "mimeType", "encoding", "text"}
)
public class Content {
    protected int size;
    @XmlElement(
            required = true
    )
    protected String mimeType;
    protected String encoding;
    protected String text;

    public Content() {
    }

    public int getSize() {
        return this.size;
    }

    public void setSize(int value) {
        this.size = value;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public void setMimeType(String value) {
        this.mimeType = value;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String value) {
        this.encoding = value;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String value) {
        this.text = value;
    }
}
