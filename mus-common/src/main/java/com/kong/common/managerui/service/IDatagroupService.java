package com.kong.common.managerui.service;

import com.kong.common.dao.IBaseDAO;
import com.kong.common.domain.BaseDomain;
import com.kong.common.service.IBaseService;
import com.kong.common.service.IPageService;
/**
 * Created by kong on 2016/1/31.
 */
public interface IDatagroupService<D extends IBaseDAO<T>, T extends BaseDomain> extends IBaseService<D, T>,IPageService<D, T>{

}