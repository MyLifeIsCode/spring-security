package com.myself.security.service.userinfo;


import com.myself.security.domain.user.UserInfo;

public interface IUserInfoService {
    UserInfo findByUserName(String userName);
}
