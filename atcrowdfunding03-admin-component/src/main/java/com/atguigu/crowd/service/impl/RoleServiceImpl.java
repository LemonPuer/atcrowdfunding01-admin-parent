package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.entity.RoleExample;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * description: add a description
 *
 * @author Lemon
 * @version 1.0.0
 * @date 2023/06/04 14:21:47
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleMapper rm;

    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {
        //1.开启分页功能
        PageHelper.startPage(pageNum, pageSize);
        //2.查询数据
        List<Role> roles = rm.selectRoleByKeyword(keyword);
        //3.封装为PageInfo对象
        return new PageInfo<>(roles);
    }

    @Override
    public void saveRole(Role role) {
        rm.insert(role);
    }

    @Override
    public void updateRole(Role role) {
        rm.updateByPrimaryKey(role);
    }

    @Override
    public void deleteRole(List<Integer> idList) {
        RoleExample re = new RoleExample();
        re.createCriteria().andIdIn(idList);
        rm.deleteByExample(re);
    }

    @Override
    public List<Role> getAssignedRole(Integer adminId) {
        return rm.selectAssignedRole(adminId);
    }

    @Override
    public List<Role> getUnAssignedRole(Integer adminId) {
        List<Role> all = rm.selectByExample(new RoleExample());
        List<Role> assignedRole = getAssignedRole(adminId);
        all.removeAll(assignedRole);
        return all;
    }
}
