package com.kong.common.managerui.domain;

import com.kong.common.domain.CreateBaseDomain;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by kong on 2016/1/31.
 */
public class Role extends CreateBaseDomain<Long> {

    /**
     * 角色名称
     */
    private String name;
    /**
     * 描述
     */
    private String description;
    /**
     * 产品
     */
    private String product;
    /**
     * dataModelId
     */
    private Long dataModelId;

    private Integer level;

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Role() {
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

    public void setProduct(String value) {
        this.product = value;
    }

    public String getProduct() {
        return this.product;
    }

    public void setDataModelId(Long value) {
        this.dataModelId = value;
    }

    public Long getDataModelId() {
        return this.dataModelId;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("Id", getId())
                .append("Name", getName())
                .append("Creator", getCreator())
                .append("CreateDate", getCreateDate())
                .append("LastModifier", getLastModifier())
                .append("LastModDate", getLastModDate())
                .append("Description", getDescription())
                .append("Status", getStatus())
                .append("Product", getProduct())
                .append("DataModelId", getDataModelId())
                .append("Level", getLevel())
                .toString();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .toHashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof Role == false) return false;
        if (this == obj) return true;
        Role other = (Role) obj;
        return new EqualsBuilder()
                .append(getId(), other.getId())
                .isEquals();
    }
}
