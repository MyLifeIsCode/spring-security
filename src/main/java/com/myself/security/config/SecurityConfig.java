package com.myself.security.config;

import com.myself.security.filter.MyAccessDecisionManager;
import com.myself.security.filter.MyFilterInvocationSecurityMetadataSource;
import com.myself.security.service.userinfo.impl.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import javax.sql.DataSource;

//@Configuration：注解这是一个配置类。
//@EnableWebSecurity：注解开启Spring Security的功能。
//如何开启方法级别安全控制
//想要开启Spring方法级安全，你需要在已经添加了@Configuration注解的类上再添加@EnableGlobalMethodSecurity注解即可。
//使用注解@PreAuthorize("hasAnyRole('admin')")即可指定访问级别的角色。


//（1）prePostEnabled :决定Spring Security的前注解是否可用 [@PreAuthorize,@PostAuthorize,..]
//（2）secureEnabled : 决定是否Spring Security的保障注解 [@Secured] 是否可用。
//（3）jsr250Enabled ：决定 JSR-250 annotations 注解[@RolesAllowed..] 是否可用。
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true,jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /** 用来防止token被修改的key */
    private String rememberMeKey = "wuqian2019";

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private DataSource dataSource;
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //配置从内存加载认证信息
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder().encode("123456"))
                .roles("beijingAdmin","shanghaiAdmin");

        auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder().encode("123456"))
                .roles("beijingUser","shanghaiUser");
    }*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()//定义那些URL需要被维护、哪些不需要被维护
                .antMatchers("/login").permitAll()//设置所有人可以访问登录页面
                .antMatchers("/","/index").permitAll()//设置所有人可以访问登录页面
                .antMatchers("/test/**","/test1/**").permitAll()//test,test1下所有都可以访问
                .antMatchers("/res/**/*.{js,html}").permitAll()//比如要把/res/的所有.js,html设置为白名单：
                .antMatchers("/haha/**").access("hasRole('admin')")
                .withObjectPostProcessor(new MyObjectPostProcessor())
                .anyRequest().authenticated()//任何请求，登陆后可以访问
//                .anyRequest().access("@authService.canAccess(request,authentication)")
                .and()
                .formLogin().loginPage("/login")
                //默认就是1209600秒，即2周
//                .and().rememberMe().key(rememberMeKey).rememberMeServices(rememberMeServices())
                .and().rememberMe().tokenRepository(tokenRepository()).tokenValiditySeconds(1209600).userDetailsService(customUserDetailService)
//                .and()
//                .addFilterBefore(new BeforeLoginFilter(), UsernamePasswordAuthenticationFilter.class)
//                .addFilterAfter(new AfterLoginFilter(),UsernamePasswordAuthenticationFilter.class)
//                .addFilterAt(new AtLoginFilter(),UsernamePasswordAuthenticationFilter.class)
                ;
//        super.configure(http);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource(){
        return new MyFilterInvocationSecurityMetadataSource();
    }
    @Bean
    public MyAccessDecisionManager accessDecisionManager(){
        return new MyAccessDecisionManager();
    }
    private class MyObjectPostProcessor implements ObjectPostProcessor<FilterSecurityInterceptor>{

        @Override
        public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
            fsi.setSecurityMetadataSource(filterInvocationSecurityMetadataSource());
            fsi.setAccessDecisionManager(accessDecisionManager());
            return fsi;
        }
    }

    @Bean
    public RememberMeServices rememberMeServices() {
        System.out.println("WebSecurityConfig.tokenBasedRememberMeServices()="+customUserDetailService);
        TokenBasedRememberMeServices tbrms = new TokenBasedRememberMeServices(rememberMeKey, customUserDetailService);
        // [可选]需要配置cookie的过期时间，默认过时时间1209600秒，即2个星期。这里设置cookie过期时间为1天
        tbrms.setTokenValiditySeconds(60 * 60 * 24 * 1);

        // 设置checkbox的参数名为rememberMe（默认为remember-me），
        //注意如果是ajax请求，参数名不是checkbox的name而是在ajax的data里
        //tbrms.setParameter("rememberMe");
        return tbrms;
    }

    // tokenRepository（）的实现代码如下：
    @Bean
    public PersistentTokenRepository tokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl =new JdbcTokenRepositoryImpl();
        jdbcTokenRepositoryImpl.setDataSource(dataSource);
        //自动创建数据库表:persistent_logins，使用一次后注释掉，不然会报错
//        jdbcTokenRepositoryImpl.setCreateTableOnStartup(true);
        return jdbcTokenRepositoryImpl;
    }
}





















