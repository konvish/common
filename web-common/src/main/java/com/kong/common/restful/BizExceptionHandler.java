package com.kong.common.restful;

import com.kong.common.exception.BizException;
import com.kong.common.protocol.ResponseT;
import com.kong.common.protocol.ResponseTs;
import com.kong.common.utils.RtnCodeEnum;
import cz.jirutka.spring.exhandler.handlers.RestExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by kong on 2016/2/2.
 */
public class BizExceptionHandler implements RestExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(BizExceptionHandler.class);
    public static final String errorMsgPattern = "invoke {} error: {} ";

    public BizExceptionHandler() {
    }

    public ResponseEntity handleException(Exception exception, HttpServletRequest request) {
        boolean isDebug = false;
        if(request.getParameter("debug") != null) {
            isDebug = true;
        }

        ResponseT responseT = null;
        if(exception instanceof BizException) {
            responseT = ResponseTs.newResponseException((BizException)exception, isDebug);
            logger.error("invoke {} error: {} ", request.getRequestURL(), ((BizException)exception).getMsg());
        } else {
            BizException bizException = null;
            if(isDebug) {
                bizException = new BizException(RtnCodeEnum.UNKNOW.getValue(), RtnCodeEnum.UNKNOW.getDesc(), exception.getMessage());
            } else {
                bizException = new BizException(RtnCodeEnum.UNKNOW.getValue(), RtnCodeEnum.UNKNOW.getDesc());
            }

            responseT = ResponseTs.newResponseException(bizException, isDebug);
            logger.error("invoke {} error: {} ", request.getRequestURL(), exception.getMessage());
        }

        return new ResponseEntity(responseT, HttpStatus.OK);
    }
}
