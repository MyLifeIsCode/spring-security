package com.myself.security.domain.permission;

import com.myself.security.domain.role.Role;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Permission {
    @Id
    @GeneratedValue
    private long id;//主键.

    private String name;//权限名称.

    private String description;//权限描述.

    /**
     *  注意：Permission 表的url通配符为两颗星，比如说 /user下的所有url，应该写成 /user/**;
     */
    private String url;//授权链接

    private long pid;//父节点id.


    // 角色 - 权限是多对多的关系
    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(name="RolePermission",joinColumns= {@JoinColumn(name="permission_id")} , inverseJoinColumns= {@JoinColumn(name="role_id")})
    private List<Role> roles;

}
