package com.example.Oauth2Authorization.service;

import java.util.List;

import com.example.Oauth2Authorization.entity.Permission;

public interface PermissionService {
    public List<Permission> findAll();
    public List<Permission> findByAdminUserId(int userId);
}
