package com.kong.rmq;

import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by kong on 2016/2/2.
 */
public class DefaultRMQProducer {
    public static final Logger logger = LoggerFactory.getLogger(DefaultRMQProducer.class);
    public static final String HTTP_REQ = "httpReq";
    public static final String HTTP_SERVER_FROM = "server";
    public static final String HTTP_JS_FROM = "jsclient";
    private DefaultMQProducer producer;

    private DefaultRMQProducer() {
        this.producer = RocketMQSingleton.getInstance();
    }

    public static DefaultRMQProducer getInstance() {
        return DefaultRMQProducer.DefaultRMQProducerHolder.instance;
    }

    public void send(String product, String bizSystem, String tag, String from, String data) throws Exception {
        this.send(product, bizSystem, tag, from, data, (SendCallback)null);
    }

    public void send(String product, String bizSystem, String tag, String from, String data, SendCallback callback) throws Exception {
        Message message = new Message(product, tag, data.getBytes());
        ArrayList keys = new ArrayList(2);
        keys.add(product);
        keys.add(bizSystem);
        keys.add(from);
        message.setKeys(keys);
        if(callback != null) {
            this.producer.send(message, callback);
        } else {
            this.producer.send(message);
        }

    }

    public void stop() {
        if(this.producer != null) {
            logger.debug("Closing rocketMQ producer ");
            this.producer.shutdown();
        }

    }

    private static class DefaultRMQProducerHolder {
        private static DefaultRMQProducer instance = new DefaultRMQProducer();

        private DefaultRMQProducerHolder() {
        }
    }
}

