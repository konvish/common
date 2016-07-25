package com.kong.common.managerui.service;

import com.kong.common.managerui.dao.IDataModelDAO;
import com.kong.common.managerui.domain.DataModel;
import com.kong.common.service.IBaseService;
import com.kong.common.service.IPageService;
/**
 * Created by kong on 2016/1/31.
 */
public interface IDataModelService extends IBaseService<IDataModelDAO,DataModel>,IPageService<IDataModelDAO,DataModel>{
}

