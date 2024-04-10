package com.hoon.api.utils.cookie;



import com.hoon.api.utils.advice.dto.Error;
import com.hoon.api.utils.exception.ConditionException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class CookieUtil {

    public void setCookie(HttpServletResponse response, String key, String value) {
        log.info("setCookie: key: {}, value: {}", key, value);
        Cookie cookie = new Cookie(key, value);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie cookie = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals(cookieName)).findAny()
                .orElseThrow(() ->
                                     new ConditionException(Error.builder().message("쿠키 " + cookieName + " 이 없습니다.").key(cookieName).value("not found").build())
                );
        return cookie.getValue();
    }

}
