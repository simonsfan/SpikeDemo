package com.spike.demo.annotation;


import com.spike.demo.util.IpUtil;
import com.spike.demo.util.ResultEnum;
import com.spike.demo.util.ResultUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName AccessLimitInterceptor
 * @Description 自定义拦截器
 * @Author simonsfan
 * @Date 2018/12/19
 * Version  1.0
 */
@Component
public class AccessLimitInterceptor extends HandlerInterceptorAdapter {

    @Resource(name="redisTemplate")
    private RedisTemplate<String, Integer> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            boolean annotationPresent = method.isAnnotationPresent(AccessLimit.class);
            if (!annotationPresent) {
                return false;
            }
            AccessLimit accessLimit = method.getAnnotation(AccessLimit.class);
            int threshold = accessLimit.threshold();
            int time = accessLimit.time();
            String ip = IpUtil.getIpAddrAdvanced(request);
            Integer limitRecord = redisTemplate.opsForValue().get(ip);
            if (limitRecord == null) {
                redisTemplate.opsForValue().set(ip, 1, time, TimeUnit.SECONDS);
            } else if (limitRecord < threshold) {
                redisTemplate.opsForValue().set(ip, limitRecord+1, time, TimeUnit.SECONDS);
            } else {
                outPut(response, ResultEnum.FREQUENT);
                return false;
            }
        }
        return true;
    }

    public void outPut(HttpServletResponse response, ResultEnum resultEnum) {
        try {
            response.getWriter().write(ResultUtil.success(resultEnum.getCode(),resultEnum.getMessage()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
