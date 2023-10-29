package com.atguigu.crowd.mvc.controller;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.impl.AdminServiceImpl;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author Lemon
 * @create 2023-02-17-15:41
 */
@Controller
public class AdminHandller {
    @Autowired
    private AdminService as;

    @PreAuthorize("hasAuthority('user:delete')")
    @RequestMapping("delete/admin.html")
    public String delAdmin(
            @RequestParam("pageNum")Integer pageNum,
            @RequestParam(value = "keyWord", defaultValue = "") String keyWord,
            @RequestParam("adminId")Integer id){
        as.delAdmain(id);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyWord="+keyWord;
    }

    @RequestMapping("update/admin.html")
    public String updateAdmin(Integer pageNum, String keyWord,Admin admin){
        as.editAdmain(admin);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyWord="+keyWord;
    }

    @RequestMapping("admin/to/edit/page.html")
    public String  toEditPage(
            @RequestParam("adminId")Integer adminId,
            @RequestParam("pageNum")Integer pageNum,
            @RequestParam(value = "keyWord", defaultValue = "") String keyWord,
            ModelMap modelMap){
        Admin admin = as.getAdminById(adminId);
        modelMap.addAttribute("admin",admin);
        modelMap.addAttribute("pageNum",pageNum);
        modelMap.addAttribute("keyWord",keyWord);
        return "admin-edit";
    }

    @RequestMapping("save/admin.html")
    public String saveAdmin(Admin admin){
        as.saveUser(admin);
        //防止刷新重复提交表单
        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }
    //引入SpringSecurity后此方法就没用了
    @RequestMapping("admin/do/login.html")
    public String doLogin(@RequestParam("loginAcct") String loginAcct, @RequestParam("userPswd") String userPswd, HttpSession session) {
        Admin admin = as.getAdminByLogin(loginAcct, userPswd);
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
        return "redirect:/admin/to/main/page.html";
    }

    @RequestMapping("admin/do/logout.html")
    public String doLogout(HttpSession session) {
        // 强制Session失效
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }
//    @PreAuthorize("hasRole('经理')")
    @RequestMapping("admin/get/page.html")
    public String getPageInfo(
            @RequestParam(value = "keyWord", defaultValue = "") String keyWord,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            ModelMap modelMap
    ) {
        PageInfo<Admin> pageInfo = as.getPageInfo(keyWord, pageNum, pageSize);
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,pageInfo);
        return "admin-page";
    }
}
