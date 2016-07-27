package com.kong.dap.dataservice;

/**
 * 资源变更监听器
 * Created by kong on 2016/7/27.
 */
public interface ResourceListener {
    /**
     * 资源事件变更回调接口
     * @param messageData
     */
    void onEvent(MessageData messageData);
}
