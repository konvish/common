package com.kong.common.managerui.service;

import com.kong.common.managerui.dao.IResourceDAO;
import com.kong.common.managerui.domain.Resource;
import com.kong.common.service.IBaseService;
import com.kong.common.service.IPageService;
/**
 * Created by kong on 2016/1/31.
 */
public interface IResourceService extends IBaseService<IResourceDAO,Resource>,IPageService<IResourceDAO,Resource>{
}
