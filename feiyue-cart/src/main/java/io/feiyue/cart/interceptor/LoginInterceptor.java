package io.feiyue.cart.interceptor;

import io.feiyue.cart.config.JwtProperties;
import io.feiyue.common.pojo.UserInfo;
import io.feiyue.common.utils.CookieUtils;
import io.feiyue.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtProperties jwtProperties;

    private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取cookie中的token
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());

        //解析token,获取用户信息
        UserInfo userInfo = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());

        if (userInfo == null) {
            return false;
        }

        //设置threadlocal
        THREAD_LOCAL.set(userInfo);

        return true;
    }

    public static UserInfo getUserInfo() {
        return THREAD_LOCAL.get();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //清空线程的局部变量, 因为使用的是tomcat的线程池,线程不会结束,也就不会释放线程的局部变量
        THREAD_LOCAL.remove();
    }
}
