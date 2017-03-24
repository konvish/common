package com.kong.common.filter.pool;

import com.kong.cloudstack.context.CloudContextFactory;
import com.kong.common.dap.model.webfilter.Creator;
import com.kong.common.dap.model.webfilter.Entry;
import com.kong.common.dap.model.webfilter.Har;
import com.kong.common.dap.model.webfilter.Log;
import com.kong.common.dap.model.webfilter.Message;
import com.kong.common.filter.AnalyticsConstants;
import com.kong.common.filter.pool.Work;
import com.kong.kafka.DefaultKafkaProducer;
import com.alibaba.fastjson.JSON;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 消息类，实现了Work接口
 *
 * Created by kong on 2016/1/3.
 */
public class Messenger implements Work{
    public static final Logger logger = LoggerFactory.getLogger(Messenger.class);

    public Messenger() {
    }

    /**
     * 执行任务
     * 把消息内容封装成Massage实体类
     * 再转成Json，通过kafka发送到大数据平台
     *
     * @param analyticsData 分析内容
     */
    public void execute(Map<String, Object> analyticsData) {
        Entry entry = (Entry)analyticsData.get(AnalyticsConstants.ANALYTICS_DATA);
        Message msg = this.getMessage(entry, (String)null, (Map)analyticsData.get(AnalyticsConstants.USER_CONTEXT));
        String data = JSON.toJSONString(msg);

        try {
            //发送消息
            DefaultKafkaProducer.getInstance().send(CloudContextFactory.getCloudContext().getProductCode(), CloudContextFactory.getCloudContext().getApplicationName(), DefaultKafkaProducer.HTTP_REQ, DefaultKafkaProducer.HTTP_SERVER_FROM, data);
        } catch (Exception e) {
            logger.error("kafka producer send data error" + e);
        }

    }

    /**
     * 终止消息发送
     * 发送是通过kafka，关闭kafka生产者
     */
    public void terminate() {
        DefaultKafkaProducer.getInstance().stop();
    }

    /**
     * 获取消息内容
     * @param entry 消息Map实体
     * @param token token
     * @param userContext 用户内容
     * @return Message
     */
    public Message getMessage(Entry entry, String token, Map userContext) {
        Message message = new Message();
        message.setHar(this.setHar(entry));
        message.setServiceToken(token);
        message.setUserContext(userContext);
        return message;
    }

    /**
     * 把Entry转成har
     * @param entry Map实体
     * @return Har
     */
    private Har setHar(Entry entry) {
        Har har = new Har();
        har.setLog(this.setLog(entry));
        return har;
    }

    /**
     * 把Entry转成对应的日志
     * @param entry Map实体
     * @return Log
     */
    private Log setLog(Entry entry) {
        Log log = new Log();
        log.setVersion(AnalyticsConstants.HAR_VERSION);
        log.setCreator(this.setCreator());
        log.getEntries().add(entry);
        return log;
    }

    /**
     * 消息发送者信息
     * @return Creator
     */
    private Creator setCreator() {
        Creator creator = new Creator();
        creator.setName(AnalyticsConstants.AGENT_NAME);
        creator.setVersion(AnalyticsConstants.AGENT_VERSION);
        return creator;
    }
}
