package io.feiyua.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class FeiyueCorsConfigutation {

    @Bean
    public CorsFilter corsFilter() {
        //1 初始化cors配置对象
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //允许跨域的域名,如果要携带cookie,不呢个写*. *代表所有域名都可以跨域
        corsConfiguration.addAllowedOrigin("http://manage.feiyue.io");
        corsConfiguration.addAllowedOrigin("http://www.feiyue.io");
        corsConfiguration.setAllowCredentials(true); //允许携带cookie
        corsConfiguration.addAllowedMethod("*");//代表所有的请求方法:get post put delete
        corsConfiguration.addAllowedHeader("*");//允许携带任何头信息

        //2 初始化cors配置源对象
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", corsConfiguration);

        //3 返回corsFilter实例, 参数:cors配置元对象
        return new CorsFilter(configSource);
    }
}
