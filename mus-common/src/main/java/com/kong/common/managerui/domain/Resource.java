package com.kong.common.managerui.domain;

import com.kong.common.domain.CreateBaseDomain;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by kong on 2016/1/31.
 */
public class Resource extends CreateBaseDomain<Long> {
    /** 页面url */
    private String url;
    /** 顺序 */
    private Integer orderNum;
    /** 父页面,null为顶层页面 */
    private Integer parentId;
    /** 编码 */
    private String number;
    /** 长编码 */
    private String longNumber;
    /** 资源名称 */
    private String name;
    /** 描述 */
    private String description;
    /** 主模型id  和 数据权限相关 */
    private Integer modelId;
    /** 业务模型名称|在同一个业务系统里不允许有同名的业务模型 */
    private String bizModelName;
    /** 产品线 */
    private String product;
    /** 是否在系统显示 */
    private Integer hide;

    public Resource(){
    }
    public void setUrl(String value) {
        this.url = value;
    }

    public String getUrl() {
        return this.url;
    }
    public void setOrderNum(Integer value) {
        this.orderNum = value;
    }

    public Integer getOrderNum() {
        return this.orderNum;
    }
    public void setParentId(Integer value) {
        this.parentId = value;
    }

    public Integer getParentId() {
        return this.parentId;
    }
    public void setNumber(String value) {
        this.number = value;
    }

    public String getNumber() {
        return this.number;
    }
    public void setLongNumber(String value) {
        this.longNumber = value;
    }

    public String getLongNumber() {
        return this.longNumber;
    }
    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }
    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }
    public void setModelId(Integer value) {
        this.modelId = value;
    }

    public Integer getModelId() {
        return this.modelId;
    }
    public void setBizModelName(String value) {
        this.bizModelName = value;
    }

    public String getBizModelName() {
        return this.bizModelName;
    }
    public void setProduct(String value) {
        this.product = value;
    }

    public String getProduct() {
        return this.product;
    }
    public void setHide(Integer value) {
        this.hide = value;
    }

    public Integer getHide() {
        return this.hide;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("Id",getId())
                .append("Url",getUrl())
                .append("OrderNum",getOrderNum())
                .append("ParentId",getParentId())
                .append("Number",getNumber())
                .append("LongNumber",getLongNumber())
                .append("Name",getName())
                .append("Creator",getCreator())
                .append("CreateDate",getCreateDate())
                .append("LastModifier",getLastModifier())
                .append("LastModDate",getLastModDate())
                .append("Description",getDescription())
                .append("ModelId",getModelId())
                .append("BizModelName",getBizModelName())
                .append("Status",getStatus())
                .append("Product",getProduct())
                .append("Hide",getHide())
                .toString();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .toHashCode();
    }

    public boolean equals(Object obj) {
        if(obj instanceof Resource == false) return false;
        if(this == obj) return true;
        Resource other = (Resource)obj;
        return new EqualsBuilder()
                .append(getId(),other.getId())
                .isEquals();
    }
}