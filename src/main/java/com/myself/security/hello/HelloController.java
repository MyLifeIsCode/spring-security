package com.myself.security.hello;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public String getWelcomeMsg(){
        return "HEllo,Spring Security";
    }

    @GetMapping("/helloAdmin")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String helloAdmin(){
        return "hello,admin";
    }

    @GetMapping("/helloUser")
//    @PreAuthorize("hasRole('ROLE_NORMAL')")
    public String helloUser(){
        return "Hello,user";
    }
}
