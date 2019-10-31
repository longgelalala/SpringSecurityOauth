package com.example.Oauth2Authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Order(2)
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)//这个注解，可以开启security的注解，我们可以在需要控制权限的方法上面使用@PreAuthorize，@PreFilter这些注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
		@Autowired
	    private UserDetailsService userDetailsService;//注入自定义userdetailservice
	    
	    @Bean
	    public PasswordEncoder passwordEncoder() {//用于注入PasswordEncoder
	        return new BCryptPasswordEncoder();
	    }
	    
	    @Bean
	    public AuthenticationManager authenticationManagerBean() throws Exception {//用于注入AuthorizationServerConfig的AuthenticationManager
	        return super.authenticationManagerBean() ;
	    }
	    
		//通过重载该方法，可配置如何通过拦截器保护请求（WEB安全）    
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	    	//不拦截/oauth/**，/login/**，/logout/**(requestMatchers用于但需要过滤多个HttpSecurity的情况)
	        http.requestMatchers().antMatchers("/oauth/**", "/login/**", "/logout/**")//使HttpSecurity接收以"/login/","/oauth/","/logout/"开头请求。
	                .and().authorizeRequests().antMatchers("/oauth/**").authenticated()
	                .and().formLogin();
	    }

	   	//通过重载该方法，可配置user-detail（用户详细信息）服务（身份验证管理生成器）
	    @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.userDetailsService(userDetailsService)//自定义用户存储
	                .passwordEncoder(passwordEncoder());//密码加密
	    }

	    //通过重载该方法，可配置Spring Security的Filter链（HTTP请求安全处理）
	    @Override
	    public void configure(WebSecurity web) {
	        super.configure(web);
	    }

	
}
