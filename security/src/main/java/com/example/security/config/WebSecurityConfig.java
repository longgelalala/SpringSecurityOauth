package com.example.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)//这个注解，可以开启security的注解，我们可以在需要控制权限的方法上面使用@PreAuthorize，@PreFilter这些注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;//注入自定义userdetailservice
	 
	@Bean
	public PasswordEncoder passwordEncoder() {//用于注入PasswordEncoder，定义加密方式
	    return new BCryptPasswordEncoder();
	}
	
	//通过重载该方法，可配置如何通过拦截器保护请求（WEB安全）    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http.csrf().disable()//禁用了 csrf 功能
	     .authorizeRequests()//限定签名成功的请求
	     .antMatchers("/decision/**","/govern/**").hasAnyRole("USER","ADMIN")//对decision和govern 下的接口 需要 USER 或者 ADMIN 权限
	     .antMatchers("/admin/login").permitAll()///admin/login 不限定
	     .antMatchers("/admin/**").hasRole("ADMIN")//对admin下的接口 需要ADMIN权限
	     .antMatchers("/oauth/**").permitAll()//不拦截 oauth 开放的资源
	     .anyRequest().permitAll()//其他没有限定的请求，允许访问
	     .and().anonymous()//对于没有配置权限的其他请求允许匿名访问
	     .and().formLogin()//使用 spring security 默认登录页面
	     .and().httpBasic();//启用http 基础验证
	}

    /**
     * 配置用户签名服务 主要是user-details 机制，
     * @param auth 签名管理器构造器，用于构建用户具体权限控制
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

	@Override
	public void configure(WebSecurity web) {
		super.configure(web);
	}
}

