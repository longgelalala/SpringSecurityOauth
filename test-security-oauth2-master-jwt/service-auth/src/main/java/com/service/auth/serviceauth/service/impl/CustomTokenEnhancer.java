package com.service.auth.serviceauth.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class CustomTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,OAuth2Authentication authentication) {
        Map<String, Object> additionalInformation = new HashMap<>();
 
        String userName = authentication.getUserAuthentication().getName();
        User user = (User) authentication.getUserAuthentication().getPrincipal();
        additionalInformation.put("userName", userName);
        additionalInformation.put("roles", user.getAuthorities());
        additionalInformation.put("organization", authentication.getName()
//                + randomAlphabetic(4)
        );
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
        return accessToken;
    }
}
