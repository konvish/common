package com.kong.common.managerui.service;

import com.kong.common.managerui.dao.IRoleResourceDAO;
import com.kong.common.managerui.domain.RoleResource;
import com.kong.common.service.IBaseService;
import com.kong.common.service.IPageService;
/**
 * Created by kong on 2016/1/31.
 */
public interface IRoleResourceService extends IBaseService<IRoleResourceDAO,RoleResource>,IPageService<IRoleResourceDAO,RoleResource>{
}
