package com.hoon.api.utils.filter;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Util {

    public static HashMap<String, Object> cookieToMap(Cookie[] cookies) {
        HashMap<String, Object> cookieMap = new HashMap<>();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie.getValue());
            }
        }
        return cookieMap;
    }

    public static String toCookieString(Cookie[] cookies) {
        return cookies == null ? "" : Arrays.stream(cookies).map(v -> v.getName() + "=" + v.getValue()).collect(Collectors.joining("; path=/;"));
    }

}
