package com.myself.security.service.permission.impl;

import com.google.common.collect.Maps;
import com.myself.security.domain.permission.Permission;
import com.myself.security.domain.role.Role;
import com.myself.security.repository.permission.PermissionRepository;
import com.myself.security.service.permission.IPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    private Map<String,Collection<ConfigAttribute>> permissionMap = Maps.newHashMap();

    @PostConstruct
    public void initPermisions(){
        log.info("PermissionServiceImpl.initPermissions()");
        permissionMap = new HashMap<>();
        Collection<ConfigAttribute> collection;
        ConfigAttribute cfg;

        List<Permission> permissions = permissionRepository.findAll();
        for (Permission permission:permissions){
            collection = new ArrayList<>();
            for (Role role: permission.getRoles()){
                cfg = new SecurityConfig("ROLE_" + role.getName());
                collection.add(cfg);
            }
            permissionMap.put(permission.getUrl(),collection);
        }
        log.info(permissionMap.toString() + "===========");
    }
    @Override
    public Map<String, Collection<ConfigAttribute>> getPermissionMap() {
        if(permissionMap.size() == 0 ){
            initPermisions();
        }
        return permissionMap;
    }
}






























