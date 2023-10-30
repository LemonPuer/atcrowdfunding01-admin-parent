package com.atguigu.crowd.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * description: add a description
 *
 * @author Lemon
 * @version 1.0.0
 * @date 2023/06/17 16:46:32
 */
@Configuration
@EnableWebSecurity
// 启用全局方法权限管理功能
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CrowdfundingSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService uds;
    @Autowired
    private PasswordEncoder pe;

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("tom").password("123456").roles("admin");
        auth.userDetailsService(uds).passwordEncoder(pe);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/bootstrap/**", "/crowd/**", "/crowd/**", "/css/**", "/fonts/**", "/img/**", "/jquery/**", "/layer/**", "/script/**", "/ztree/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/admin/to/login/page.html")
                .permitAll()
                // 过往的控制器方法不再使用
                .loginProcessingUrl("/security/do/login.html")
                .permitAll()
                .usernameParameter("loginAcct")
                .passwordParameter("userPswd")
                .defaultSuccessUrl("/admin/to/main/page.html")
                .and()
                .logout()
                .logoutUrl("/security/do/logout.html")
                .logoutSuccessUrl("/admin/to/login/page.html")
                .and()
                .csrf()
                .disable();
    }
}
