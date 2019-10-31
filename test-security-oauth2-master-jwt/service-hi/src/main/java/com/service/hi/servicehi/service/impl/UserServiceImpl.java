package com.service.hi.servicehi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.service.hi.servicehi.entity.SysUser;
import com.service.hi.servicehi.repository.SysUserRepository;
import com.service.hi.servicehi.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    SysUserRepository sysUserRepository;

    @Override
    @Transactional
    public SysUser create(String username, String password) {
    	if(sysUserRepository.getUserByName(username) == null){
        	SysUser user = new SysUser();
            user.setName(username);
            password = passwordEncoder.encode(password);
            user.setPassword(password);
            user = sysUserRepository.save(user);
            return user;
    	}else{
    		return null;
    	}
    }
}
