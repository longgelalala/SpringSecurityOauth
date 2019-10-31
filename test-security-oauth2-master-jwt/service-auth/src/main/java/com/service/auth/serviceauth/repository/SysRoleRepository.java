package com.service.auth.serviceauth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.service.auth.serviceauth.entity.SysRole;

@Repository
public interface SysRoleRepository extends CrudRepository<SysRole,Integer> {
	
}
