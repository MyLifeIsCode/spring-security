package com.myself.security.domain.user;

import lombok.Data;

import javax.persistence.*;
import com.myself.security.domain.role.*;

import java.util.List;

@Entity
@Data
public class UserInfo {
//    public enum Role{
//        ADMIN,NORMAL
//    }

    @Id
    @GeneratedValue
    private Long uid;

    private String userName;

    private String password;

    //用户－－角色：多对多的关系．
    @ManyToMany(fetch = FetchType.EAGER)//立即从数据库中进行加载数据;
    @JoinTable(name = "UserRole", joinColumns = {@JoinColumn(name = "uid")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;

}