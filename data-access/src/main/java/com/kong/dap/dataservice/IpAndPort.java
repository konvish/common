package com.kong.dap.dataservice;

/**
 * 封装Ip和端口
 * Created by kong on 2016/7/27.
 */
public class IpAndPort {
    private String ip;
    private int port;

    public IpAndPort(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
