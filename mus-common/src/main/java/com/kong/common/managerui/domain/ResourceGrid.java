package com.kong.common.managerui.domain;

import com.kong.common.domain.CreateBaseDomain;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by kong on 2016/1/31.
 */
public class ResourceGrid extends CreateBaseDomain<Long>{
    private Integer resId;
    private String displayName;
    private String colId;
    private Integer orderNum;
    private Integer width;
    private String edittype;
    private String editable;
    private String editoptions;
    private String editrules;
    private String formatter;
    private String formatoptions;
    private String unformat;
    private Integer hide;
    private Integer unedit;
    private String description;
    private String moduleName;

    public ResourceGrid(){
    }
    public void setResId(Integer value) {
        this.resId = value;
    }

    public Integer getResId() {
        return this.resId;
    }
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    public String getDisplayName() {
        return this.displayName;
    }
    public void setColId(String value) {
        this.colId = value;
    }

    public String getColId() {
        return this.colId;
    }
    public void setOrderNum(Integer value) {
        this.orderNum = value;
    }

    public Integer getOrderNum() {
        return this.orderNum;
    }
    public void setWidth(Integer value) {
        this.width = value;
    }

    public Integer getWidth() {
        return this.width;
    }
    public void setEdittype(String value) {
        this.edittype = value;
    }

    public String getEdittype() {
        return this.edittype;
    }
    public void setEditable(String value) {
        this.editable = value;
    }

    public String getEditable() {
        return this.editable;
    }
    public void setEditoptions(String value) {
        this.editoptions = value;
    }

    public String getEditoptions() {
        return this.editoptions;
    }
    public void setEditrules(String value) {
        this.editrules = value;
    }

    public String getEditrules() {
        return this.editrules;
    }
    public void setFormatter(String value) {
        this.formatter = value;
    }

    public String getFormatter() {
        return this.formatter;
    }
    public void setFormatoptions(String value) {
        this.formatoptions = value;
    }

    public String getFormatoptions() {
        return this.formatoptions;
    }
    public void setUnformat(String value) {
        this.unformat = value;
    }

    public String getUnformat() {
        return this.unformat;
    }
    public void setHide(Integer value) {
        this.hide = value;
    }

    public Integer getHide() {
        return this.hide;
    }
    public void setUnedit(Integer value) {
        this.unedit = value;
    }

    public Integer getUnedit() {
        return this.unedit;
    }
    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }
    public void setModuleName(String value) {
        this.moduleName = value;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("Id",getId())
                .append("Creator",getCreator())
                .append("CreateDate",getCreateDate())
                .append("LastModifier",getLastModifier())
                .append("LastModDate",getLastModDate())
                .append("Status",getStatus())
                .append("ResId",getResId())
                .append("DisplayName",getDisplayName())
                .append("ColId",getColId())
                .append("OrderNum",getOrderNum())
                .append("Width",getWidth())
                .append("Edittype",getEdittype())
                .append("Editable",getEditable())
                .append("Editoptions",getEditoptions())
                .append("Editrules",getEditrules())
                .append("Formatter",getFormatter())
                .append("Formatoptions",getFormatoptions())
                .append("Unformat",getUnformat())
                .append("Hide",getHide())
                .append("Unedit",getUnedit())
                .append("Description",getDescription())
                .append("ModuleName",getModuleName())
                .toString();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .toHashCode();
    }

    public boolean equals(Object obj) {
        if(obj instanceof ResourceGrid == false) return false;
        if(this == obj) return true;
        ResourceGrid other = (ResourceGrid)obj;
        return new EqualsBuilder()
                .append(getId(),other.getId())
                .isEquals();
    }
}
