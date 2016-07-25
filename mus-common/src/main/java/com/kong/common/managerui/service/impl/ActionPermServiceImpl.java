package com.kong.common.managerui.service.impl;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kong.common.managerui.dao.IPermissionDAO;
import com.kong.common.managerui.domain.Resource;
import com.kong.common.managerui.domain.ResourceAction;
import com.kong.common.managerui.service.IActionPermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by kong on 2016/1/31.
 */
@Service("actionPermServiceImpl")
public class ActionPermServiceImpl implements IActionPermService {
    @Autowired
    private IPermissionDAO permissionDAO;

    @Override
    public List<Resource> getResourcePerms(Object userId) {
        return getResourcePerms(userId, null);
    }

    @Override
    public List<Resource> getResourcePerms(Object userId,String product) {
        return permissionDAO.getResByPerm(userId,product,null);
    }

    @Override
    public List<Resource> getResourcePerms(Object userId,String product,String hide) {
        return permissionDAO.getResByPerm(userId,product,hide);
    }

    @Override
    public Set<String> getActionPermsByRes(Object userId, Object resourceId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", userId);
        params.put("resourceId", resourceId);
        List<ResourceAction> resourceActionList = permissionDAO.getResActionByPerm(params);
        Set<String> actionSet = Sets.newHashSet();
        for(ResourceAction resourceAction : resourceActionList){
            actionSet.add(resourceAction.getActionAlias());
        }
        return actionSet;
    }

    @Override
    public Map<String, Set<String>> getActionPerms(Object userId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("userId", userId);
        List<ResourceAction> resourceActionList = permissionDAO.getResActionByPerm(params);

        Map<String, Set<String>> actionPerms = Maps.newHashMap();
        Set<String> perms = null;
        for(ResourceAction resourceAction : resourceActionList){
            perms = actionPerms.get(resourceAction.getResourceId());
            if(perms == null){
                perms = Sets.newHashSet();
            }
            perms.add(resourceAction.getActionAlias());

            actionPerms.put(String.valueOf(resourceAction.getResourceId()), perms);
        }

        return actionPerms;
    }
}
