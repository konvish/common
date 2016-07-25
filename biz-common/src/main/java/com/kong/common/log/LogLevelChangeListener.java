package com.kong.common.log;

import ch.qos.logback.classic.Level;
import com.kong.cloudstack.dynconfig.DynConfigClient;
import com.kong.cloudstack.dynconfig.DynConfigClientFactory;
import com.kong.cloudstack.dynconfig.IChangeListener;
import com.kong.cloudstack.dynconfig.domain.Configuration;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * 基于logback 监测业务系统的日志级别变化
 * Created by Administrator on 2016/1/3.
 */
@Component("logLevelChangeListener")
public class LogLevelChangeListener {
    public static final Logger logger = LoggerFactory.getLogger(LogLevelChangeListener.class);
    private String oldLevel = Level.WARN.levelStr;

    @PostConstruct
    public void init() throws Exception {
        DynConfigClient dynConfigClient = DynConfigClientFactory.getClient();

        oldLevel = dynConfigClient.getConfig("common", "logLevel");
        setLogLevel();

        dynConfigClient.registerListeners("common", "logLevel", new IChangeListener() {
            @Override
            public Executor getExecutor() {
                return Executors.newSingleThreadExecutor();
            }

            @Override
            public void receiveConfigInfo(final Configuration configuration) {
                logger.info("old level {}, now level {}", oldLevel, configuration.getConfig());

                getExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        oldLevel = configuration.getConfig();
                        //目前只对我们业务系统的日志级别改变进行监听
                        setLogLevel();
                    }
                });
            }
        });
    }

    private void setLogLevel(){
        if(oldLevel != null) {
            ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger("cn.thinkjoy");
            root.setLevel(Level.toLevel(oldLevel));
            logger.warn(" ===== do setLogLevel =====");
        }
    }
}
