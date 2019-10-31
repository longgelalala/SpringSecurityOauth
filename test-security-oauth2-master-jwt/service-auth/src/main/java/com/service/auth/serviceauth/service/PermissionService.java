package com.service.auth.serviceauth.service;

import java.util.List;

import com.service.auth.serviceauth.entity.Permission;

public interface PermissionService {
    public List<Permission> findAll();
    public List<Permission> findByAdminUserId(int userId);
}
