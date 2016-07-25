package com.kong.common.managerui.domain;

import com.kong.common.domain.CreateBaseDomain;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Created by kong on 2016/1/31.
 */
public class DataModel extends CreateBaseDomain<Long>{
    /** 优先级 */
    private Integer priority;
    /** 其对应的模型主体id */
    private Integer modelId;
    /** 分配的url */
    private String assignUrl;
    /** 追加数据权限的sql */
    private String whereSql;
    /** 按**维度设置 */
    private String name;
    /** 主键名称 */
    private String keyName;
    /** 字段值 */
    private String valueName;
    /** 数据源sql */
    private String datasourceSql;

    public DataModel(){
    }
    public void setPriority(Integer value) {
        this.priority = value;
    }

    public Integer getPriority() {
        return this.priority;
    }
    public void setModelId(Integer value) {
        this.modelId = value;
    }

    public Integer getModelId() {
        return this.modelId;
    }
    public void setAssignUrl(String value) {
        this.assignUrl = value;
    }

    public String getAssignUrl() {
        return this.assignUrl;
    }
    public void setWhereSql(String value) {
        this.whereSql = value;
    }

    public String getWhereSql() {
        return this.whereSql;
    }
    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }
    public void setKeyName(String value) {
        this.keyName = value;
    }

    public String getKeyName() {
        return this.keyName;
    }
    public void setValueName(String value) {
        this.valueName = value;
    }

    public String getValueName() {
        return this.valueName;
    }
    public void setDatasourceSql(String value) {
        this.datasourceSql = value;
    }

    public String getDatasourceSql() {
        return this.datasourceSql;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("Id",getId())
                .append("Creator",getCreator())
                .append("CreateDate",getCreateDate())
                .append("LastModifier",getLastModifier())
                .append("LastModDate",getLastModDate())
                .append("Status",getStatus())
                .append("Priority",getPriority())
                .append("ModelId",getModelId())
                .append("AssignUrl",getAssignUrl())
                .append("WhereSql",getWhereSql())
                .append("Name",getName())
                .append("KeyName",getKeyName())
                .append("ValueName",getValueName())
                .append("DatasourceSql",getDatasourceSql())
                .toString();
    }

    public int hashCode() {
        return new HashCodeBuilder()
                .append(getId())
                .toHashCode();
    }

    public boolean equals(Object obj) {
        if(obj instanceof DataModel == false) return false;
        if(this == obj) return true;
        DataModel other = (DataModel)obj;
        return new EqualsBuilder()
                .append(getId(), other.getId())
                .isEquals();
    }
}
