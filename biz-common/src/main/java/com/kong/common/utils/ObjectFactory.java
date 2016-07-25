package com.kong.common.utils;

/**
 * 对象工厂
 * Created by kong on 2016/1/3.
 */
public class ObjectFactory {
    private static class ObjectBuild{
        private static Object instance = new Object();
    }

    /**
     * 获取一个空对象的单例
     * @return
     */
    public static Object getSingle(){
        return ObjectBuild.instance;
    }
}
