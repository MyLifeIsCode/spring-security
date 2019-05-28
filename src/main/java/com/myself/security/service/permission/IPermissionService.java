package com.myself.security.service.permission;

import org.springframework.security.access.ConfigAttribute;

import java.util.Collection;
import java.util.Map;

public interface IPermissionService {
    public Map<String, Collection<ConfigAttribute>> getPermissionMap();
}
