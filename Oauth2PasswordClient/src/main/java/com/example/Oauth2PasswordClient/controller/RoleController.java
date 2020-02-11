package com.example.Oauth2PasswordClient.controller;


import com.example.Oauth2PasswordClient.repository.SysRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/role")
public class RoleController {


    @Autowired
    SysRoleRepository sysRoleRepository;


    @RequestMapping(value = "/getAll")
    public List testVue() {
        return sysRoleRepository.findAll();
    }

}
