package com.kong.kafka;

import com.kong.dap.dataservice.DapDataSender;
import com.kong.dap.dataservice.MessageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * Created by kong on 2016/1/3.
 */
public class DefaultKafkaProducer {
    public static final Logger logger = LoggerFactory.getLogger(DefaultKafkaProducer.class);
    public static final String HTTP_REQ = "httpReq";
    public static final String KAFKA_PREFIX = "RESOURCE_";
    public static final String HTTP_SERVER_FROM = "server";
    public static final String STR_APPEND = "_";
    public static final String HTTP_JS_FROM = "jsclient";
    private DapDataSender dapDataSender;

    private DefaultKafkaProducer() {
        this.dapDataSender = KafkaMQSingleton.getInstance();
    }

    public static DefaultKafkaProducer getInstance() {
        return DefaultKafkaProducer.DefaultKafkaProducerHolder.instance;
    }

    public void send(String product, String bizSystem, String tag, String from, String data) throws Exception {
        MessageData messageData = new MessageData("RESOURCE_" + (product + "_" + bizSystem + "_" + tag).toUpperCase(), data);
        this.dapDataSender.send(messageData);
    }

    public void stop() {
    }

    private static class DefaultKafkaProducerHolder {
        private static DefaultKafkaProducer instance = new DefaultKafkaProducer();

        private DefaultKafkaProducerHolder() {
        }
    }
}
