package com.kong.common.managerui.iauth.client.token.storage;

import com.kong.cloudstack.cache.IRedisRepository;
import com.kong.cloudstack.cache.RedisRepositoryFactory;
import com.kong.cloudstack.dynconfig.DynConfigClient;
import com.kong.cloudstack.dynconfig.DynConfigClientFactory;
import com.kong.cloudstack.dynconfig.IChangeListener;
import com.kong.cloudstack.dynconfig.domain.Configuration;
import com.kong.common.managerui.domain.User;
import com.kong.common.managerui.iauth.client.token.storage.RedisTokenStore;
import com.kong.common.managerui.iauth.client.token.storage.UserStore;
import com.alibaba.fastjson.JSON;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


/**
 * Created by Administrator on 2016/1/3.
 */
@Repository
public class RedisUserStore implements UserStore {
    private static Logger logger = LoggerFactory.getLogger(RedisTokenStore.class);
    public int USER_EXPIRE_TIME = 600;
    public static final String PREFIX = "user:";
    private IRedisRepository userStorage;

    public RedisUserStore() {
    }

    public IRedisRepository getUserStorage() {
        return this.userStorage;
    }

    @PostConstruct
    public void init() throws Exception {
        DynConfigClient dynConfigClient = DynConfigClientFactory.getClient();

        try {
            this.USER_EXPIRE_TIME = Integer.parseInt(dynConfigClient.getConfig("ucm", "common", "userExpireTime"));
        } catch (Exception var3) {
            logger.info("userExpireTime没有进行配置，采用默认值: " + this.USER_EXPIRE_TIME);
        }

        dynConfigClient.registerListeners("ucm", "common", "tokenExpireTime", new IChangeListener() {
            public Executor getExecutor() {
                return Executors.newSingleThreadExecutor();
            }

            public void receiveConfigInfo(final Configuration configuration) {
                System.out.println("=================" + configuration);
                this.getExecutor().execute(new Runnable() {
                    public void run() {
                        System.out.println("========ASYN=========" + configuration);
                        RedisUserStore.this.USER_EXPIRE_TIME = Integer.parseInt(configuration.getConfig());
                    }
                });
            }
        });
        this.userStorage = RedisRepositoryFactory.getRepository("ucm", "common", "tokenStorage");
    }

    public User storeUser(Object key, User user) {
        String userJson = JSON.toJSONString(user);
        this.userStorage.set("user:" + key, userJson, (long)this.USER_EXPIRE_TIME, TimeUnit.SECONDS);
        return null;
    }

    public User readUser(Object key) {
        return (User)JSON.parseObject((String)this.userStorage.get("user:" + key), User.class);
    }

    public void postpone(Object key) {
        this.userStorage.expire("user:" + key, (long)this.USER_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    public void removeUser(String key) {
        this.userStorage.del("user:" + key);
    }
}
