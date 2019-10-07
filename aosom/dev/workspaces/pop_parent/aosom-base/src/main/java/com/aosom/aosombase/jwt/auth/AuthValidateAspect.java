package com.aosom.aosombase.jwt.auth;


import com.aosom.aosombase.entity.BaseResponse;
import com.aosom.aosombase.jwt.JwtUtil;
import com.aosom.aosombase.tag.TagParse;
import com.google.common.base.Joiner;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

/**
 * 标记验证用户登入
 */
@Aspect
@Component
public class AuthValidateAspect {


    @Pointcut("@annotation(com.aosom.aosombase.jwt.auth.Auth)")
    public void point() {
    }

    @Before("point()")
    public void validateAuth() throws Exception {
        String  token =  (String) ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getAttribute("token",SCOPE_REQUEST);
        JwtUtil.verifyToken(token);
    }

}
