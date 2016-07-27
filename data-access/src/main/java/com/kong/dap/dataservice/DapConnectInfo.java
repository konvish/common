package com.kong.dap.dataservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据分析平台服务连接信息
 * Created by kong on 2016/7/27.
 */
public class DapConnectInfo {
    private List<IpAndPort> ipAndPorts = new ArrayList<IpAndPort>();
    private static Logger logger = LoggerFactory.getLogger(DapConnectInfo.class);

    public DapConnectInfo(String[] ipAndPort) {
        if (ipAndPort == null || ipAndPort.length == 0){
            logger.error("service ip or port is null,check it");
            System.exit(-1);
        }
        for (String ipPort : ipAndPort) {
            String ip = ipPort.split(":")[0];
            int port = Integer.parseInt(ipPort.split(":")[1]);
            IpAndPort ipAndPort1 = new IpAndPort(ip,port);
            this.ipAndPorts.add(ipAndPort1);
        }
    }

    public String getFirstIp(){
        return ipAndPorts.get(0).getIp();
    }

    public int getFirstPort(){
        return ipAndPorts.get(0).getPort();
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < this.ipAndPorts.size(); i++) {
            IpAndPort ipAndPort = ipAndPorts.get(i);
            if (i<=this.ipAndPorts.size()-1)
                sb.append(ipAndPort.getIp()).append(":").append(ipAndPort.getPort()).append(",");
        }
        return sb.toString();
    }
}
