package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.mvc.config.SecurityAdmin;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * description: add a description
 *
 * @author Lemon
 * @version 1.0.0
 * @date 2023/06/19 12:00:51
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AdminService as;
    @Autowired
    private RoleService rs;
    @Autowired
    private AuthService aus;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Admin admin = as.getAdminByLoginAcct(s);
        List<Role> roles = rs.getAssignedRole(admin.getId());
        List<String> authNames = aus.getAuthByAdminId(admin.getId());
        List<GrantedAuthority> list=new ArrayList<>();
        for(Role r:roles){
            list.add(new SimpleGrantedAuthority("ROLE_"+r.getName()));
        }
        for(String a:authNames){
            list.add(new SimpleGrantedAuthority(a));
        }
        return new SecurityAdmin(admin, list);
    }
}
