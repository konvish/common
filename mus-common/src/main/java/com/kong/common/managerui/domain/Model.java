package com.kong.common.managerui.domain;

import com.kong.common.domain.CreateBaseDomain;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by kong on 2016/1/31.
 */
public class Model extends CreateBaseDomain<Long>{
    private String name;
    private String tblName;
    private String description;

    public Model(){
    }
    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }
    public void setTblName(String value) {
        this.tblName = value;
    }

    public String getTblName() {
        return this.tblName;
    }
    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("Id",getId())
                .append("Creator",getCreator())
                .append("CreateDate",getCreateDate())
                .append("LastModifier",getLastModifier())
                .append("LastModDate",getLastModDate())
                .append("Status",getStatus())
                .append("Name",getName())
                .append("TblName",getTblName())
                .append("Description",getDescription())
                .toString();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .toHashCode();
    }

    public boolean equals(Object obj) {
        if(obj instanceof Model == false) return false;
        if(this == obj) return true;
        Model other = (Model)obj;
        return new EqualsBuilder()
                .append(getId(),other.getId())
                .isEquals();
    }
}