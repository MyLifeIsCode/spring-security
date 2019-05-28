package com.myself.security.init;

import com.myself.security.domain.permission.Permission;
import com.myself.security.domain.user.UserInfo;
import com.myself.security.domain.role.*;
import com.myself.security.repository.permission.PermissionRepository;
import com.myself.security.repository.role.RoleRepository;
import com.myself.security.repository.user.UserInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DataInitService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

//    @PostConstruct
    public void dataInit() {



       List<Role> roles = new ArrayList<>();
        Role adminRole= new Role("admin","管理员");
        Role normalRole = new Role("normal","普通用户");
        roleRepository.save(adminRole);
        roleRepository.save(normalRole);

        roles.add(adminRole);
        roles.add(normalRole);

        UserInfo admin = new UserInfo();
        admin.setUserName("admin");
        admin.setPassword(passwordEncoder.encode("123"));
        admin.setRoles(roles);
//        userInfoRepository.save(admin);


        roles = new ArrayList<>();
        roles.add(normalRole);

        UserInfo user = new UserInfo();
        user.setUserName("user");
        user.setPassword(passwordEncoder.encode("123"));
        user.setRoles(roles);
//        userInfoRepository.save(user);


        Permission permission1 = new Permission();
        permission1.setUrl("/hello/helloUser");
        permission1.setName("普通用户URL");
        permission1.setDescription("普通用户的访问路径");
        permission1.setRoles(roles);
        permissionRepository.save(permission1);

        Permission permission2 = new Permission();
        permission2.setUrl("/hello/helloAdmin");
        permission2.setName("管理员URL");
        permission2.setDescription("管理员的访问路径");
        List<Role> roles2 = new ArrayList<>();
        roles2.add(adminRole);
        permission2.setRoles(roles2);
        permissionRepository.save(permission2);

    }

}
