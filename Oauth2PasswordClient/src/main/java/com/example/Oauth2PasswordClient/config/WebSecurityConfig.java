package com.example.Oauth2PasswordClient.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsUtils;


@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)//这个注解，可以开启security的注解，我们可以在需要控制权限的方法上面使用@PreAuthorize，@PreFilter这些注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;//注入自定义userdetailservice

    @Bean
    public PasswordEncoder passwordEncoder() {
        //用于注入PasswordEncoder，定义加密方式
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        //用于注入AuthorizationServerConfig的AuthenticationManager
        return super.authenticationManagerBean();
    }

    //配置不起作用
    //通过重载该方法，可配置如何通过拦截器保护请求（WEB安全），确定需要什么权限
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //在Security的默认拦截器里，默认会开启CSRF处理，判断请求是否携带了token，如果没有就拒绝访问。
        //并且，在请求为(GET|HEAD|TRACE|OPTIONS)时，则不会开启
        http.authorizeRequests()//限定签名成功的请求
//                .antMatchers("/test/**","/admin/**","/user/**").authenticated()//签名成功后可访问
//				.antMatchers("/admin/**").hasRole("ADMIN1")
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()//就是这一行啦
                .anyRequest().authenticated()//其他没有限定的请求，允许访问
                .and().formLogin()
                    .permitAll()
                .and().httpBasic()
                .and().csrf().disable();//启用http 基础验证（get后面可以加参数）

    }

    /**
     * 配置用户签名服务 主要是user-details 机制，
     *
     * @param auth 签名管理器构造器，用于构建用户具体权限控制
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //传进去userDetail实体，以及密码的加密方式
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }
}

