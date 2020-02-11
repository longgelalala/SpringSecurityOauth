package com.example.Oauth2PasswordClient.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.Oauth2PasswordClient.entity.SysRole;

import java.util.List;


@Repository
public interface SysRoleRepository extends CrudRepository<SysRole,Integer> {
	List<SysRole> findAll();
}
