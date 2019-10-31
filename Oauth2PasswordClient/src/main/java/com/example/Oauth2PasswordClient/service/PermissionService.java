package com.example.Oauth2PasswordClient.service;

import java.util.List;
import com.example.Oauth2PasswordClient.entity.Permission;

public interface PermissionService {
    public List<Permission> findAll();
    public List<Permission> findByAdminUserId(int userId);
}
