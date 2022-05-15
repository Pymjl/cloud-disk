package cuit.pymjl.interceptor;

import cn.hutool.core.util.StrUtil;
import cuit.pymjl.exception.AppException;
import cuit.pymjl.util.JwtUtils;
import cuit.pymjl.util.RedisUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/16 0:11
 **/
@Log4j2
@Component
public class TokenInterceptor implements HandlerInterceptor {
    private static final String BEARER = "Bearer ";
    @Resource
    RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("开始从Authorization获取token......");
        String header = request.getHeader("Authorization");
        if (header != null && !"".equals(header)) {
            if (header.startsWith(BEARER)) {
                //获得token
                String token = header.substring(7);
                log.info("获取到的token==>[{}]", token);
                Claims claims = JwtUtils.verifyJwt(token);
                String userId = (String) redisUtil.get(token);
                if (StrUtil.isBlank(userId)) {
                    throw new AppException("token已过期,请重新登录");
                }
                log.info("获取到的用户ID==>[{}]", userId);
                request.setAttribute("userId", userId);
                return true;
            }
        }
        throw new AppException("请先登录");
    }
}
