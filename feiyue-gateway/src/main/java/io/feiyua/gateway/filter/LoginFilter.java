package io.feiyua.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.feiyua.gateway.config.FilterProperties;
import io.feiyua.gateway.config.JwtProperties;
import io.feiyue.common.utils.CookieUtils;
import io.feiyue.common.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Component //放到同一个容器中
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {

        //获取白名单
        List<String> allowPaths = this.filterProperties.getAllowPaths();

        //初始化运行上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request对象
        HttpServletRequest request = context.getRequest();
        //获取请求的路径
        String url = request.getRequestURL().toString();

        for (String allowPath : allowPaths) {
            if (StringUtils.contains(url, allowPath)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Object run() throws ZuulException {

        //初始化运行上下文
        RequestContext context = RequestContext.getCurrentContext();
        //获取request对象
        HttpServletRequest request = context.getRequest();

        String token = CookieUtils.getCookieValue(request, this.jwtProperties.getCookieName());

        if (StringUtils.isBlank(token)) {
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }

        try {
            JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();

            context.setSendZuulResponse(false);
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }

        return null;
    }
}
