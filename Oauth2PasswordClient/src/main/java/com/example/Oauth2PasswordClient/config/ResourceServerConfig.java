package com.example.Oauth2PasswordClient.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsUtils;


/**
 * 这个类表明了此应用是OAuth2 的资源服务器，此处主要指定了受资源服务器保护的资源链接
 * 1、不加该资源服务器权限认证一直出现问题不起作用
 * 2、加了该资源服务器，配置了权限，表单登陆又不起作用
 * 3、ResourceServerConfigurerAdapter与WebSecurityConfigurerAdapter里面的 configure(HttpSecurity http)配置冲突但是只有资源服务器里面的生效
 */

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

/*	//密码登陆模式
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()//禁用了 csrf 功能
                .authorizeRequests()//限定签名成功的请求
                .antMatchers("/decision/**","/govern/**").hasAnyRole("USER","ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/test/**").authenticated()//必须认证过后才可以访问
                .anyRequest().permitAll()//其他没有限定的请求，允许随意访问
                .and().anonymous();//对于没有配置权限的其他请求允许匿名访问
    }*/


    //必须在资源服务器里配置配能对权限控制
    //这边的配置才起了作用
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()//禁用了 csrf 功能
                .authorizeRequests()//限定签名成功的请求
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()//就是这一行啦
                .antMatchers("/test/**", "/admin/**", "/user/**").authenticated()//签名成功后可访问
                .antMatchers("/user/**").hasRole("ADMIN")
                .anyRequest().permitAll()//其他没有限定的请求，允许访问
//	            .and().anonymous()//对于没有配置权限的其他请求允许匿名访问
                .and().formLogin().permitAll()//使用 spring security 默认登录页面
                .and().httpBasic();//启用http 基础验证
    }
}


