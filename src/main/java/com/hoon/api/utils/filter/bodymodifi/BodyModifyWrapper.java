package com.hoon.api.utils.filter.bodymodifi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class BodyModifyWrapper extends HttpServletRequestWrapper {

    private JSONObject jsonObject;
    private byte[] newData;
    public BodyModifyWrapper(HttpServletRequest request) throws IOException {
        super(request);
        InputStream is = super.getInputStream();
        this.jsonObject =  readJSONStringFromRequestBody(IOUtils.toByteArray(is));
    }

    @Override
    public String toString() {
        return jsonObject.toString();
    }

    public Object get(String key){
        return jsonObject.get(key);
    }

    public void remove(String key){
        jsonObject.remove(key);
    }

    public void add(String key, Object value){ jsonObject.put(key, value); }

    public void decodeAdd(String key, String value) {
        if(value != null) jsonObject.put(key, new String(Base64.getDecoder().decode(value)));
    }

    public static JSONObject readJSONStringFromRequestBody(byte[] rawData) throws JsonProcessingException {
        StringBuffer json = new StringBuffer();
        ObjectMapper mapper = new ObjectMapper();
        String line = null;
        InputStream is = null;
        JSONObject jObj = new JSONObject();
        if(rawData.length > 0) {
            try {
                is = new ByteArrayInputStream(rawData);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            Map<String, String> map = mapper.readValue(json.toString(), Map.class);
            jObj = new JSONObject(map);
        }
        return jObj;
    }

    @Override
    public ServletInputStream getInputStream() {
        newData = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
        final ByteArrayInputStream bis = new ByteArrayInputStream(newData);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return bis.available() == 0;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int read() {
                return bis.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletRequest getRequest() {
        return super.getRequest();
    }

}
