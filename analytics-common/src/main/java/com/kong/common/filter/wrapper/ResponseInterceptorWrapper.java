package com.kong.common.filter.wrapper;

import com.kong.common.filter.wrapper.OutputStreamCloner;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/3.
 */
public class ResponseInterceptorWrapper extends HttpServletResponseWrapper {
    private ServletOutputStream contentStream;
    private PrintWriter writer;
    private OutputStreamCloner cloner;
    private Map<String, String> headerMap = new HashMap();

    public ResponseInterceptorWrapper(HttpServletResponse response) {
        super(response);
    }

    public void flushBuffer() throws IOException {
        if(this.writer != null) {
            this.writer.flush();
        } else if(this.contentStream != null) {
            this.cloner.flush();
        }

    }

    public byte[] getClone() {
        return this.cloner != null?this.cloner.getClone():new byte[0];
    }

    public ServletOutputStream getOutputStream() throws IOException {
        if(this.writer != null) {
            throw new IllegalStateException("getOutputStream called after getWriter");
        } else {
            if(this.contentStream == null) {
                this.contentStream = this.getResponse().getOutputStream();
                this.cloner = new OutputStreamCloner(this.contentStream);
            }

            return this.cloner;
        }
    }

    public PrintWriter getWriter() throws IOException {
        if(this.contentStream != null) {
            throw new IllegalStateException("getWriter called after getOutputStream ");
        } else {
            if(this.writer == null) {
                this.cloner = new OutputStreamCloner(this.getResponse().getOutputStream());
                this.writer = new PrintWriter(new OutputStreamWriter(this.cloner, this.getResponse().getCharacterEncoding()), true);
            }

            return this.writer;
        }
    }

    public Collection<String> getHeaderNames() {
        Collection names = super.getHeaderNames();
        Iterator i$ = this.headerMap.keySet().iterator();

        while(i$.hasNext()) {
            String name = (String)i$.next();
            names.add(name);
        }

        return names;
    }

    public Collection<String> getHeaders(String name) {
        Collection values = super.getHeaders(name);
        if(this.headerMap.containsKey(name)) {
            values.add(this.headerMap.get(name));
        }

        return values;
    }

    public int getSize() {
        return this.cloner.getClone().length;
    }
}
