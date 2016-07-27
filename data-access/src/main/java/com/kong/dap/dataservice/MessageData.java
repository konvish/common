package com.kong.dap.dataservice;

/**
 * 消息实体类
 * Created by kong on 2016/7/27.
 */
public class MessageData {
    //资源类型，有大数据分析平台统一提供
    private String resourceName;
    //消息ID，可以为空
    private String messageId;
    //消息数据
    private String data;
    private byte[] binaryData;

    public MessageData(String resourceName, String data) {
        this.resourceName = resourceName;
        this.data = data;
    }

    public MessageData(String resourceName, String messageId, String data) {
        this.resourceName = resourceName;
        this.messageId = messageId;
        this.data = data;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }
}
