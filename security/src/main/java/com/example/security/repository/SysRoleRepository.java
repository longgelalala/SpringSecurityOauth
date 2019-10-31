package com.example.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.security.entity.SysRole;


@Repository
public interface SysRoleRepository extends CrudRepository<SysRole,Integer> {
	
}
