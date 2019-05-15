package com.myself.security.service.userinfo.impl;

import com.myself.security.domain.user.UserInfo;
import com.myself.security.service.userinfo.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.myself.security.domain.role.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("CustomUserDetailService.loadUserByUserName");
        UserInfo userInfo = userInfoService.findByUserName(userName);
        if(Objects.isNull(userInfo)){
            throw new UsernameNotFoundException("not fount");
        }

        //定义权限
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 用户可以访问的资源名称（或者说用户所拥有的权限） 注意：必须"ROLE_"开头
        userInfo.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });

        User userDetail = new User(userInfo.getUserName(),userInfo.getPassword(),authorities);
        return userDetail;
    }
}













