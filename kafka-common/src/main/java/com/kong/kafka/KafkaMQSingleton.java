package com.kong.kafka;

import com.kong.cloudstack.context.CloudContextFactory;
import com.kong.cloudstack.dynconfig.DynConfigClientFactory;
import com.kong.dap.dataservice.DapConnectInfo;
import com.kong.dap.dataservice.DapDataSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by kong on 2016/1/3.
 */
public class KafkaMQSingleton {
    public static final Logger logger = LoggerFactory.getLogger(KafkaMQSingleton.class);
    private static DapDataSender instance = null;
    private static String RMQ_BROKER_URL = null;

    private KafkaMQSingleton() {
    }

    public static synchronized DapDataSender getInstance() {
        if(RMQ_BROKER_URL == null || RMQ_BROKER_URL.length() == 0) {
            try {
                RMQ_BROKER_URL = DynConfigClientFactory.getClient().getConfig("cmc", "common", "kafkaAddr");
            } catch (Exception var5) {
                logger.error("init RMQ broker url config error!", var5);
                System.exit(-1);
            }
        }

        if(RMQ_BROKER_URL == null || RMQ_BROKER_URL.length() == 0) {
            logger.error("init RMQ broker url config error! namesrvAddr is empty.");
            System.exit(-1);
        }

        if(instance == null) {
            String group = CloudContextFactory.getCloudContext().getProduct() + "-" + CloudContextFactory.getCloudContext().getApplicationName();
            String[] ipPorts = RMQ_BROKER_URL.split(",");
            DapConnectInfo dapConnectInfo = new DapConnectInfo(ipPorts);

            try {
                instance = new DapDataSender(dapConnectInfo);
            } catch (Exception var4) {
                logger.error("init kafka error", var4);
                System.exit(-1);
            }
        }

        return instance;
    }

}
