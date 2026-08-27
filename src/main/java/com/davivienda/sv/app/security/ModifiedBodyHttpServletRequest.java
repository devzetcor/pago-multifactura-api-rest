package com.davivienda.sv.app.security;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

/**
 * Wrapper para HttpServletRequest que permite modificar el body del request
 */
public class ModifiedBodyHttpServletRequest extends HttpServletRequestWrapper {

    private final byte[] modifiedBody;
    private final String modifiedJsonString;

    public ModifiedBodyHttpServletRequest(HttpServletRequest request, String modifiedJson) {
        super(request);
        this.modifiedJsonString = modifiedJson;
        this.modifiedBody = modifiedJson.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ModifiedServletInputStream(this.modifiedBody);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.modifiedBody);
        return new BufferedReader(new InputStreamReader(byteArrayInputStream, StandardCharsets.UTF_8));
    }

    @Override
    public int getContentLength() {
        return this.modifiedBody.length;
    }

    @Override
    public long getContentLengthLong() {
        return this.modifiedBody.length;
    }

    /**
     * Método personalizado para obtener el JSON modificado
     */
    public String getModifiedJson() {
        return this.modifiedJsonString;
    }

    /**
     * Implementación personalizada de ServletInputStream
     */
    private static class ModifiedServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream inputStream;

        public ModifiedServletInputStream(byte[] data) {
            this.inputStream = new ByteArrayInputStream(data);
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return inputStream.read(b);
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return inputStream.read(b, off, len);
        }

        @Override
        public boolean isFinished() {
            return inputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(jakarta.servlet.ReadListener readListener){
            // No implementado para este caso de uso
            throw new UnsupportedOperationException("ReadListener no soportado");
        }
    }
}