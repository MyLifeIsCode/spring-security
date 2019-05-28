package com.myself.security.service.auth;

import com.myself.security.service.permission.IPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.security.auth.login.Configuration;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

@Slf4j
@Component
public class AuthService {

    @Autowired
    private IPermissionService permissionService;


    public boolean canAccess(HttpServletRequest request, Authentication authentication){
        log.info("Authservice.canAccess()");
        boolean b = false;
        log.info(authentication + "");
        //判断是否已经登陆,anonymousUser|userdetail.User
        Object principal = authentication.getPrincipal();
        if(principal == null || "anonymousUser".equals(principal)){
            return b;
        }
        //这里可以单独把AnonymousAuthenticationToken拿出来校验，也可以放到roles同意校验，其role为ROLE_ANONYMOUS
        if(authentication instanceof AnonymousAuthenticationToken){
            //check if this uri can be access by anonymus
            //return
        }
        Map<String, Collection<ConfigAttribute>> map = permissionService.getPermissionMap();
         Collection<ConfigAttribute> configAttributes = null;
         String resUrl ;
         //URL规则匹配
        AntPathRequestMatcher matcher;
        for (Iterator<String> it = map.keySet().iterator();it.hasNext();){
            resUrl = it.next();
            matcher = new AntPathRequestMatcher(resUrl);
            if(matcher.matches(request)){
                configAttributes = map.get(resUrl);
                break;
            }
        }
        ConfigAttribute cfa = null;
        String neetRole = null;
        for (Iterator<ConfigAttribute> it = configAttributes.iterator();it.hasNext();){
            cfa = it.next();
            neetRole = cfa.getAttribute();
            for (GrantedAuthority grantedAuthority : authentication.getAuthorities()){
                if (neetRole.equals(grantedAuthority.getAuthority())){
                    log.info("needRole = ",neetRole);
                    b = true;
                    break;
                }
            }
        }
        return b;
    }

}




























