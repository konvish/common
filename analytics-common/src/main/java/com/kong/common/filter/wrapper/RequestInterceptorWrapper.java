package com.kong.common.filter.wrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Created by kong on 2016/1/3.
 */
public class RequestInterceptorWrapper extends HttpServletRequestWrapper{
    private final String payload;
    private Map<String, String> headerMap = new HashMap();

    public RequestInterceptorWrapper(HttpServletRequest request) throws IOException {
        super(request);
        StringBuilder content = new StringBuilder();
        BufferedReader reader = null;
        ServletInputStream in = request.getInputStream();
        if(in != null) {
            reader = new BufferedReader(new InputStreamReader(in));
            char[] e = new char[128];
            boolean bytesCount = true;

            int bytesCount1;
            while((bytesCount1 = reader.read(e)) > 0) {
                content.append(e, 0, bytesCount1);
            }
        } else {
            content.append("");
        }

        if(reader != null) {
            try {
                reader.close();
            } catch (IOException var7) {
                ;
            }
        }

        this.payload = content.toString();
    }

    public void addHeader(String name, String value) {
        this.headerMap.put(name, value);
    }

    public String getHeader(String name) {
        String headerValue = super.getHeader(name);
        if(this.headerMap.containsKey(name)) {
            headerValue = (String)this.headerMap.get(name);
        }

        return headerValue;
    }

    public Enumeration<String> getHeaderNames() {
        ArrayList names = Collections.list(super.getHeaderNames());
        Iterator i$ = this.headerMap.keySet().iterator();

        while(i$.hasNext()) {
            String name = (String)i$.next();
            names.add(name);
        }

        return Collections.enumeration(names);
    }

    public Enumeration<String> getHeaders(String name) {
        ArrayList values = Collections.list(super.getHeaders(name));
        if(this.headerMap.containsKey(name)) {
            values.add(this.headerMap.get(name));
        }

        return Collections.enumeration(values);
    }

    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.payload.getBytes());
        ServletInputStream inputStream = new ServletInputStream() {
            public boolean isFinished() {
                return false;
            }

            public boolean isReady() {
                return false;
            }

            public void setReadListener(ReadListener readListener) {
            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return inputStream;
    }

    public String getPayload() {
        return this.payload;
    }
}
