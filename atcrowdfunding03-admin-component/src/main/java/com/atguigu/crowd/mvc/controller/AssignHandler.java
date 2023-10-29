package com.atguigu.crowd.mvc.controller;

import com.atguigu.crowd.entity.Auth;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.api.AuthService;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * description: add a description
 *
 * @author Lemon
 * @version 1.0.0
 * @date 2023/06/08 13:12:05
 */
@Controller
public class AssignHandler {
    @Autowired
    private AdminService as;
    @Autowired
    private RoleService rs;
    @Autowired
    private AuthService ats;

    @RequestMapping("assign/to/assign/role/page.html")
    public String toAssignRolePage(
            @RequestParam("adminId") Integer adminId,
//            @RequestParam("pageNum") Integer pageNum,
//            @RequestParam("keyWord") String keyword,
            ModelMap modelMap) {
        //查询已分配的角色
        List<Role> assigned = rs.getAssignedRole(adminId);
        //查询未分配的角色
        List<Role> unAssigned = rs.getUnAssignedRole(adminId);
        modelMap.addAttribute("assigned", assigned);
        modelMap.addAttribute("unAssigned", unAssigned);
        return "assign-role";
    }

    @RequestMapping("assign/do/role/assign.html")
    public String saveAdminRelationship(
            @RequestParam("adminId") Integer adminId,
            @RequestParam("keyWord") String keyWord,
            @RequestParam("pageNum") Integer pageNum,
            @RequestParam(value = "roleIdList", required = false) List<Integer> list
    ) {
        as.saveRelationship(adminId, list);
        return "redirect:/admin/get/page.html?pageNum=" + pageNum + "&keyWord=" + keyWord;
    }

    @ResponseBody
    @RequestMapping("assgin/get/all/auth.json")
    public ResultEntity<List<Auth>> getAllAuth() {
        return ResultEntity.successWithData(ats.getAll());
    }

    @ResponseBody
    @RequestMapping("assign/get/assigned/auth/id/by/role/id.json")
    public ResultEntity<List<Integer>> getAssignedAuth(
            Integer roleId
    ) {
        return ResultEntity.successWithData(ats.getAuthByRoleId(roleId));
    }

    @ResponseBody
    @RequestMapping("assign/do/role/assign/auth.json")
    public ResultEntity<String> saveRoleRelationship(
            @RequestBody Map<String, List<Integer>> map
    ) {
        ats.saveRoleRelationship(map.get("roleId").get(0), map.get("authIdArray"));
        return ResultEntity.successWithoutData();
    }
}
