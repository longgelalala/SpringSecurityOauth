package com.example.Oauth2Authorization.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.Oauth2Authorization.entity.Permission;

@Repository
public interface PermissionRepository extends CrudRepository<Permission,Integer> {
 
}
