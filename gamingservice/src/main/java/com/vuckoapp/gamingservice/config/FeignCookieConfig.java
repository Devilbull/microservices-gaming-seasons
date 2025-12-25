package com.vuckoapp.gamingservice.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
@RequiredArgsConstructor
public class FeignCookieConfig {

    @Bean
    public RequestInterceptor cookieForwardInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {

                ServletRequestAttributes attrs =
                        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                if (attrs == null) return;

                HttpServletRequest request = attrs.getRequest();
                if (request.getCookies() == null) return;

                for (Cookie cookie : request.getCookies()) {
                    if ("jwt".equals(cookie.getName())) {
                        template.header(
                                "Cookie",
                                "jwt=" + cookie.getValue()
                        );
                        System.out.println("Forwarding cookie: jwt=" + cookie.getValue());

                    }
                }
            }
        };
    }
}
