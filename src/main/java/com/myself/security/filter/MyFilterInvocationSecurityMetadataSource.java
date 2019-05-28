package com.myself.security.filter;

import com.myself.security.service.permission.IPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
        log.info(filterInvocation.getFullRequestUrl());
        if(isMatcherAllowedRequest(filterInvocation)) return null;//return null 表示允许访问，不做拦截

        HttpServletRequest request = filterInvocation.getHttpRequest();
        String resUrl;
        //URL规则匹配
        AntPathRequestMatcher matcher;
        for (Iterator<String> it = map.keySet().iterator();it.hasNext();){
            resUrl = it.next();
            matcher = new AntPathRequestMatcher(resUrl);
            if(matcher.matches(request)){
                log.info(map.get(resUrl).toString());
                return map.get(resUrl);
            }
        }
        //SecurityConfig.createList("ROLE_USER");
        //方式一：没有匹配到,直接是白名单了.不登录也是可以访问的。
        //return null;
        //方式二：配有匹配到，需要指定相应的角色：
        return SecurityConfig.createList("ROLE_admin");
    }

    /**
     * 判断当前请求是否在允许请求的范围内
     * @param fi 当前请求
     * @return 是否在范围中
     */
    private boolean isMatcherAllowedRequest(FilterInvocation fi){
        return allowedRequest().stream().map(AntPathRequestMatcher::new)
                .filter(requestMatcher -> requestMatcher.matches(fi.getHttpRequest()))
                .toArray().length > 0;
    }

    /**
     * 定义允许请求的列表
     * @return
     */
    private List<String> allowedRequest(){
        return Arrays.asList("/login","/css/**","/fonts/**","/js/**","/scss/**","/img/**");
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
























