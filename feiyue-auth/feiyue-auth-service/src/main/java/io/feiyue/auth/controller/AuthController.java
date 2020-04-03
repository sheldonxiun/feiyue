package io.feiyue.auth.controller;

import io.feiyue.auth.config.JwtProperties;
import io.feiyue.auth.service.AuthService;
import io.feiyue.common.pojo.UserInfo;
import io.feiyue.common.utils.CookieUtils;
import io.feiyue.common.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("accredit")
    public ResponseEntity<Void> accredit(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        //返回的token在controller中,存入到cookie,因为controller容易获取request和response
        String token = this.authService.accredit(username, password);

        if (StringUtils.isBlank(token)) {
            //未认证 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CookieUtils.setCookie(request, response, this.jwtProperties.getCookieName(), token, this.jwtProperties.getExpire() * 60);

        return ResponseEntity.ok(null);
    }

    @GetMapping("verify")
    public ResponseEntity<UserInfo> verify(@CookieValue("FEIYUE_TOKEN") String token,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        try {
            //通过jwt工具类使用公钥解析jwt
            UserInfo userInfo = JwtUtils.getInfoFromToken(token, this.jwtProperties.getPublicKey());

            if (userInfo == null) {
                //未认证
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            //30分钟内, 只要用户还在操作就要更新Cookie信息,和jwt信息
            //1 刷新jwt中有效信息
            JwtUtils.generateToken(userInfo, this.jwtProperties.getPrivateKey(), this.jwtProperties.getExpire());

            //2 刷新cookie信息
            CookieUtils.setCookie(request, response, this.jwtProperties.getCookieName(), token, this.jwtProperties.getExpire() * 60);

            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
