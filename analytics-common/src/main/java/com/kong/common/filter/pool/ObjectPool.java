package com.kong.common.filter.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 抽象对象工作池
 * Created by kong on 2016/1/3.
 */
public abstract class ObjectPool<E extends Work> {
    private static final Logger logger = LoggerFactory.getLogger(ObjectPool.class);
    //线程安全链式队列构成的工作池
    private ConcurrentLinkedQueue<E> pool;
    //任务调度服务
    private ScheduledExecutorService executor;

    /**
     * 构造对象工作池
     * @param minThread 工作池的大小
     */
    public ObjectPool(int minThread) {
        this.createObject(minThread);
    }

    /**
     * 构造对象工作池
     * @param minPoolSize 最小线程数
     * @param maxPoolSize 最大线程数
     * @param interval 间隔
     */
    public ObjectPool(final int minPoolSize, final int maxPoolSize, int interval) {
        this.createObject(minPoolSize);
        //单线程调度器
        this.executor = Executors.newSingleThreadScheduledExecutor();
        //设置多长时间查看调度情况
        //一旦工作池的大小小于最小线程数，创建相应的线程数补到最小线程数
        //大于最大线程数的时候，移除超出的线程数
        this.executor.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                int size = ObjectPool.this.pool.size();
                ObjectPool.logger.debug("Pool size:" + size);
                if(size < minPoolSize) {
                    ObjectPool.this.createObject(minPoolSize - size);
                } else if(size > maxPoolSize) {
                    ObjectPool.this.removeObject(size - maxPoolSize);
                }

            }
        }, (long)interval, (long)interval, TimeUnit.SECONDS);
    }

    /**
     * 从池中取出对象
     * @return E
     */
    public E borrowObject() {
        E poolObject = null;
        if((poolObject = this.pool.poll()) == null) {
            poolObject = this.createPoolObject();
        }

        logger.debug("Object borrowed from pool:" + poolObject.toString());
        return poolObject;
    }

    /**
     * 填充对象工作池的对象
     * @param tobeCreated 线程池大小
     */
    public void createObject(int tobeCreated) {
        if(this.pool == null) {
            this.pool = new ConcurrentLinkedQueue();
        }

        for(int i = 0; i < tobeCreated; ++i) {
            Work poolObject = this.createPoolObject();
            logger.debug("New object added to pool:" + poolObject.toString());
            this.pool.add(this.createPoolObject());
        }

    }

    /**
     * 创建目标对象
     * 抽象方法，目标对象需要重写此方法
     * @return E
     */
    public abstract E createPoolObject();

    /**
     * 应用场景
     * 超过线程池的最大线程数时候移除线程
     * @param toBeRemoved 移除线程数
     */
    public void removeObject(int toBeRemoved) {
        for(int i = 0; i < toBeRemoved; ++i) {
            Work poolObject = (Work)this.pool.poll();
            poolObject.terminate();
            logger.debug("Object removed from pool:" + poolObject.toString());
        }

    }

    /**
     * 执行完回放队列尾部
     * @param poolObject E
     */
    public void returnObject(E poolObject) {
        if(poolObject != null) {
            this.pool.offer(poolObject);
            logger.debug("Object returned to pool:" + poolObject.toString());
        }
    }

    /**
     * 关闭池
     */
    public void terminate() {
        if(this.executor != null) {
            this.executor.shutdown();
        }

        if(this.pool != null) {
            this.removeObject(this.pool.size());
        }

    }
}
