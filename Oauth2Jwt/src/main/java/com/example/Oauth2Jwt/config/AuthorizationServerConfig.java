package com.example.Oauth2Jwt.config;

import java.util.Arrays;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
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
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import com.example.Oauth2Jwt.service.impl.CustomTokenEnhancer;

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
 
/*    @Autowired
    TokenStore tokenStore;//token模式
    
    @Autowired
    JwtAccessTokenConverter accessTokenConverter;//jwt配置
*/
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("mytest.jks"), "mypass".toCharArray());//证书路径和密钥库密码
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("mytest"));//密钥别名
        return converter;
    }
    
	@Bean
	public TokenEnhancer tokenEnhancer() {
		return new CustomTokenEnhancer();
	}

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
/*        		//授权码模式
                .withClient("client_code")
                .secret(passwordEncoder.encode("123456"))
                .authorizedGrantTypes("authorization_code", "refresh_token",
                        "password", "implicit")
                .scopes("all")
                .authorities("ROLE_ADMIN")
                .redirectUris("https://www.baidu.com")
                .accessTokenValiditySeconds(1200)
                .refreshTokenValiditySeconds(50000);*/

    }
 
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(
                Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));//令牌中添加自定义信息

        endpoints.tokenStore(tokenStore())
        		.tokenEnhancer(tokenEnhancerChain)//令牌配置
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)//必须设置 UserDetailsService 否则刷新token 时会报错
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);
        endpoints.accessTokenConverter(jwtAccessTokenConverter());
        
        //自定义授权页
        endpoints.pathMapping("/oauth/confirm_access", "/confirm");
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

