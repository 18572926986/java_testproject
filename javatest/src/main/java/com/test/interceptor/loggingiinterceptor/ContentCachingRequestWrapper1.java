package com.test.interceptor.loggingiinterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ContentCachingRequestWrapper1 extends HttpServletRequestWrapper implements CachedBodyRequest {
    private final ByteArrayOutputStream cachedBody;

    public ContentCachingRequestWrapper1(HttpServletRequest request) throws IOException {
        super(request);
        cachedBody = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = request.getInputStream().read(buffer)) != -1) {
            cachedBody.write(buffer, 0, bytesRead);
        }
    }

    @Override
    public byte[] getContentAsByteArray() {
        return cachedBody.toByteArray();
    }
}
