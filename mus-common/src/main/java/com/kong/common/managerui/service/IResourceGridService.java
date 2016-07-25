package com.kong.common.managerui.service;

import com.kong.common.managerui.dao.IResourceGridDAO;
import com.kong.common.managerui.domain.ResourceGrid;
import com.kong.common.service.IBaseService;
import com.kong.common.service.IPageService;
/**
 * Created by kong on 2016/1/31.
 */
public interface IResourceGridService extends IBaseService<IResourceGridDAO,ResourceGrid>,IPageService<IResourceGridDAO,ResourceGrid>{
}
