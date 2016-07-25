package com.kong.rmq;

import com.kong.cloudstack.context.CloudContextFactory;
import com.kong.cloudstack.dynconfig.DynConfigClientFactory;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kong on 2016/2/2.
 */
public class RocketMQSingleton {
    public static final Logger logger = LoggerFactory.getLogger(RocketMQSingleton.class);
    private static DefaultMQProducer instance = null;
    private static String RMQ_BROKER_URL = null;

    private RocketMQSingleton() {
    }

    public static synchronized DefaultMQProducer getInstance() {
        if(RMQ_BROKER_URL == null || RMQ_BROKER_URL.length() == 0) {
            try {
                RMQ_BROKER_URL = DynConfigClientFactory.getClient().getConfig("cmc", "common", "namesrvAddr");
            } catch (Exception var3) {
                logger.error("init RMQ broker url config error!", var3);
                System.exit(-1);
            }
        }

        if(RMQ_BROKER_URL == null || RMQ_BROKER_URL.length() == 0) {
            logger.error("init RMQ broker url config error! namesrvAddr is empty.");
            System.exit(-1);
        }

        if(instance == null) {
            String group = CloudContextFactory.getCloudContext().getProduct() + "-" + CloudContextFactory.getCloudContext().getApplicationName();
            instance = new DefaultMQProducer(group);
            instance.setNamesrvAddr(RMQ_BROKER_URL);

            try {
                instance.start();
            } catch (MQClientException var2) {
                logger.error("init rocketMQ error", var2);
                System.exit(-1);
            }
        }

        return instance;
    }
}
