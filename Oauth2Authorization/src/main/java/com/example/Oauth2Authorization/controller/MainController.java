package com.example.Oauth2Authorization.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin")
public class MainController {
 
    @RequestMapping("/")
    public String index(){
        return "index" ;
    }
 
    @RequestMapping("/detail")
    public String hello(){
        return "hello" ;
    }
 
    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
