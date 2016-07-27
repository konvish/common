package com.kong.dap.dataservice.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kong.dap.dataservice.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * RestProxy方式
 * Created by kong on 2016/7/27.
 */
public class KafkaRestProxyDataAccessServiceImpl implements IDapDataAccessService {

    //生产端
    private CloseableHttpClient httpclient;
    private DapConnectInfo dapConnectInfo;

    //消费端
    private DapConnectInfo dapConnectInfoRev;
    private String baseUrlRev;
    private ResourceListener listenerRev;
    private Properties propertyRev = new Properties();

    private Logger logger = LoggerFactory.getLogger(KafkaRestProxyDataAccessServiceImpl.class);

    @Override
    public void establishConnect(DapConnectInfo dapConnectInfo) throws Exception {
        this.establishConnect(dapConnectInfo, null);
    }

    @Override
    public void establishConnect(DapConnectInfo dapConnectInfo, Properties sendProperty) throws Exception {
        this.httpclient = HttpClients.createDefault();
        this.dapConnectInfo = dapConnectInfo;
    }

    @Override
    public void send(MessageData messageData) throws Exception {
        String topic = messageData.getResourceName();
        String messageStr = messageData.getData();
        String data = this.buildKafkaProxyData(messageStr);
        HttpPost httpPost = this.buildKafkaProxyHttpPost(topic, data);
        processResponse(httpclient.execute(httpPost));
    }

    @Override
    public void send(List<MessageData> messageDataList) throws Exception {
        Map<String, List<String>> topicMessageMap = new HashMap<>();
        for (MessageData messageData : messageDataList) {
            String topic = messageData.getResourceName();
            String messageStr = messageData.getData();
            if (topicMessageMap.get(topic) == null) {
                topicMessageMap.put(topic, new ArrayList<String>());
            }
            topicMessageMap.get(topic).add(messageStr);
        }

        for (Map.Entry<String, List<String>> me : topicMessageMap.entrySet()) {
            String topic = me.getKey();
            List<String> messageList = me.getValue();
            String data = this.buildKafkaProxyData(messageList);
            HttpPost httpPost = this.buildKafkaProxyHttpPost(topic, data);
            processResponse(httpclient.execute(httpPost));
        }
    }

    @Override
    public void closeConnect() {
        try {
            httpclient.close();
            logger.info("httpclient is closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void establishConnectReceiver(DapConnectInfo dapConnectInfo) throws Exception {
        throw new RuntimeException("KafkaRestProxyDataAccessServiceImpl.establishConnectReceiver not available!");
    }

    @Override
    public void establishConnectReceiver(DapConnectInfo dapConnectInfo, Properties sendProperty) throws Exception {
        this.dapConnectInfoRev = dapConnectInfo;
        this.propertyRev = sendProperty;
        String clientId = sendProperty.getProperty("clientId");
        if (clientId == null || "".endsWith(clientId)) {
            throw new RuntimeException("clientId cant not be null!");
        }
        this.deleteOldBaseUrlRev();
        this.initBaseUrlRev();
        logger.info("receiver has prepared to start!");
    }

    @Override
    public void registerListener(ResourceListener listener) throws Exception {
        this.listenerRev = listener;
    }

    @Override
    public void startReceiver() throws Exception {
        ReceiverLongPolling receiverLongPolling;
        String pollIntervalStr = propertyRev.getProperty("pollInterval");
        String resourceName = propertyRev.getProperty("resourceName");

        if (pollIntervalStr != null) {
            long poolInterval = Long.parseLong(pollIntervalStr);
            if (poolInterval <= 0) {
                throw new IllegalArgumentException("pollInterval can not be negative or zero !");
            }
            receiverLongPolling = new ReceiverLongPolling(this.baseUrlRev, poolInterval, resourceName, listenerRev);

        } else {
            receiverLongPolling = new ReceiverLongPolling(this.baseUrlRev, resourceName, listenerRev);
        }

        new Thread(receiverLongPolling).start();
        logger.info("start to receive.");
    }

    @Override
    public void closeConnectReceiver() {

    }

    private String buildKafkaProxyData(String message) {
        return "{\"records\":[" + this.getRecordValue(message) + "]}";
    }

    private String getRecordValue(String message) {
        return "{\"value\":\"" + this.encodeBase64(message.getBytes()) + "\"}";
    }

    private String encodeBase64(final byte[] bytes) {
        return new String(Base64.encodeBase64(bytes));
    }

    private HttpPost buildKafkaProxyHttpPost(String topic, String data) {
        HttpPost httpPost = new HttpPost("http://" + dapConnectInfo.getFirstIp() + ":" + dapConnectInfo.getFirstPort() + "/topics/" + topic);
        StringEntity entity = null;
        try {
            entity = new StringEntity(data);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        entity.setContentType("application/vnd.kafka.binary.v1+json");
        httpPost.setEntity(entity);
        return httpPost;
    }

    private String buildKafkaProxyData(List<String> messageList) {
        StringBuffer recordBuffer = new StringBuffer("{\"records\":[");
        for (String message : messageList) {
            recordBuffer.append(this.getRecordValue(message)).append(",");
        }
        recordBuffer.deleteCharAt(recordBuffer.length() - 1);
        recordBuffer.append("]}");
        return recordBuffer.toString();
    }

    private void processResponse(CloseableHttpResponse response) {
        try {
            logger.info(response.getStatusLine().toString());
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                logger.info("Response content length: " + resEntity.getContentLength());
            }
            EntityUtils.consume(resEntity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initBaseUrlRev() throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        String clientId = this.propertyRev.getProperty("clientId");
        HttpPost httpPost = new HttpPost("http://" + dapConnectInfoRev.getFirstIp() + ":" + dapConnectInfoRev.getFirstPort() + "/consumers/group_" + clientId);
        String data = "{\"format\": \"binary\", \"auto.offset.reset\": \"largest\",\"id\":\"" + clientId + "\",\"auto.commit.enable\":false}";
        StringEntity entity = new StringEntity(data);
        entity.setContentType("application/vnd.kafka.v1+json");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            logger.info(response.getStatusLine().toString());
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                logger.debug("Response content length: " + resEntity.getContentLength());
                String result = Utils.inputStream2Str(resEntity.getContent());
                logger.info(result);
                this.baseUrlRev = JSONObject.parseObject(result).getString("base_uri");
            }

        } finally {
            response.close();
        }
        httpclient.close();

    }

    private void deleteOldBaseUrlRev() throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            String clientId = this.propertyRev.getProperty("clientId");
            HttpDelete httpDelete = new HttpDelete("http://" + dapConnectInfoRev.getFirstIp() + ":" + dapConnectInfoRev.getFirstPort() + "/consumers/group_" + clientId + "/instances/" + clientId);
            CloseableHttpResponse response = httpclient.execute(httpDelete);
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("delete old base Url failed");
        } finally {
            httpclient.close();
        }
    }
}

class ReceiverLongPolling implements Runnable {
    private long pollIntervalSec = 5;
    private ResourceListener resourceListener;
    private CloseableHttpClient httpclient;
    private String baseUrlRev;
    private String resourceName;
    private Logger logger = LoggerFactory.getLogger(ReceiverLongPolling.class);
    private volatile boolean isRun = true;

    public ReceiverLongPolling(String baseUrlRev, String resourceName, ResourceListener resourceListener) {
        this.resourceListener = resourceListener;
        this.httpclient = HttpClients.createDefault();
        this.baseUrlRev = baseUrlRev;
        this.resourceName = resourceName;
    }

    public ReceiverLongPolling(String baseUrlRev, long pollIntervalSec, String resourceName, ResourceListener resourceListener) {
        this.resourceListener = resourceListener;
        this.pollIntervalSec = pollIntervalSec;
        this.httpclient = HttpClients.createDefault();
        this.baseUrlRev = baseUrlRev;
        this.resourceName = resourceName;
    }

    private String decodeBase64(final String base64Str) {
        return new String(Base64.decodeBase64(base64Str));
    }


    public String readMessage() throws Exception {
        String result = null;
        HttpGet httpGet = new HttpGet(this.baseUrlRev + "/topics/" + this.resourceName);
        httpGet.setHeader("Accept", "application/vnd.kafka.binary.v1+json");
        httpGet.setHeader("Content-Type", "application/vnd.kafka.binary.v1+json");
        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            logger.debug(response.getStatusLine().toString());
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                logger.debug("Response content length: " + resEntity.getContentLength());
                result = Utils.inputStream2Str(resEntity.getContent());
                logger.debug("result:" + result);
            }

        } finally {
            response.close();
        }
        return result;
    }

    //{"error_code":40403,"message":"Consumer instance not found. io.confluent.rest.exceptions.RestNotFoundException: Consumer instance not found.\nio.confluent.rest.exceptions.RestNotFoundException: Consumer instance not found.\n\tat io.confluent.kafkarest.Errors.consumerInstanceNotFoundException(Errors.java:50)\n\tat io.confluent.kafkarest.ConsumerManager.getConsumerInstance(ConsumerManager.java:310)\n\tat io.confluent.kafkarest.ConsumerManager.getConsumerInstance(ConsumerManager.java:320)\n\tat io.confluent.kafkarest.ConsumerManager.readTopic(ConsumerManager.java:193)\n\tat io.confluent.kafkarest.resources.ConsumersResource.readTopic(ConsumersResource.java:142)\n\tat io.confluent.kafkarest.resources.ConsumersResource.readTopicBinary(ConsumersResource.java:119)\n\tat sun.reflect.GeneratedMethodAccessor26.invoke(Unknown Source)\n\tat sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n\tat java.lang.reflect.Method.invoke(Method.java:606)\n\tat org.glassfish.jersey.server.model.internal.ResourceMethodInvocationHandlerFactory$1.invoke(ResourceMethodInvocationHandlerFactory.java:81)\n\tat org.glassfish.jersey.server.model.internal.AbstractJavaResourceMethodDispatcher$1.run(AbstractJavaResourceMethodDispatcher.java:151)\n\tat org.glassfish.jersey.server.model.internal.AbstractJavaResourceMethodDispatcher.invoke(AbstractJavaResourceMethodDispatcher.java:171)\n\tat org.glassfish.jersey.server.model.internal.JavaResourceMethodDispatcherProvider$VoidOutInvoker.doDispatch(JavaResourceMethodDispatcherProvider.java:136)\n\tat org.glassfish.jersey.server.model.internal.AbstractJavaResourceMethodDispatcher.dispatch(AbstractJavaResourceMethodDispatcher.java:104)\n\tat org.glassfish.jersey.server.model.ResourceMethodInvoker.invoke(ResourceMethodInvoker.java:406)\n\tat org.glassfish.jersey.server.model.ResourceMethodInvoker.apply(ResourceMethodInvoker.java:350)\n\tat org.glassfish.jersey.server.model.ResourceMethodInvoker.apply(ResourceMethodInvoker.java:106)\n\tat org.glassfish.jersey.server.ServerRuntime$1.run(ServerRuntime.java:259)\n\tat org.glassfish.jersey.internal.Errors$1.call(Errors.java:271)\n\tat org.glassfish.jersey.internal.Errors$1.call(Errors.java:267)\n\tat org.glassfish.jersey.internal.Errors.process(Errors.java:315)\n\tat org.glassfish.jersey.internal.Errors.process(Errors.java:297)\n\tat org.glassfish.jersey.internal.Errors.process(Errors.java:267)\n\tat org.glassfish.jersey.process.internal.RequestScope.runInScope(RequestScope.java:319)\n\tat org.glassfish.jersey.server.ServerRuntime.process(ServerRuntime.java:236)\n\tat org.glassfish.jersey.server.ApplicationHandler.handle(ApplicationHandler.java:1028)\n\tat org.glassfish.jersey.servlet.WebComponent.service(WebComponent.java:373)\n\tat org.glassfish.jersey.servlet.ServletContainer.service(ServletContainer.java:381)\n\tat org.glassfish.jersey.servlet.ServletContainer.service(ServletContainer.java:344)\n\tat org.glassfish.jersey.servlet.ServletContainer.service(ServletContainer.java:219)\n\tat org.eclipse.jetty.servlet.ServletHolder.handle(ServletHolder.java:684)\n\tat org.eclipse.jetty.servlet.ServletHandler.doHandle(ServletHandler.java:503)\n\tat org.eclipse.jetty.server.session.SessionHandler.doHandle(SessionHandler.java:229)\n\tat org.eclipse.jetty.server.handler.ContextHandler.doHandle(ContextHandler.java:1086)\n\tat org.eclipse.jetty.servlet.ServletHandler.doScope(ServletHandler.java:429)\n\tat org.eclipse.jetty.server.session.SessionHandler.doScope(SessionHandler.java:193)\n\tat org.eclipse.jetty.server.handler.ContextHandler.doScope(ContextHandler.java:1020)\n\tat org.eclipse.jetty.server.handler.ScopedHandler.handle(ScopedHandler.java:135)\n\tat org.eclipse.jetty.server.handler.HandlerCollection.handle(HandlerCollection.java:154)\n\tat org.eclipse.jetty.server.handler.HandlerWrapper.handle(HandlerWrapper.java:116)\n\tat org.eclipse.jetty.server.Server.handle(Server.java:370)\n\tat org.eclipse.jetty.server.AbstractHttpConnection.handleRequest(AbstractHttpConnection.java:494)\n\tat org.eclipse.jetty.server.AbstractHttpConnection.headerComplete(AbstractHttpConnection.java:971)\n\tat org.eclipse.jetty.server.AbstractHttpConnection$RequestHandler.headerComplete(AbstractHttpConnection.java:1033)\n\tat org.eclipse.jetty.http.HttpParser.parseNext(HttpParser.java:644)\n\tat org.eclipse.jetty.http.HttpParser.parseAvailable(HttpParser.java:235)\n\tat org.eclipse.jetty.server.AsyncHttpConnection.handle(AsyncHttpConnection.java:82)\n\tat org.eclipse.jetty.io.nio.SelectChannelEndPoint.handle(SelectChannelEndPoint.java:696)\n\tat org.eclipse.jetty.io.nio.SelectChannelEndPoint$1.run(SelectChannelEndPoint.java:53)\n\tat org.eclipse.jetty.util.thread.QueuedThreadPool.runJob(QueuedThreadPool.java:608)\n\tat org.eclipse.jetty.util.thread.QueuedThreadPool$3.run(QueuedThreadPool.java:543)\n\tat java.lang.Thread.run(Thread.java:745)\n"}

    public List<MessageData> buildMessages(String result) {
        List<MessageData> messageDataList = new ArrayList<MessageData>();
        if (!Utils.isEmpty(result)) {
            try {

                //[{"key":null,"value":"MQ==","partition":0,"offset":10},{"key":null,"value":"Mg==","partition":0,"offset":11},{"key":null,"value":"Mw==","partition":0,"offset":12},{"key":null,"value":"NA==","partition":0,"offset":13},{"key":null,"value":"NQ==","partition":0,"offset":14}]
                JSONArray jsonArray = JSONArray.parseArray(result);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String key = jsonObject.getString("key");
                    String value = jsonObject.getString("value");
                    value = this.decodeBase64(value);
                    MessageData messageData = new MessageData(resourceName, value);
                    messageDataList.add(messageData);
                }
            } catch (Exception e) {
                logger.error("result:" + result);
                e.printStackTrace();
            }

        }
        return messageDataList;
    }

    public void stopP() {
        this.isRun = false;
    }

    public void continueP() {
        this.isRun = true;
        this.run();
    }

    public void close() {
        try {
            this.httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while (isRun) {
            try {
                String result = readMessage();
                List<MessageData> messageDataList = buildMessages(result);
                if (messageDataList != null && messageDataList.size() > 0) {
                    for (MessageData messageData : messageDataList) {
                        resourceListener.onEvent(messageData);
                    }
                }
            } catch (Exception e) {
                logger.error("read message failed.");
                e.printStackTrace();
            }

            Utils.sleep(pollIntervalSec * 1000);
        }

    }
}
