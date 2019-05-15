package com.myself.security.domain.role;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Role {
    @Id
    @GeneratedValue
    private long rid;//主键.
    private String name;//角色名称.
    private String description;//角色描述.

    public Role() {
    }

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }

}