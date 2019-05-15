package security;

import com.myself.security.SpringSecurityApplication;
import com.myself.security.domain.user.UserInfo;
import com.myself.security.repository.user.UserInfoRepository;
import com.myself.security.service.userinfo.IUserInfoService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringSecurityApplication.class,webEnvironment = SpringBootTest.WebEnvironment.NONE)

public class Test {
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @org.junit.Test
    public void test(){
        System.out.printf("11");
    }

    @org.junit.Test
    public void dataInit(){
        UserInfo admin = new UserInfo();
        admin.setUid(1L);
        admin.setUserName("admin");
        admin.setPassword(passwordEncoder.encode("123"));
//        admin.setRole(UserInfo.Role.ADMIN);
        userInfoRepository.save(admin);


        UserInfo user = new UserInfo();
        user.setUid(2L);
        user.setUserName("user");
        user.setPassword(passwordEncoder.encode("123"));
//        user.setRole(UserInfo.Role.NORMAL);
        userInfoRepository.save(user);

        List<UserInfo> all = userInfoRepository.findAll();
        System.out.printf("111");
    }
}
