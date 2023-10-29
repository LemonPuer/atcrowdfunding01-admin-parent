package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.AuthExample;
import com.atguigu.crowd.mapper.AuthMapper;
import com.atguigu.crowd.service.api.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description: add a description
 *
 * @author Lemon
 * @version 1.0.0
 * @date 2023/06/09 13:33:21
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthMapper am;

    @Override
    public List<Auth> getAll() {
        return am.selectByExample(new AuthExample());
    }

    @Override
    public List<Integer> getAuthByRoleId(Integer roleId) {
        return am.selectAuthIdByRoleId(roleId);
    }

    @Override
    public void saveRoleRelationship(Integer roleId, List<Integer> list) {
        am.deleteRelationShip(roleId);
        if (list != null && list.size() > 0 )
            am.insertRoleRelationship(roleId, list);
    }

    @Override
    public List<String> getAuthByAdminId(Integer adminId) {
        return am.selectAuthByAdminId(adminId);
    }
}
