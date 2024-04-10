package com.hoon.api.utils.filter.parammodifi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.util.*;

public class ParamModifyWrapper extends HttpServletRequestWrapper {

    private final Map<String, String[]> modifiedParameters;

    public ParamModifyWrapper(HttpServletRequest request) {
        super(request);
        this.modifiedParameters = new HashMap<>(request.getParameterMap());
    }

    @Override
    public String getParameter(String name) {
        String[] values = modifiedParameters.get(name);
        return (values != null && values.length > 0) ? values[0] : null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return Collections.unmodifiableMap(modifiedParameters);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(modifiedParameters.keySet());
    }



    @Override
    public String[] getParameterValues(String name) {
        return modifiedParameters.get(name);
    }

    public void add(String name, String value) {
        modifiedParameters.put(name, new String[]{value});
    }

    public void decodeAdd(String key, String value) {
        if(value != null) modifiedParameters.put(key, new String[]{new String(Base64.getDecoder().decode(value))});
    }
}
