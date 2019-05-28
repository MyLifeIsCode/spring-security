package com.myself.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

@Slf4j
public class MyAccessDecisionManager implements AccessDecisionManager {
    /**
     * 方法是判断是否拥有权限的决策方法
     * （1）authtication 是释CustomUserService中添加到GrantedAuthority对象中的权限信息集合
     * （2）object 包含客户端发起的请求的request信息，可转换为HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
     * （3）configAttributes 为MyFilterInvocationSecurityMetadataSource的getAttributes(Object object)这个方法返回的结果，此方法是为了判定用户请求的url是否在权限列表中，如果在权限表中，则返回decide方法
     * @param authentication
     * @param object
     * @param configAttributes
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        log.info("MyAccessDecisionManager.decide()");
        if(configAttributes == null || configAttributes.size() == 0){
            throw new AccessDeniedException("permission denied");
        }

        ConfigAttribute cfa ;
        String needRole;
        //遍历URL获取权限信息和用户信息的角色信息进行对比
        for (Iterator<ConfigAttribute> it = configAttributes.iterator();it.hasNext();){
            cfa = it.next();
            needRole = cfa.getAttribute();
            log.info("decide,needRole:"+needRole+",authentication="+authentication);
            for (GrantedAuthority grantedAuthority: authentication.getAuthorities()){
                if (needRole.equals(grantedAuthority.getAuthority())){
                    return;
                }
            }
        }
        throw new AccessDeniedException("permission denied");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }
}


































