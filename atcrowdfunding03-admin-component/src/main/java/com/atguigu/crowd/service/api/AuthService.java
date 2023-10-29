package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Auth;

import java.util.List;

/**
 * description: add a description
 *
 * @author Lemon
 * @version 1.0.0
 * @date 2023/06/09 13:33:03
 */
public interface AuthService {
    List<Auth> getAll();

    List<Integer> getAuthByRoleId(Integer roleId);

    void saveRoleRelationship(Integer roleId, List<Integer> list);

    List<String> getAuthByAdminId(Integer adminId);
}
