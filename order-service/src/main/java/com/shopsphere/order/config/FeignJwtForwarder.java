package com.shopsphere.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class FeignJwtForwarder {
    @Bean
    public RequestInterceptor authForwarder() {
        return (RequestTemplate template) -> {
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            if (attrs == null) return;
            HttpServletRequest req = (HttpServletRequest) attrs.resolveReference(RequestAttributes.REFERENCE_REQUEST);
            if (req == null) return;
            String auth = req.getHeader("Authorization");
            if (auth != null) template.header("Authorization", auth);
        };
    }
}
