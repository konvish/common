package com.kong.common.filter.mapper;

import com.kong.common.dap.model.webfilter.Content;
import com.kong.common.dap.model.webfilter.Entry;
import com.kong.common.dap.model.webfilter.Message;
import com.kong.common.dap.model.webfilter.NameValuePair;
import com.kong.common.dap.model.webfilter.Request;
import com.kong.common.dap.model.webfilter.Response;
import com.kong.common.dap.model.webfilter.Timings;
import com.kong.common.filter.wrapper.RequestInterceptorWrapper;
import com.kong.common.filter.wrapper.ResponseInterceptorWrapper;
import com.google.common.io.BaseEncoding;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 分析数据的映射
 * Created by kong on 2016/1/3.
 */
public class AnalyticsDataMapper {
    Logger logger = LoggerFactory.getLogger(AnalyticsDataMapper.class);
    private static final String DEFAULT_MIME_TYPE = "application/octet-stream";
    private RequestInterceptorWrapper request;
    private ResponseInterceptorWrapper response;
    Message message;

    /**
     * 构造方法，request方式与response方式
     * @param request request
     * @param response response
     */
    public AnalyticsDataMapper(RequestInterceptorWrapper request, ResponseInterceptorWrapper response) {
        this.request = request;
        this.response = response;
    }

    /**
     * 获取接收的分析数据
     * @param requestReceivedTime 接收时间
     * @param sendTime 发送时间
     * @param waitTime 等待时间
     * @return Entry
     */
    public Entry getAnalyticsData(Date requestReceivedTime, long sendTime, long waitTime) {
        Entry entry = new Entry();
        entry.setClientIPAddress(this.request.getRemoteAddr());
        entry.setServerIPAddress(this.request.getLocalAddr());
        entry.setStartedDateTime(this.dateAsIso(requestReceivedTime));
        entry.setRequest(this.mapRequest());
        entry.setResponse(this.mapResponse());
        entry.setTimings(this.mapTimings(requestReceivedTime, sendTime, waitTime));
        return entry;
    }

    /**
     * 设置请求头
     *
     * @param requestHar 请求头
     */
    private void setRequestHeaders(Request requestHar) {
        Enumeration headers = this.request.getHeaderNames();
        List<NameValuePair> headerList = requestHar.getHeaders();
        int size = 2;

        while(headers.hasMoreElements()) {
            String name = (String)headers.nextElement();
            size += name.getBytes().length;

            for(Enumeration values = this.request.getHeaders(name); values.hasMoreElements(); size += 6) {
                NameValuePair pair = new NameValuePair();
                pair.setName(name);
                String headerValue = (String)values.nextElement();
                size += headerValue.getBytes().length;
                pair.setValue(headerValue);
                headerList.add(pair);
            }
        }

        requestHar.setHeadersSize(size);
    }

    /**
     * 设置返回头
     * @param responseHar 返回头
     */
    private void setResponseHeaders(Response responseHar) {
        Collection headers = this.response.getHeaderNames();
        if(headers != null) {
            List<NameValuePair> headerList = responseHar.getHeaders();
            int size = 2;
            Iterator i$ = headers.iterator();

            while(i$.hasNext()) {
                String name = (String)i$.next();
                Collection values = this.response.getHeaders(name);

                for(Iterator i$1 = values.iterator(); i$1.hasNext(); size += 6) {
                    String value = (String)i$1.next();
                    NameValuePair pair = new NameValuePair();
                    size += name.getBytes().length;
                    pair.setName(name);
                    size += value.getBytes().length;
                    pair.setValue(value);
                    headerList.add(pair);
                }
            }

            responseHar.setHeadersSize(size);
        }
    }

    private Request mapRequest() {
        Request requestHar = new Request();
        requestHar.setBodySize(this.request.getContentLength());
        requestHar.setContent(this.mapRequestContent());
        requestHar.setMethod(this.request.getMethod());
        requestHar.setUrl(this.request.getRequestURL().toString());
        requestHar.setHttpVersion(this.request.getProtocol());
        this.setRequestHeaders(requestHar);
        this.setQueryString(requestHar);
        return requestHar;
    }

    private void setQueryString(Request requestHar) {
        Enumeration params = this.request.getParameterNames();
        List paramList = requestHar.getQueryString();

        while(params.hasMoreElements()) {
            String name = (String)params.nextElement();
            String[] values = this.request.getParameterValues(name);
            String[] arr$ = values;
            int len$ = values.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String value = arr$[i$];
                NameValuePair pair = new NameValuePair();
                pair.setName(name);
                pair.setValue(value);
                paramList.add(pair);
            }
        }

    }

    private Content mapRequestContent() {
        Content content = new Content();
        content.setEncoding(this.request.getCharacterEncoding());
        String mimeType = this.request.getContentType();
        content.setMimeType(DEFAULT_MIME_TYPE);
        if(mimeType != null && mimeType.length() > 0) {
            content.setMimeType(this.request.getContentType());
        }

        content.setSize(this.request.getPayload().length());
        if(this.request.getPayload().length() > 0) {
            try {
                content.setText(BaseEncoding.base64().encode(this.request.getPayload().getBytes(this.request.getCharacterEncoding() == null?"UTF-8":this.request.getCharacterEncoding())));
            } catch (UnsupportedEncodingException var4) {
                throw new RuntimeException("Failed to encode request body");
            }
        }

        return content;
    }

    private Response mapResponse() {
        Response responseHar = new Response();
        responseHar.setBodySize(this.response.getClone().length);
        responseHar.setContent(this.mapResponseContent());
        responseHar.setHttpVersion(this.request.getProtocol());
        responseHar.setStatus(Integer.toString(this.response.getStatus()));
        responseHar.setStatusText(responseHar.getStatus());
        this.setResponseHeaders(responseHar);
        return responseHar;
    }

    private Content mapResponseContent() {
        Content content = new Content();
        content.setEncoding(this.response.getCharacterEncoding());
        String mimeType = this.response.getContentType();
        content.setMimeType(DEFAULT_MIME_TYPE);
        if(mimeType != null && mimeType.length() > 0) {
            content.setMimeType(mimeType);
        }

        content.setSize(this.response.getClone().length);
        return content;
    }

    private Timings mapTimings(Date requestReceivedTime, long sendTime, long waitTime) {
        Timings timings = new Timings();
        timings.setReceive(0L);
        timings.setSend(sendTime);
        timings.setWait(waitTime);
        return timings;
    }

    private String dateAsIso(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.s\'Z\'");
        df.setTimeZone(tz);
        return df.format(new Date());
    }
}
