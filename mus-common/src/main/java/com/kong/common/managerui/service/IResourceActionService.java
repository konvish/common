package com.kong.common.managerui.service;

import com.kong.common.managerui.dao.IResourceActionDAO;
import com.kong.common.managerui.domain.ResourceAction;
import com.kong.common.service.IBaseService;
import com.kong.common.service.IPageService;
/**
 * Created by kong on 2016/1/31.
 */
public interface IResourceActionService extends IBaseService<IResourceActionDAO,ResourceAction>,IPageService<IResourceActionDAO,ResourceAction>{
}
