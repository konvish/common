package com.kong.common.managerui.dao;

import com.kong.common.managerui.domain.DatagroupData;
import com.kong.common.managerui.domain.Resource;
import com.kong.common.managerui.domain.ResourceAction;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 权限专有的DAO
 * Created by kong on 2016/1/31.
 */
public interface IPermissionDAO {
    public List<Resource> getResByPerm(Object userId);

    public List<Resource> getResByPerm(@Param("userId")Object userId,@Param("product")String product);

    public List<Resource> getResByPerm(@Param("userId")Object userId,@Param("product")String product, @Param("hide")String hide);

    public List<ResourceAction> getResActionByPerm(@Param("condition")Map<String, Object> params);

    public List<DatagroupData> getDataByPerm(@Param("condition")Map<String, Object> params);
}