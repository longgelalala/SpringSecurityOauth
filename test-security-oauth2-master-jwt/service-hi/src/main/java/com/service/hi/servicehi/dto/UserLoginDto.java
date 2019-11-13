package com.service.hi.servicehi.dto;

import com.service.hi.servicehi.entity.JWT;
import com.service.hi.servicehi.entity.SysUser;

public class UserLoginDto {
    private JWT jwt;
    private SysUser user;

    public JWT getJwt() {
        return jwt;
    }

    public void setJwt(JWT jwt) {
        this.jwt = jwt;
    }

    public SysUser getUser() {
        return user;
    }

    public void setUser(SysUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserLoginDto{" +
                "jwt=" + jwt +
                ", user=" + user +
                '}';
    }
}
