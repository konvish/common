package com.kong.common.protocol;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by kong on 2016/2/2.
 */
public class Request implements Serializable {

    private static final long   serialVersionUID = 4110747075355197761L;
    /*
    是否压缩
     */
    private String              style;

    /*
    请求数据
     */
    private Map<String, Object> data;

    /*
    请求头
     */
    private Map<String, Object> clientInfo;

    public Request() {
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public Map<String, Object> getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(Map<String, Object> clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String getDataString(String key) {
        return data.get(key) == null ? "" : data.get(key).toString();
    }

    public Integer getDataInteger(String key) {
        return Integer.parseInt(data.get(key) == null ? "" : data.get(key).toString());
    }

    public Long getDataLong(String key) {
        return Long.parseLong(data.get(key) == null ? "" : data.get(key).toString());
    }

    public List<Integer> getDataListInteger(String key) {
        return (List<Integer>)(data.get(key));
    }

    public String getClientInfoString(String key) {
        return clientInfo.get(key) == null ? "" : clientInfo.get(key).toString();
    }

    @Override
    public String toString() {
        return "RequestBody{" + "style='" + style + '\'' + ", data=" + data + ", clientInfo="
                + clientInfo + '}';
    }
}
