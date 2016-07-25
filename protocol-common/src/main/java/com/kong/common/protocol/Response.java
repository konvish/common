package com.kong.common.protocol;

import com.kong.common.exception.BizException;
import com.kong.common.utils.RtnCodeEnum;

import java.io.Serializable;

/**
 * http服务返回的数据包装类
 * Created by kong on 2016/2/2.
 */
public class Response implements Serializable {
    /** 返回的响应码 为空，说明是正常返回*/
    private String rtnCode;
    /** 错误信息 有业务异常的时候，来源于BizException；否则网关出错（系统异常），使用通用异常 */
    private String msg;
    /** 错误堆栈信息，便于排查问题   正常是调试模式下该字段才返回信息 */
    private String developMsg;
    /** 错误说明url 有业务异常的时候，来源于BizException；否则网关出错（系统异常），使用通用异常 */
    private String uri;
    private long ts = System.currentTimeMillis();
    /** 返回的业务 有业务异常的时候，来源于BizException；否则网关出错（系统异常），使用通用异常 */
    private Object bizData;

    private Response(ResponseBuilder builder) {
        this.rtnCode = builder.rtnCode;
        this.msg = builder.msg;
        this.developMsg = builder.developMsg;
        this.uri = builder.uri;
        this.bizData = builder.bizData;
    }

    public Response(){}

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Object getBizData() {
        return bizData;
    }

    public void setBizData(Object bizData) {
        this.bizData = bizData;
    }

    public long getTs() {
        return ts;
    }

    public static class ResponseBuilder {
        private String rtnCode;
        private String msg;
        private String developMsg;
        private String uri;
        private Object bizData;

        public ResponseBuilder(BizException bizException) {
            this.rtnCode = bizException.getErrorCode();
            this.msg = bizException.getMsg();
            this.developMsg = bizException.getDevelopMsg();
            this.uri = bizException.getUri();
        }

        public ResponseBuilder(RtnCodeEnum rtnCodeEnum) {
            this.rtnCode = rtnCodeEnum.getValue();
            this.msg = rtnCodeEnum.getDesc();
        }

        public ResponseBuilder rtnCode(String rtnCode) {
            this.rtnCode = rtnCode;
            return this;
        }

        public ResponseBuilder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public ResponseBuilder developMsg(String developMsg) {
            this.developMsg = developMsg;
            return this;
        }

        public ResponseBuilder uri(String uri) {
            this.uri = uri;
            return this;
        }

        public ResponseBuilder bizData(Object bizData) {
            this.bizData = bizData;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }
}
