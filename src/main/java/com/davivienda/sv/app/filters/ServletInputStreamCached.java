package com.davivienda.sv.app.filters;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ServletInputStreamCached extends ServletInputStream {

    private final InputStream cachedInputStream;

    public ServletInputStreamCached(byte[] cachedBody) {
        this.cachedInputStream = new ByteArrayInputStream(cachedBody);
    }

    @Override
    public int read() throws IOException {
        return cachedInputStream.read();
    }

    @Override
    public boolean isFinished() {
        try {
            return cachedInputStream.available() == 0;
        } catch (IOException e) {
            return true;
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException("ReadListener no soportado.");
    }
}
