package com.kong.common.managerui.service;

import com.kong.common.managerui.dao.IRoleUserDAO;
import com.kong.common.managerui.domain.RoleUser;
import com.kong.common.service.IBaseService;
import com.kong.common.service.IPageService;
/**
 * Created by kong on 2016/1/31.
 */
public interface IRoleUserService extends IBaseService<IRoleUserDAO,RoleUser>,IPageService<IRoleUserDAO,RoleUser>{
}