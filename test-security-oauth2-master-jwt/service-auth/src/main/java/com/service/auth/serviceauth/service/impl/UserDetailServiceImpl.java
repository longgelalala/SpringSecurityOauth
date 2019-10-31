package com.service.auth.serviceauth.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.service.auth.serviceauth.entity.Permission;
import com.service.auth.serviceauth.entity.SysUser;
import com.service.auth.serviceauth.repository.SysUserRepository;
import com.service.auth.serviceauth.service.PermissionService;

//自定义userdetailservice
@Service
public class UserDetailServiceImpl implements UserDetailsService {

  @Autowired
  SysUserRepository sysUserRepository;
  @Autowired
  PermissionService permissionService;
  @Autowired
  PasswordEncoder passwordEncoder;//BCryptPasswordEncoder

  @Transactional
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      SysUser sysUser = sysUserRepository.getUserByName(username);
      List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
      if (sysUser != null) {
          System.err.println("sysUser===============" + sysUser);
          //获取用户的授权
          List<Permission> permissions = permissionService.findByAdminUserId(sysUser.getId());
          //声明授权文件
          for (Permission permission : permissions) {
              if (permission != null && permission.getName() != null) {
                  GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_"+permission.getName());//spring Security中权限名称必须满足ROLE_XXX
                  grantedAuthorities.add(grantedAuthority);
              }
          }
      }
      System.err.println("grantedAuthorities===============" + grantedAuthorities);
      return new User(sysUser.getName(), sysUser.getPassword(), grantedAuthorities);
  }
}

