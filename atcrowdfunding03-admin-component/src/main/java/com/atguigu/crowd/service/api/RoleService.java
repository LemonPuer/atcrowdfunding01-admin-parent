package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * description: add a description
 *
 * @author Lemon
 * @version 1.0.0
 * @date 2023/06/04 14:19:46
 */
public interface RoleService {
    //分页显示
    PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword);

    //添加角色
    void saveRole(Role role);

    //修改角色
    void updateRole(Role role);

    //删除角色
    void deleteRole(List<Integer> idList);

    List<Role> getAssignedRole(Integer adminId);

    List<Role> getUnAssignedRole(Integer adminId);
}
