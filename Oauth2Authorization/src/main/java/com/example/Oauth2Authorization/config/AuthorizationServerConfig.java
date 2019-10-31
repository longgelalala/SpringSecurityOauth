package com.example.Oauth2Authorization.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

@Configuration
@EnableAuthorizationServer // 这个注解告诉 Spring 这个应用是 OAuth2 的授权服务器//
// 提供/oauth/authorize,/oauth/token,/oauth/check_token,/oauth/confirm_access,/oauth/error
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
 
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
 
    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;
 
    @Autowired
    private UserDetailsService userDetailsService;
 

    @Autowired
    @Qualifier("passwordEncoder")
    PasswordEncoder passwordEncoder;//BCryptPasswordEncoder
 
    @Bean
    public TokenStore tokenStore() {
//        return new InMemoryTokenStore(); //使用内存中的 token store
        return new JdbcTokenStore(dataSource); ///使用Jdbctoken store
    }
 
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource)
        		//授权码模式
                .withClient("client_code")
                .secret(passwordEncoder.encode("123456"))
                .authorizedGrantTypes("authorization_code", "refresh_token",
                        "password", "implicit")
                .scopes("all")
                .authorities("ROLE_ADMIN")
                .redirectUris("https://www.baidu.com")
                .accessTokenValiditySeconds(1200)
                .refreshTokenValiditySeconds(50000);

    }
 
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)//必须设置 UserDetailsService 否则刷新token 时会报错
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
    }
 
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
		/* 配置token获取合验证时的策略 */
        .tokenKeyAccess("permitAll()")// 开启/oauth/token_key验证端口无权限访问
        .checkTokenAccess("isAuthenticated()")// 开启/oauth/check_token验证端口认证权限访问
        .allowFormAuthenticationForClients();//允许表单登录
 
    }
}

