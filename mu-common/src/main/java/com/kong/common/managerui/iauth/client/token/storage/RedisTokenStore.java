package com.kong.common.managerui.iauth.client.token.storage;

import com.kong.cloudstack.cache.IRedisRepository;
import com.kong.cloudstack.cache.RedisRepositoryFactory;
import com.kong.cloudstack.dynconfig.DynConfigClient;
import com.kong.cloudstack.dynconfig.DynConfigClientFactory;
import com.kong.cloudstack.dynconfig.IChangeListener;
import com.kong.cloudstack.dynconfig.domain.Configuration;
import com.kong.common.managerui.iauth.core.token.Token;
import com.kong.common.managerui.iauth.core.token.storage.TokenStore;
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
public class RedisTokenStore implements TokenStore {
    private static Logger logger = LoggerFactory.getLogger(RedisTokenStore.class);
    public int TOKEN_EXPIRE_TIME = 600;
    public static final String PREFIX = "token:";
    private IRedisRepository tokenStorage;

    public RedisTokenStore() {
    }

    public IRedisRepository getTokenStorage() {
        return this.tokenStorage;
    }

    @PostConstruct
    public void init() throws Exception {
        DynConfigClient dynConfigClient = DynConfigClientFactory.getClient();

        try {
            this.TOKEN_EXPIRE_TIME = Integer.parseInt(dynConfigClient.getConfig("ucm", "common", "tokenExpireTime"));
        } catch (Exception var3) {
            logger.info("tokenExpireTime没有进行配置，采用默认值: " + this.TOKEN_EXPIRE_TIME);
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
                        RedisTokenStore.this.TOKEN_EXPIRE_TIME = Integer.parseInt(configuration.getConfig());
                    }
                });
            }
        });
        this.tokenStorage = RedisRepositoryFactory.getRepository("ucm", "common", "tokenStorage");
    }

    public Token readToken(String key) {
        return (Token)this.tokenStorage.get("token:" + key);
    }

    public void postpone(String key) {
        this.tokenStorage.expire("token:" + key, (long)this.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    public void store(String key, Token token) {
        this.tokenStorage.set("token:" + key, token, (long)this.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    public void removeToken(String key) {
        this.tokenStorage.del("token:" + key);
    }
}
