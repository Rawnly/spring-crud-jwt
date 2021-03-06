package com.federicovitale.spring_jwt_boilerplate.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class HeaderUtil {
    public static String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token = token.replace("Bearer ", "");
        return token;
    }
}
