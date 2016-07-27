package com.kong.dap.dataservice;

import java.util.List;
import java.util.Properties;

/**
 * 大数据分析平台数据访问服务接口
 * Created by kong on 2016/7/27.
 */
public interface IDapDataAccessService {
    /**
     * 建立连接
     * @param dapConnectInfo 连接信息
     * @throws Exception
     */
    void establishConnect(DapConnectInfo dapConnectInfo) throws Exception;

    /**
     * 建立连接
     * @param dapConnectInfo
     * @param sendProperty
     * @throws Exception
     */
    void establishConnect(DapConnectInfo dapConnectInfo, Properties sendProperty) throws Exception;

    /**
     * 发送一条消息
     *
     * @param messageData
     */
    void send(MessageData messageData) throws Exception;

    /**
     *发送多条消息
     *
     * @param messageDataList
     */
    void send(List<MessageData> messageDataList) throws Exception;

    /**
     *连接关闭
     */
    void closeConnect();

    /**
     *建立接收连接
     *
     * @param dapConnectInfo
     */
    void establishConnectReceiver(DapConnectInfo dapConnectInfo) throws Exception;

    /**
     *建立接收连接
     *
     * @param dapConnectInfo
     * @param sendProperty
     */
    void establishConnectReceiver(DapConnectInfo dapConnectInfo,Properties sendProperty) throws Exception;

    /**
     * 注册监听
     *
     * @param listener
     */
    void registerListener(ResourceListener listener) throws Exception;

    /**
     * 启动接收
     * @throws Exception
     */
    void startReceiver() throws Exception;

    /**
     * 关闭接收连接
     */
    void closeConnectReceiver();
}
