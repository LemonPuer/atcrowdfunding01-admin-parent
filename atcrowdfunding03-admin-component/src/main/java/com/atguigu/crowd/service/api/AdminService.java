package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Admin;
import com.github.pagehelper.PageInfo;
import java.util.List;

/**
 * @author Lemon
 * @create 2023-02-19-10:26
 */
public interface AdminService {
    //根据id删除用户
    void delAdmain(Integer id);
    //根据id修改用户
    void editAdmain(Admin admin);
    //根据id查询用户
    Admin getAdminById(Integer id);
    //检查登录账号
    Admin getAdminByLogin(String account, String password);
    //添加账号
    void saveUser(Admin admin);
    //得到全部记录
    List<Admin> getAll();
    //得到页面信息用于分页
    PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize);
}
