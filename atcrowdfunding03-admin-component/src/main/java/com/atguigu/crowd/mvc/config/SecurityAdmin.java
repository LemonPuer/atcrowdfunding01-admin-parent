package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * description: add a description
 *
 * @author Lemon
 * @version 1.0.0
 * @date 2023/06/19 13:46:43
 */
public class SecurityAdmin extends User {
    private Admin originalAdmin;

    /**
     * @param admin       传入原始的Admin对象，方便获取
     * @param authorities 创建角色、权限信息的集合
     */
    public SecurityAdmin(Admin admin, Collection<? extends GrantedAuthority> authorities) {
        super(admin.getLoginAcct(), admin.getUserPswd(), authorities);
        this.originalAdmin = admin;
        this.originalAdmin.setUserPswd(null);
    }

    public Admin getOriginalAdmin() {
        return originalAdmin;
    }
}
