package com.kong.common.managerui.service;

import com.kong.common.managerui.dao.IModelDAO;
import com.kong.common.managerui.domain.Model;
import com.kong.common.service.IBaseService;
import com.kong.common.service.IPageService;
/**
 * Created by kong on 2016/1/31.
 */
public interface IModelService extends IBaseService<IModelDAO,Model>,IPageService<IModelDAO,Model>{
}
