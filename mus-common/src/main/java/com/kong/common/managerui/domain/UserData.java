package com.kong.common.managerui.domain;

import com.kong.common.domain.CreateBaseDomain;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by kong on 2016/1/31.
 */
public class UserData extends CreateBaseDomain<Long>{
    private Integer dataModelId;
    private Object dataId;
    private Object userId;

    public UserData(){
    }

    public void setDataModelId(Integer value) {
        this.dataModelId = value;
    }

    public Integer getDataModelId() {
        return this.dataModelId;
    }
    public void setDataId(Object value) {
        this.dataId = value;
    }

    public Object getDataId() {
        return this.dataId;
    }
    public void setUserId(Object value) {
        this.userId = value;
    }

    public Object getUserId() {
        return this.userId;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("Id",getId())
                .append("Creator",getCreator())
                .append("CreateDate",getCreateDate())
                .append("LastModifier",getLastModifier())
                .append("LastModDate",getLastModDate())
                .append("Status",getStatus())
                .append("DataModelId",getDataModelId())
                .append("DataId",getDataId())
                .append("UserId",getUserId())
                .toString();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .toHashCode();
    }

    public boolean equals(Object obj) {
        if(obj instanceof UserData == false) return false;
        if(this == obj) return true;
        UserData other = (UserData)obj;
        return new EqualsBuilder()
                .append(getId(),other.getId())
                .isEquals();
    }
}