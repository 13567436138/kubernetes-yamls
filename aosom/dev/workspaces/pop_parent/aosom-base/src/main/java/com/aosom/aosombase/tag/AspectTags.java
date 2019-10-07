package com.aosom.aosombase.tag;

import com.aosom.aosombase.entity.BaseResponse;
import com.google.common.base.Joiner;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;

@Aspect
@Component
public class AspectTags {
    @Pointcut("@annotation(com.aosom.aosombase.tag.AddTags)")
    public void point() {
    }



    @AfterReturning(returning = "response",pointcut = "point()")
    public void doAfter(Object response) {
        if(response instanceof BaseResponse){
            HttpServletResponse httpResponse = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            BaseResponse  baseResponse=     (BaseResponse)  response;
            Set<String> tags=new HashSet<String>();
            try {
                TagParse.parseTag( baseResponse.getData(),tags);
                //用空格隔开的字符串
                httpResponse.addHeader("tags", Joiner.on(" ").join(tags) );
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        }


    }
}
