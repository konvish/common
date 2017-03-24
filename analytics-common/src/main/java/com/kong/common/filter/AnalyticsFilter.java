package com.kong.common.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kong.common.context.UserContextHolder;
import com.kong.common.dap.model.webfilter.Entry;
import com.kong.common.filter.common.Util;
import com.kong.common.filter.mapper.AnalyticsDataMapper;
import com.kong.common.filter.pool.Messenger;
import com.kong.common.filter.pool.ObjectPool;
import com.kong.common.filter.pool.SendAnalyticsTask;
import com.kong.common.filter.pool.Work;
import com.kong.common.filter.wrapper.RequestInterceptorWrapper;
import com.kong.common.filter.wrapper.ResponseInterceptorWrapper;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * http 请求 数据记录拦截器   支持 基于zk的云化配置和 非基于zk的云化配置两类
 *  每个产品线都有自己的本地元数据描述文件；
 *  每个产品线的日志数据都天然隔离；
 *
 *  此filter需要放在身份验证的filter之后
 * Created by Administrator on 2016/1/3.
 */
public class AnalyticsFilter implements Filter{
    static final Logger logger = LoggerFactory.getLogger(AnalyticsFilter.class);
    //异步线程Service
    private ExecutorService analyticsServiceExecutor;

    private ObjectPool<Work> pool;
    private boolean isAnlayticsEnabled = true;

    public AnalyticsFilter() {
    }

    public void destroy() {
        this.analyticsServiceExecutor.shutdown();
        this.pool.terminate();
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if(this.isAnlayticsEnabled) {
            long sendStartTime = System.currentTimeMillis();
            Date requestReceivedTime = new Date();
            RequestInterceptorWrapper request = new RequestInterceptorWrapper((HttpServletRequest)req);
            ResponseInterceptorWrapper response = new ResponseInterceptorWrapper((HttpServletResponse)res);
            long waitStartTime = System.currentTimeMillis();
            chain.doFilter(request, response);
            long waitEndTime = System.currentTimeMillis();
            this.callAsyncAnalytics(requestReceivedTime, request, response, waitStartTime - sendStartTime, waitEndTime - waitStartTime);
        } else {
            chain.doFilter(req, res);
        }

    }

    private void callAsyncAnalytics(Date requestReceivedTime, RequestInterceptorWrapper request, ResponseInterceptorWrapper response, long sendTime, long waitTime) {
        try {
            long x = System.currentTimeMillis();
            HashMap messageProperties = new HashMap();
            Entry analyticsData = (new AnalyticsDataMapper(request, response)).getAnalyticsData(requestReceivedTime, sendTime, waitTime);
            long recvEndTime = System.currentTimeMillis();
            analyticsData.getTimings().setReceive(recvEndTime - x);
            analyticsData.setTime(recvEndTime - x + sendTime + waitTime);
            messageProperties.put("data", analyticsData);
            if(UserContextHolder.getUserContext() != null) {
                messageProperties.put("usercontext", UserContextHolder.getUserContext().getContexts());
            }

            this.analyticsServiceExecutor.execute(new SendAnalyticsTask(this.pool, messageProperties));
        } catch (Throwable var14) {
            logger.error("Failed to send analytics data", var14);
        }

    }

    public void init(FilterConfig config) throws ServletException {
        if(this.isAnlayticsEnabled) {
            int poolSize = this.getEnvVarOrDefault("analytics.worker.size", Runtime.getRuntime().availableProcessors() * 2);
            final int socketPoolMin = this.getEnvVarOrDefault("analytics.socket.min", 10);
            final int socketPoolMax = this.getEnvVarOrDefault("analytics.socket.max", 20);
            final int poolUpdateInterval = this.getEnvVarOrDefault("analytics.socket.interval", 5);
            this.pool = new ObjectPool(socketPoolMin, socketPoolMax, poolUpdateInterval) {
                public Work createPoolObject() {
                    return new Messenger();
                }
            };
            this.analyticsServiceExecutor = Executors.newFixedThreadPool(poolSize);
        }

    }

    private int getEnvVarOrDefault(String name, int defaultVal) {
        String val = System.getProperty(name);
        return Util.notBlank(val)?Integer.parseInt(val):defaultVal;
    }

    private boolean isAnalyticsFlagEnabled() {
        return Boolean.parseBoolean(System.getProperty("analytics.enabled.flag"));
    }
}
