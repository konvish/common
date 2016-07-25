package com.kong.common.filter.wrapper;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by kong on 2016/1/3.
 */
public class OutputStreamCloner extends ServletOutputStream {
    private OutputStream outputStream;
    private ByteArrayOutputStream clonedStream = new ByteArrayOutputStream();

    public OutputStreamCloner(OutputStream contentStream) {
        this.outputStream = contentStream;
    }

    public byte[] getClone() {
        return this.clonedStream.toByteArray();
    }

    public void write(int data) throws IOException {
        this.outputStream.write(data);
        this.clonedStream.write(data);
    }

    public boolean isReady() {
        return false;
    }

    public void setWriteListener(WriteListener writeListener) {
    }
}
