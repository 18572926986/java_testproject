package com.test.interceptor.loggingiinterceptor;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class ContentCachingResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream cachedBody;
    private PrintWriter writer;

    public ContentCachingResponseWrapper(HttpServletResponse response) {
        super(response);
        cachedBody = new ByteArrayOutputStream();
        writer = new PrintWriter(cachedBody);
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    public byte[] getContentAsByteArray() {
        writer.flush();
        return cachedBody.toByteArray();
    }
}
