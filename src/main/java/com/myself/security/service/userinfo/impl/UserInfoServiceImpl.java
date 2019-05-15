package com.myself.security.service.userinfo.impl;

import com.myself.security.domain.user.UserInfo;
import com.myself.security.repository.user.UserInfoRepository;
import com.myself.security.service.userinfo.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements IUserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserInfo findByUserName(String userName) {
        return userInfoRepository.findByUserName(userName);
    }
}
