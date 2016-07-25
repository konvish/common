package com.kong.common.protocol;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by kong on 2016/2/2.
 */
public class RequestT<T> implements Serializable {
    private static final long serialVersionUID = -5125919776534597878L;
    /*
    是否压缩
     */
    private String style;

    /*
    请求数据
     */
    private T data;

    /*
    请求头
     */
    private Map<String, Object> clientInfo;

    public RequestT() {
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map<String, Object> getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(Map<String, Object> clientInfo) {
        this.clientInfo = clientInfo;
    }

    @Override
    public String toString() {
        return "RequestT{" +
                "style='" + style + '\'' +
                ", data=" + data +
                ", clientInfo=" + clientInfo +
                '}';
    }
}
