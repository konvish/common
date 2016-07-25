package com.kong.common.managerui.domain;

import com.kong.common.domain.CreateBaseDomain;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by kong on 2016/1/31.
 */
public class RoleUser extends CreateBaseDomain<Long>{
    private Object userId;
    private Integer roleId;

    public RoleUser(){
    }
    public void setUserId(Object value) {
        this.userId = value;
    }

    public Object getUserId() {
        return this.userId;
    }
    public void setRoleId(Integer value) {
        this.roleId = value;
    }

    public Integer getRoleId() {
        return this.roleId;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("Id",getId())
                .append("Creator",getCreator())
                .append("CreateDate",getCreateDate())
                .append("LastModifier",getLastModifier())
                .append("LastModDate",getLastModDate())
                .append("Status",getStatus())
                .append("UserId",getUserId())
                .append("RoleId",getRoleId())
                .toString();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .toHashCode();
    }

    public boolean equals(Object obj) {
        if(obj instanceof RoleUser == false) return false;
        if(this == obj) return true;
        RoleUser other = (RoleUser)obj;
        return new EqualsBuilder()
                .append(getId(),other.getId())
                .isEquals();
    }
}

