package com.myself.security.config;

import com.myself.security.filter.AfterLoginFilter;
import com.myself.security.filter.AtLoginFilter;
import com.myself.security.filter.BeforeLoginFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                .anyRequest().authenticated()//任何请求，登陆后可以访问
                .and()
                .formLogin().loginPage("/login")
                .and()
                .addFilterBefore(new BeforeLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new AfterLoginFilter(),UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new AtLoginFilter(),UsernamePasswordAuthenticationFilter.class)
                ;
//        super.configure(http);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
