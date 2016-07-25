package com.kong.common.managerui.domain;

import com.kong.common.domain.CreateBaseDomain;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by kong on 2016/1/31.
 */
public class UserDatagroup extends CreateBaseDomain<Long>{
    private Integer datagroupId;
    private Object userId;

    public UserDatagroup(){
    }
    public void setDatagroupId(Integer value) {
        this.datagroupId = value;
    }

    public Integer getDatagroupId() {
        return this.datagroupId;
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
                .append("DatagroupId",getDatagroupId())
                .append("UserId",getUserId())
                .toString();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .toHashCode();
    }

    public boolean equals(Object obj) {
        if(obj instanceof UserDatagroup == false) return false;
        if(this == obj) return true;
        UserDatagroup other = (UserDatagroup)obj;
        return new EqualsBuilder()
                .append(getId(),other.getId())
                .isEquals();
    }
}