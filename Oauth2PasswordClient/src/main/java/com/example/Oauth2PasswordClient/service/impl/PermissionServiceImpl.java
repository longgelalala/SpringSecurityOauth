package com.example.Oauth2PasswordClient.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Oauth2PasswordClient.entity.Permission;
import com.example.Oauth2PasswordClient.repository.PermissionRepository;
import com.example.Oauth2PasswordClient.service.PermissionService;


@Service
public class PermissionServiceImpl implements PermissionService {
 
    @Autowired
    PermissionRepository permissionRepository ;
 
    @PersistenceContext
    private EntityManager entityManager;
 
    @Override
    public List<Permission> findAll() {
        return null;
    }
 
    @Override
    public List<Permission> findByAdminUserId(int userId) {
 
        List<Permission> list = new ArrayList<Permission>();
        List<Object[]> abcs = entityManager.createNativeQuery("select p.* \n" +
                "        from sys_user u\n" +
                "        LEFT JOIN sys_role_user sru on u.id= sru.Sys_User_id\n" +
                "        LEFT JOIN sys_role r on sru.Sys_Role_id=r.id\n" +
                "        LEFT JOIN sys_permission_role spr on spr.role_id=r.id\n" +
                "        LEFT JOIN sys_permission p on p.id =spr.permission_id\n" +
                "        where u.id="+userId).getResultList();
        for (Object[] abc : abcs) {
            Permission permission = new Permission();
            permission.setId(Integer.valueOf(abc[0]+""));
            permission.setName(abc[1]+"");
            permission.setDescritpion(abc[2]+"");
            permission.setUrl(abc[3]+"");
//            permission.setPid(Integer.valueOf(abc[4]+""));
            list.add(permission);
        }
        return list;
    }
}
