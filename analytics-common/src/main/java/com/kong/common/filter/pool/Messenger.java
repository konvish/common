package com.kong.common.filter.pool;

import com.kong.cloudstack.context.CloudContextFactory;
import com.kong.common.dap.model.webfilter.Creator;
import com.kong.common.dap.model.webfilter.Entry;
import com.kong.common.dap.model.webfilter.Har;
import com.kong.common.dap.model.webfilter.Log;
import com.kong.common.dap.model.webfilter.Message;
import com.kong.common.filter.pool.Work;
import com.kong.kafka.DefaultKafkaProducer;
import com.alibaba.fastjson.JSON;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by kong on 2016/1/3.
 */
public class Messenger implements Work{
    public static final Logger logger = LoggerFactory.getLogger(Messenger.class);

    public Messenger() {
    }

    public void execute(Map<String, Object> analyticsData) {
        Entry entry = (Entry)analyticsData.get("data");
        Message msg = this.getMessage(entry, (String)null, (Map)analyticsData.get("usercontext"));
        String data = JSON.toJSONString(msg);

        try {
            DefaultKafkaProducer.getInstance().send(CloudContextFactory.getCloudContext().getProductCode(), CloudContextFactory.getCloudContext().getApplicationName(), "httpReq", "server", data);
        } catch (Exception var6) {
            logger.error("rocketMQ producer send data error" + var6);
        }

    }

    public void terminate() {
        DefaultKafkaProducer.getInstance().stop();
    }

    public Message getMessage(Entry entry, String token, Map userContext) {
        Message message = new Message();
        message.setHar(this.setHar(entry));
        message.setServiceToken(token);
        message.setUserContext(userContext);
        return message;
    }

    private Har setHar(Entry entry) {
        Har har = new Har();
        har.setLog(this.setLog(entry));
        return har;
    }

    private Log setLog(Entry entry) {
        Log log = new Log();
        log.setVersion("1.2");
        log.setCreator(this.setCreator());
        log.getEntries().add(entry);
        return log;
    }

    private Creator setCreator() {
        Creator creator = new Creator();
        creator.setName("Analytics Java Agent");
        creator.setVersion("1.0");
        return creator;
    }
}
