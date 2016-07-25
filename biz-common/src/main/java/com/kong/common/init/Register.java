package com.kong.common.init;

import com.kong.cloudstack.cmc.ClusterManagerClientFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 注册自己到 CMC
 * Created by kong on 2016/1/3.
 */
@Component("register")
public class Register {
    @PostConstruct
    public void init(){
        ClusterManagerClientFactory.createClient().register();
    }
}
