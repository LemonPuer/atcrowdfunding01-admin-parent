package com.atguigu.crowd.mvc.controller;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * description: add a description
 *
 * @author Lemon
 * @version 1.0.0
 * @date 2023/06/04 14:34:44
 */
@RestController
public class RoleHandler {
    @Autowired
    RoleService rs;

    @PreAuthorize("hasAnyRole('root','部长')")
    @RequestMapping("role/get/page/info.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "keyword", defaultValue = "") String keyword
    ) {
        PageInfo<Role> pageInfo = rs.getPageInfo(pageNum, pageSize, keyword);
        return ResultEntity.successWithData(pageInfo);
    }

    @RequestMapping("role/save.json")
    public ResultEntity<String> saveRole(Role role) {
        rs.saveRole(role);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("role/update.json")
    public ResultEntity<String> updateRole(Role role) {
        rs.updateRole(role);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("role/delete.json")
    public ResultEntity<String> deleteRole(@RequestBody List<Integer> idList) {
        rs.deleteRole(idList);
        return ResultEntity.successWithoutData();
    }
}
