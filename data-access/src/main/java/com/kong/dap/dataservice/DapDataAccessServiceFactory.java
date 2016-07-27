package com.kong.dap.dataservice;

import com.kong.dap.dataservice.impl.KafkaDapDataAccessServiceImpl;
import com.kong.dap.dataservice.impl.KafkaRestProxyDataAccessServiceImpl;

/**
 * 大数据分析平台数据访问服务工厂类
 * Created by kong on 2016/7/27.
 */
public class DapDataAccessServiceFactory {

    /**
     * 工厂方法，根据接入类型生成相应的服务对象
     *
     * @param dapDAType 接入类型，默认kafka
     * @return
     */
    public static IDapDataAccessService getDataAccessService(String dapDAType){
        IDapDataAccessService dapDataAccessService = null;
        if ("kafka".equalsIgnoreCase(dapDAType)){
            dapDataAccessService = new KafkaDapDataAccessServiceImpl();
        }else if ("rest".equalsIgnoreCase(dapDAType))
            dapDataAccessService = new KafkaRestProxyDataAccessServiceImpl();
        return dapDataAccessService;
    }
}
