package com.service.hi.servicehi.service;

import com.service.hi.servicehi.entity.SysUser;

public interface UserService {

	SysUser create(String username, String password);

}
