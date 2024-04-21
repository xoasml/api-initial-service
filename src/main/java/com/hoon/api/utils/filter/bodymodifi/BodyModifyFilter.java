package com.hoon.api.utils.filter.bodymodifi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;


import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.*;
import java.util.HashMap;
import java.util.regex.Pattern;

import static com.hoon.api.utils.filter.Util.cookieToMap;


@Slf4j
@Order(9999)
@WebFilter(urlPatterns = {"/*"})
public class BodyModifyFilter implements Filter {

    @Value(value = "${spring.profiles.active}")
    private String profile;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HashMap<String, Object> cookieMap = cookieToMap(req.getCookies());
        String contentType = request.getContentType();

        try {
            if (contentType != null && Pattern.matches("multipart/form-data.*", contentType)) req.getParts();
            BodyModifyWrapper bodyModifyWrapper = new BodyModifyWrapper(req);

            // local 환경에서는 user : "tester", region : "GIMPO" 로 설정
            if (!"prod".equals(profile)) { // 복호화가 필요 없는 경우의 add() 메소드 사용
                bodyModifyWrapper.add("user", cookieMap.get("user") == null ? "tester" : cookieMap.get("user"));
                bodyModifyWrapper.add("region", cookieMap.get("region") == null ? "GIMPO" : cookieMap.get("region"));
            }

            if ("prod".equals(profile)) { // 복호화가 필요한 경우의 decodeAdd() 메소드 사용
                bodyModifyWrapper.decodeAdd("user", (String) cookieMap.get("user"));
                bodyModifyWrapper.add("region", cookieMap.get("region"));
            }

            chain.doFilter(bodyModifyWrapper, response);
        } catch (Exception e) {
            log.error("BodyModifyFilter Cookie 작업 실행중 Exception");
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("BodyModifyFilter 시작 > Request 요청 시 RequestBody 에 핸들링 하는 필터");
    }

    @Override
    public void destroy() {
        log.info("BodyModifyFilter 종료 > Request 요청 시 RequestBody 에 핸들링 하는 필터");
    }


}
