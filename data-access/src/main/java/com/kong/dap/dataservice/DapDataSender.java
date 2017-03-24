package com.kong.dap.dataservice;

import java.util.List;

/**
 * 大数据分析平台数据接入发送端
 * Created by kong on 2016/7/27.
 */
public class DapDataSender {
    //发送工具
    private String dapDAType = "kafka";
    private DapConnectInfo dapConnectInfo;
    private IDapDataAccessService dapDataAccessService;


    public DapDataSender(DapConnectInfo dapConnectInfo) throws Exception {
        this(dapConnectInfo, "kafka");
    }

    public DapDataSender(DapConnectInfo dapConnectInfo, String dapDAType) throws Exception {
        this.dapConnectInfo = dapConnectInfo;
        if (dapDAType != null && !"".endsWith(dapDAType)) {
            this.dapDAType = dapDAType;
        } else {
            throw new Exception("dapDAType cant not be null!");
        }

        dapDataAccessService = DapDataAccessServiceFactory.getDataAccessService(this.dapDAType);
        dapDataAccessService.establishConnect(this.dapConnectInfo);

    }

    public void send(MessageData messageData) throws Exception {
        this.dapDataAccessService.send(messageData);
    }

    public void send(List<MessageData> messageDataList) throws Exception {
        this.dapDataAccessService.send(messageDataList);
    }

    public void close() {
        this.dapDataAccessService.closeConnect();
    }
}
