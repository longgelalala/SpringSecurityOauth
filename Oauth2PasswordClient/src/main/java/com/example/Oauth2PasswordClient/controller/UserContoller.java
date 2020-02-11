package com.example.Oauth2PasswordClient.controller;

import com.example.Oauth2PasswordClient.entity.SysUser;
import com.example.Oauth2PasswordClient.entity.UserInfo;
import com.example.Oauth2PasswordClient.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserContoller {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SysUserRepository sysUserRepository;

    //开放端口给用户注册
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public void signUp(@RequestBody SysUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        sysUserRepository.save(user);
    }

    @RequestMapping(value = "/info")
    public UserInfo testVue() {
//        roles: ['admin'],
//        introduction: 'I am a super administrator',
//                avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
//                name: 'Super Admin'
        UserInfo userInfo = new UserInfo();
        userInfo.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        userInfo.setIntroduction("I am a super administrator");
        userInfo.setName("Super Admin");
        List<String> role = new ArrayList<String>();
        role.add("admin");
        userInfo.setRoles(role);
        return userInfo;
    }
}
