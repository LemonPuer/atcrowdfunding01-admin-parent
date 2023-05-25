package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Lemon
 * @create 2023-02-19-10:34
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper mapper;

    @Override
    public void delAdmain(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void editAdmain(Admin admin) {
        Admin old = mapper.selectByPrimaryKey(admin.getId());
        if (admin.getUserPswd() == null) {
            admin.setCreateTime(old.getCreateTime());
            admin.setUserPswd(old.getUserPswd());
        }
        mapper.updateByPrimaryKey(admin);
    }

    @Override
    public Admin getAdminById(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * description 验证登录信息
     *
     * @return Admin
     * @author Lemon
     */
    @Override
    public Admin getAdminByLogin(String account, String password) {
        AdminExample example = new AdminExample();
        example.createCriteria().andLoginAcctEqualTo(account);
        List<Admin> list = mapper.selectByExample(example);
        if (list == null || list.size() == 0) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        if (list.size() > 1) {
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        Admin admin = list.get(0);
        if (admin == null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        String s = CrowdUtil.md5(password);
        if (!admin.getUserPswd().equalsIgnoreCase(s))
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        return admin;
    }

    /**
     * description 添加用户
     *
     * @return int
     * @author Lemon
     */
    @Override
    public void saveUser(Admin admin) {
        Date date = new Date();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        String format = sd.format(date);
        admin.setCreateTime(format);
        admin.setUserPswd(CrowdUtil.md5(admin.getUserPswd()));
        try {
            mapper.insert(admin);
        } catch (Exception e) {
            //账号重复导致异常
            if (e instanceof DuplicateKeyException) {
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
            //其它异常直接抛出
            throw e;
        }
    }

    @Override
    public List<Admin> getAll() {
        return mapper.selectByExample(null);
    }

    /**
     * description 搜索指定信息
     *
     * @return PageInfo<Admin>
     * @author Lemon
     */
    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Admin> admins = mapper.selectAdminPage(keyword);
        return new PageInfo<>(admins);
    }
}
