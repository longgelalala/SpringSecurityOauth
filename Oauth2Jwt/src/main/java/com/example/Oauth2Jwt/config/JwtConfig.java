package com.example.Oauth2Jwt.config;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.util.FileCopyUtils;

@Configuration
public class JwtConfig {
	
/*    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {//jwt配置
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("111");
        return converter;
    }*/
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        Resource resource =  new ClassPathResource("public.txt");
        String publicKey;
        try {
            publicKey = new String(FileCopyUtils.copyToByteArray(resource.getInputStream()));
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        converter.setVerifierKey(publicKey);
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {//配置token模式
        return new JwtTokenStore(accessTokenConverter());
    }
 
    @Bean
    @Primary//@Primary：自动装配时当出现多个Bean候选者时，被注解为@Primary的Bean将作为首选者，否则将抛出异常
    public DefaultTokenServices tokenServices() {//用于资源服务器
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }
}
