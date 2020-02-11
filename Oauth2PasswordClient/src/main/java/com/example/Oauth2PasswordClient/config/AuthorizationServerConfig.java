package com.example.Oauth2PasswordClient.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;

//@Order(2)
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

//    @Bean
//    public TokenStore tokenStore() {
////        return new InMemoryTokenStore(); //使用内存中的 token store
//        return new JdbcTokenStore(dataSource); ///使用Jdbctoken store
//    }

    //使用jwt格式的token进行计算
    @Value("${security.oauth2.jwt.signingKey}")
    private String signingKey;

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(signingKey);
        return jwtAccessTokenConverter;
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }


    //    ClientDetailsServiceConfigurer：用来配置客户端详情服务（ClientDetailsService），客户端详情信息在这里进行初始化，
    //    你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        这里使用Jdbc实现客户端详情服务，数据源dataSource不做叙述，使用框架默认的表，
        clients.jdbc(dataSource);
        //密码登陆模式
//        clients.inMemory()
//                .withClient("client")
//                .secret(new BCryptPasswordEncoder().encode("123456"))//密码采用
//                .authorizedGrantTypes("password", "refresh_token")//允许授权范围
//                .authorities("ROLE_ADMIN", "ROLE_USER")//客户端可以使用的权限
//                .scopes("read", "write")
//                .accessTokenValiditySeconds(7200)//token超时时间
//                .refreshTokenValiditySeconds(7200)//token刷新的token超时时间
//
//                //客户端模式
//                .and().withClient("client_1")
//                .secret(passwordEncoder.encode("123456"))
//                .authorizedGrantTypes("client_credentials")
//                .scopes("read", "write")
//                .authorities("client_credentials")
//                .accessTokenValiditySeconds(7200);
    }

    // 用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
                .userDetailsService(userDetailsService)//必须设置 UserDetailsService 否则刷新token 时会报错
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);//可以使用get也可以使用post
    }


    // 用来配置令牌端点(Token Endpoint)的安全约束.
    /**
     *这里主要做的事情，就是如果开启了allowFormAuthenticationForClients，那么就在BasicAuthenticationFilter之前添加clientCredentialsTokenEndpointFilter，使用ClientDetailsUserDetailsService来进行client端登录的验证
     *链接：https://www.jianshu.com/p/57cfdfbf57dc
     * */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                /* 配置token获取合验证时的策略 */
                .tokenKeyAccess("permitAll()")// 开启/oauth/token_key验证端口无权限访问
                .checkTokenAccess("isAuthenticated()")// 开启/oauth/check_token验证端口认证权限访问

                .allowFormAuthenticationForClients();//允许表单登录
    }
}

