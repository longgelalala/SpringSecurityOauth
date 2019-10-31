package com.example.security.service;

import java.util.List;
import com.example.security.entity.Permission;

public interface PermissionService {
    public List<Permission> findAll();
    public List<Permission> findByAdminUserId(int userId);
}
