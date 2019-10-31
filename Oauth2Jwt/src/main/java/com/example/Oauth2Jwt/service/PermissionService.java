package com.example.Oauth2Jwt.service;

import java.util.List;
import com.example.Oauth2Jwt.entity.Permission;

public interface PermissionService {
    public List<Permission> findAll();
    public List<Permission> findByAdminUserId(int userId);
}
