package com.kong.common.filter.pool;

import com.kong.common.filter.pool.ObjectPool;
import com.kong.common.filter.pool.Work;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/3.
 */
public class SendAnalyticsTask implements Runnable{
    private ObjectPool<Work> pool;
    private Map<String, Object> analyticsData;

    public SendAnalyticsTask(ObjectPool<Work> pool, Map<String, Object> analyticsData) {
        this.pool = pool;
        this.analyticsData = analyticsData;
    }

    @Override
    public void run() {
        Work work = this.pool.borrowObject();
        work.execute(this.analyticsData);
        this.pool.returnObject(work);
    }
}
