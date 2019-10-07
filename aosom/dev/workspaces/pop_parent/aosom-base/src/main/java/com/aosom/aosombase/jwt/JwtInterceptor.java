package com.aosom.aosombase.jwt;

import com.aosom.aosombase.context.GlobalContext;
import org.jose4j.jwt.JwtClaims;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析token    把登入的数据放到全局变量中
 */
public class JwtInterceptor   implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
         String  token=  request.getHeader("token");
         if(!StringUtils.isEmpty(token)){
               //解析
             JwtClaims   chalims= JwtUtil.verifyToken(token);
             Map<String, Object> map = (HashMap)chalims.getClaimValue("userclaim");
             Long uid=  (Long)  map.get("uid");
             GlobalContext.saveKey(GlobalContext.MEMBER_ID,uid+"");
         }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
