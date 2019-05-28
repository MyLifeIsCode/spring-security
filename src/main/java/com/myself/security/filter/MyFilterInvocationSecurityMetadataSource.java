package com.myself.security.filter;

import com.myself.security.service.permission.IPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import java.util.Collection;
import java.util.Map;

/**
 * 加载权限信息
 * 继承FilterInvocationSecurityMetadataSource重写getAttributes的方法进行通过uri获取权限配置信息：
 */
@Slf4j
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {


    @Autowired
    private IPermissionService permissionService;

    /**
     * 此方法为了判定用户请求url 是否在权限表中，如果在权限表中，则返回给decide方法，
     * @param object -->FilterInvocation
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        log.info("MyFilterInvocationSecurityMetadataSource.getAttributes()");
        Map<String,Collection<ConfigAttribute>> map = permissionService.getPermissionMap();
        FilterInvocation filterInvocation = (FilterInvocation)object;
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
