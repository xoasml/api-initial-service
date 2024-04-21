package com.hoon.api.utils.filter.parammodifi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;

import static com.hoon.api.utils.filter.Util.cookieToMap;

@Slf4j
@Order(9998)
@WebFilter(urlPatterns = {"/*"})
public class ParamModifyFilter implements Filter {

    @Value(value = "${spring.profiles.active}")
    private String profile;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HashMap<String, Object> cookieMap = cookieToMap(req.getCookies());

        try {
            ParamModifyWrapper paramModifyWrapper = new ParamModifyWrapper(req);

            // local 환경에서는 user 를 tester, region 을 GIMPO 로 설정
            if (!"prod".equals(profile)) { // 복호화가 필요 없는 경우의 add() 메소드 사용
                paramModifyWrapper.add("user", cookieMap.get("user") == null ? "tester" : (String) cookieMap.get("user"));
                paramModifyWrapper.add("region", cookieMap.get("region") == null ? "GIMPO" : (String) cookieMap.get("region"));
            }

            if ("prod".equals(profile)) { // 복호화가 필요한 경우의 decodeAdd() 메소드 사용
                paramModifyWrapper.decodeAdd("user", (String) cookieMap.get("user"));
                paramModifyWrapper.add("region", (String) cookieMap.get("region"));
            }

            chain.doFilter(paramModifyWrapper, response);

        } catch (Exception e) {
            log.error("ParamModifyFilter Cookie 작업 실행중 Exception");
            chain.doFilter(request, response);
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("ParamModifyFilter 시작 > Request 요청 시 Parameter 에 핸들링 하는 필터");
    }

    @Override
    public void destroy() {
        log.info("ParamModifyFilter 종료 > Request 요청 시 Parameter 에 핸들링 하는 필터");
    }

}
