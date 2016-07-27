package com.kong.dap.dataservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * 大数据分析平台数据接入发送端
 * Created by kong on 2016/7/27.
 */
public class DapDataReceiver {
    private String dapDAType = "kafka";
    private DapConnectInfo dapConnectInfo;
    private IDapDataAccessService dapDataAccessService;
    private Logger logger = LoggerFactory.getLogger(DapDataReceiver.class);

    public DapDataReceiver(DapConnectInfo dapConnectInfo) throws Exception {
        this("kafka",dapConnectInfo);
    }

    public DapDataReceiver(String dapDAType, DapConnectInfo dapConnectInfo) throws Exception {
        this.dapConnectInfo = dapConnectInfo;
        if (dapDAType!=null && !"".equals(dapDAType))
            this.dapDAType = dapDAType;
        else
            throw new Exception("dapDAType can not be null!");

        dapDataAccessService = DapDataAccessServiceFactory.getDataAccessService(this.dapDAType);
    }

    public void registerListener(Properties revProperty, ResourceListener listener) throws Exception {
        if (revProperty == null || revProperty.getProperty("resourceName") == null) {
            throw new RuntimeException("revProperty must be put 'resourceName'.");
        }

        dapDataAccessService.establishConnectReceiver(this.dapConnectInfo, revProperty);
        dapDataAccessService.registerListener(listener);
    }

    public void start() {
        try {
            dapDataAccessService.startReceiver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        this.dapDataAccessService.closeConnectReceiver();
    }
}
