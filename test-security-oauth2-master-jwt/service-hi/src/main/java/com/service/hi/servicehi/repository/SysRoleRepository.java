package com.service.hi.servicehi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.service.hi.servicehi.entity.SysRole;


@Repository
public interface SysRoleRepository extends CrudRepository<SysRole,Integer> {
	
}
