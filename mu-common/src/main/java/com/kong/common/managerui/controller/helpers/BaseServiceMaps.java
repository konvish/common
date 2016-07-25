package com.kong.common.managerui.controller.helpers;

import com.google.common.collect.Maps;
import com.kong.common.managerui.service.*;
import com.kong.common.service.IBaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by Administrator on 2016/1/3.
 */
public class BaseServiceMaps {
    @Autowired
    private IModelService modelService;
    @Autowired
    private IResourceService resourceService;
    @Autowired
    private IResourceActionService resourceActionService;
    @Autowired
    private IResourceGridService resourceGridService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IRoleResourceService roleResourceService;
    @Autowired
    private IRoleUserService roleUserService;
    @Autowired
    private IUserDataService userDataService;
    @Autowired
    private IDatagroupDataService datagroupDataService;
    @Autowired
    private IDatagroupService datagroupService;
    @Autowired
    private IDataModelService dataModelService;
    @Autowired
    private IUserDatagroupService userDatagroupService;
    protected final Map<String, IBaseService> serviceMap = Maps.newHashMap();

    public BaseServiceMaps() {
    }

    protected void init() {
        this.serviceMap.put("datamodel", this.dataModelService);
        this.serviceMap.put("model", this.modelService);
        this.serviceMap.put("resource", this.resourceService);
        this.serviceMap.put("resourceaction", this.resourceActionService);
        this.serviceMap.put("resourcegrid", this.resourceGridService);
        this.serviceMap.put("role", this.roleService);
        this.serviceMap.put("roleresource", this.roleResourceService);
        this.serviceMap.put("roleuser", this.roleUserService);
        this.serviceMap.put("userdata", this.userDataService);
        this.serviceMap.put("datagroupData".toLowerCase(), this.datagroupDataService);
        this.serviceMap.put("datagroup".toLowerCase(), this.datagroupService);
        this.serviceMap.put("userDatagroup".toLowerCase(), this.userDatagroupService);
    }

    public IBaseService get(String mainObj) {
        return (IBaseService)this.serviceMap.get(mainObj);
    }

}
