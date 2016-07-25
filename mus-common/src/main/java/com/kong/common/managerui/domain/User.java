package com.kong.common.managerui.domain;

import com.kong.common.domain.UserDomain;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by kong on 2016/1/31.
 */
public class User extends UserDomain<Long> {
    //是否管理员
    private Integer isAdmin;
    //权限
    private Integer bizDimension;
    //
    private Integer employeeId;
    //产品线
    private String product;
    //产品线
    private String realName;


    //扩展字段可以添加想要的个性化属性
    private Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public User(){
    }


    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setIsAdmin(Integer value) {
        this.isAdmin = value;
    }

    public Integer getIsAdmin() {
        return this.isAdmin;
    }

    public void setEmployeeId(Integer value) {
        this.employeeId = value;
    }

    public Integer getEmployeeId() {
        return this.employeeId;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("Id",getId())
                .append("IsAdmin", getIsAdmin())
                .append("Name",getName())
                .append("Password",getPassword())
                .append("Creator",getCreator())
                .append("CreateDate",getCreateDate())
                .append("LastModifier",getLastModifier())
                .append("LastModDate",getLastModDate())
                .append("Status",getStatus())
                .append("EmployeeId",getEmployeeId())
                .append("RealName",getRealName())
                .toString();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .toHashCode();
    }

    public boolean equals(Object obj) {
        if(obj instanceof User == false) return false;
        if(this == obj) return true;
        User other = (User)obj;
        return new EqualsBuilder()
                .append(getId(),other.getId())
                .isEquals();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getBizDimension() {
        return bizDimension;
    }

    public void setBizDimension(Integer bizDimension) {
        this.bizDimension = bizDimension;
    }
}