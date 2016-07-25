package com.kong.common.managerui.service;

import com.kong.common.managerui.dao.IUserDataDAO;
import com.kong.common.managerui.domain.UserData;
import com.kong.common.service.IBaseService;
import com.kong.common.service.IPageService;
/**
 * Created by kong on 2016/1/31.
 */
public interface IUserDataService extends IBaseService<IUserDataDAO,UserData>,IPageService<IUserDataDAO,UserData>{
}