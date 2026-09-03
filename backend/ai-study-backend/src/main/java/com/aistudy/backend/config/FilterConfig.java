package com.aistudy.backend.config;


import com.aistudy.backend.filter.LoginFilter;
import com.aistudy.backend.user.mapper.UserMapper;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;



@Configuration
public class FilterConfig {


    @Bean
    public FilterRegistrationBean<LoginFilter> loginFilter(
            UserMapper userMapper
    ){


        FilterRegistrationBean<LoginFilter> bean =
                new FilterRegistrationBean<>();


        bean.setFilter(
                new LoginFilter(userMapper)
        );


        bean.addUrlPatterns(
                "/*"
        );


        /*
         * 最后执行
         */
        bean.setOrder(
                Ordered.LOWEST_PRECEDENCE
        );


        return bean;

    }


}