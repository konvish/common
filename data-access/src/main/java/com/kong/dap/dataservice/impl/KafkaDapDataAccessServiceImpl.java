package com.kong.dap.dataservice.impl;

import com.kong.dap.dataservice.DapConnectInfo;
import com.kong.dap.dataservice.IDapDataAccessService;
import com.kong.dap.dataservice.MessageData;
import com.kong.dap.dataservice.ResourceListener;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 大数据分析平台数据访问服务Kafka实现
 * Created by kong on 2016/7/27.
 */
public class KafkaDapDataAccessServiceImpl implements IDapDataAccessService {

    //生产端
    private KafkaProducer<Integer, String> producer = null;
    private Logger logger = LoggerFactory.getLogger(KafkaDapDataAccessServiceImpl.class);

    //消费端
    private DapConnectInfo dapConnectInfo;
    private ConsumerConnector consumer = null;
    private ResourceListener listener;
    private Properties properties = new Properties();

    @Override
    public void establishConnect(DapConnectInfo dapConnectInfo) throws Exception {
        Properties props = new Properties();
        props.put("serializer.class","kafka.serializer.StringEncoder");
        props.put("producer.type","async");
        props.put("metadata.broker.list",dapConnectInfo.toString());
        producer = new KafkaProducer<Integer,String>(props);
    }

    @Override
    public void establishConnect(DapConnectInfo dapConnectInfo, Properties sendProperty) throws Exception {

    }

    @Override
    public void send(MessageData messageData) throws Exception {
        String topic = messageData.getResourceName();
        String messageStr = messageData.getData();
        producer.send(new ProducerRecord<Integer, String>(topic,messageStr));
    }

    @Override
    public void send(List<MessageData> messageDataList) throws Exception {
        for (MessageData messageData:messageDataList) {
            String topic = messageData.getResourceName();
            String messageStr = messageData.getData();
            producer.send(new ProducerRecord<Integer, String>(topic,messageStr));
        }
    }

    @Override
    public void closeConnect() {
        producer.close();
    }

    @Override
    public void establishConnectReceiver(DapConnectInfo dapConnectInfo) throws Exception {
        this.establishConnectReceiver(dapConnectInfo,null);
    }

    @Override
    public void establishConnectReceiver(DapConnectInfo dapConnectInfo, Properties sendProperty) throws Exception {
        this.dapConnectInfo = dapConnectInfo;
        this.properties = sendProperty;
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(this.properties));
    }

    @Override
    public void registerListener(ResourceListener listener) throws Exception {
        this.listener = listener;
    }

    @Override
    public void startReceiver() throws Exception {
        String resourceName = this.properties.getProperty("resourceName");
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(resourceName, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = consumerMap.get(resourceName).get(0);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while (it.hasNext()) {
            MessageData messageData = new MessageData(resourceName, new String(it.next().message()));
            this.listener.onEvent(messageData);
        }
    }

    @Override
    public void closeConnectReceiver() {
        if (consumer != null) {
            consumer.shutdown();
        }
    }
}
