package com.service.auth.serviceauth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.service.auth.serviceauth.entity.Permission;

@Repository
public interface PermissionRepository extends CrudRepository<Permission,Integer> {
 
}
