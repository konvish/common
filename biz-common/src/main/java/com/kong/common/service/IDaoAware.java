package com.kong.common.service;

import com.kong.common.dao.IBaseDAO;
import com.kong.common.domain.BaseDomain;

/**
 * 业务主对象DAO注入
 * Created by kong on 2016/1/3.
 */
public interface IDaoAware<D extends IBaseDAO,T extends BaseDomain> {
    public D getDao();
}
