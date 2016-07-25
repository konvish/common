package com.kong.common.managerui.service;

import com.kong.common.managerui.dao.IRoleDAO;
import com.kong.common.managerui.domain.Role;
import com.kong.common.service.IBaseService;
import com.kong.common.service.IPageService;
/**
 * Created by kong on 2016/1/31.
 */
public interface IRoleService extends IBaseService<IRoleDAO,Role>,IPageService<IRoleDAO,Role>{
}

