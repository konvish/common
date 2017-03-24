package com.kong.common.filter.pool;

import java.util.Map;

/**
 * 发送分析运行任务
 * 实现runnable接口
 * 开启线程执行分析任务
 * Created by Administrator on 2016/1/3.
 */
public class SendAnalyticsTask implements Runnable{
    //对象池
    private ObjectPool<Work> pool;
    //分析数据内容
    private Map<String, Object> analyticsData;

    public SendAnalyticsTask(ObjectPool<Work> pool, Map<String, Object> analyticsData) {
        this.pool = pool;
        this.analyticsData = analyticsData;
    }

    /**
     * 执行流程
     * 从对象池取得对象
     * ->执行任务
     * ->回放对象
     */
    @Override
    public void run() {
        Work work = this.pool.borrowObject();
        work.execute(this.analyticsData);
        this.pool.returnObject(work);
    }
}
