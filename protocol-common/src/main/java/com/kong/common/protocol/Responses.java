package com.kong.common.protocol;

import com.kong.common.exception.BizException;
import com.kong.common.utils.RtnCodeEnum;

/**
 * Response 工具类
 * Created by kong on 2016/2/2.
 */
public class Responses {
    /**
     * 返回未知异常
     * @return
     */
    public static Response newUnknow(){
        return new Response.ResponseBuilder(RtnCodeEnum.UNKNOW).build();
    }

    /**
     *
     * 成功返回
     *
     * @return
     */
    public static Response newOK(){
        return new Response.ResponseBuilder(RtnCodeEnum.SUCCESS).build();
    }

    /**
     *
     * 业务异常返回
     *
     * @return
     */
    public static Response newResponse(BizException ex){
        return new Response.ResponseBuilder(ex).build();
    }

    /**
     * 正常业务对象的返回
     * @param bizData
     * @return
     */
    public static Response newResponse(Object bizData){
        return new Response.ResponseBuilder(RtnCodeEnum.SUCCESS).bizData(bizData).build();
    }

    /**
     * 返回网络异常
     * @return
     */
    public static Response newNetError(){
        return new Response.ResponseBuilder(RtnCodeEnum.NET_ERROR).build();
    }

    /**
     * 返回请求参数异常
     * @return
     */
    public static Response newParamError(){
        return new Response.ResponseBuilder(RtnCodeEnum.PARAMETER_ERROR).build();
    }

    /**
     * 返回调用次数超限异常
     * @return
     */
    public static Response newOverLimit(){
        return new Response.ResponseBuilder(RtnCodeEnum.APP_OVER_INVOCATION_LIMIT).build();
    }
}
