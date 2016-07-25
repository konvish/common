package com.kong.common.managerui.service.impl;

import com.google.common.collect.Maps;
import com.kong.common.domain.UserDomain;
import com.kong.common.managerui.dao.IDataModelDAO;
import com.kong.common.managerui.dao.IPermissionDAO;
import com.kong.common.managerui.dao.IResourceDAO;
import com.kong.common.managerui.dao.IUserDataDAO;
import com.kong.common.managerui.domain.DataModel;
import com.kong.common.managerui.domain.DatagroupData;
import com.kong.common.managerui.domain.Resource;
import com.kong.common.service.IDataPermService;
import com.kong.common.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 数据权限 service
 * Created by kong on 2016/1/31.
 */
@Service("dataPermServiceImpl")
public class DataPermServiceImpl implements IDataPermService {
    @Autowired
    private IDataModelDAO dataModelDAO;
    @Autowired
    private IResourceDAO resourceDAO;
    @Autowired
    private IUserDataDAO userDataDAO;
    @Autowired
    private IPermissionDAO permissionDAO;

    @Override
    public String makeDataPermSql(String resUrl) {
        UserDomain user = UserContext.getCurrentUser();

        Resource resource = resourceDAO.findOne("url", resUrl, null, null);

        int modelId = 0;
        if (resource != null && resource.getModelId() != null) {
            modelId = resource.getModelId();
        } else {
            //没有主模型，说明没有数据权限设置
            return null;
        }
        DataModel dataModel = dataModelDAO.findOne("modelId", modelId, null, null); //dataModelDAO.getDataPermSql(modelId);
        if (dataModel == null) {
            return null;
        }
        String formatSql = dataModel.getWhereSql();
        StringBuilder stringBuilder = new StringBuilder("");
        Map<String, Object> paramMap = Maps.newHashMap();
        paramMap.put("dataModelId", dataModel.getModelId());
        paramMap.put("userId", user.getId());
        //List<UserData> dataIds = userDataDAO.queryList(paramMap, null, null);
        List<DatagroupData> dataIds = permissionDAO.getDataByPerm(paramMap);


        int size = dataIds.size();

        if (size <= 1) {
            return String.format(formatSql, stringBuilder);
        } else {
            for (DatagroupData userData : dataIds) {
                stringBuilder.append(userData.getDataId()).append(",");
            }
            return String.format(formatSql, stringBuilder.deleteCharAt(stringBuilder.length() - 1));
        }

    }
}
