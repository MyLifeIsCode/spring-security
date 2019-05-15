package com.myself.security.repository.user;

import com.myself.security.domain.user.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {

    UserInfo findByUserName(String username);
}
