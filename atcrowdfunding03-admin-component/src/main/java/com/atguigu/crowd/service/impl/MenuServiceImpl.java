package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Menu;
import com.atguigu.crowd.entity.MenuExample;
import com.atguigu.crowd.mapper.MenuMapper;
import com.atguigu.crowd.service.api.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description: add a description
 *
 * @author Lemon
 * @version 1.0.0
 * @date 2023/06/07 11:41:00
 */
@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper mm;

    @Override
    public List<Menu> getAll() {
        return mm.selectByExample(new MenuExample());
    }

    @Override
    public void saveMenu(Menu menu) {
        mm.insert(menu);
    }

    @Override
    public void updateMenu(Menu menu) {
        //防止将pid置空
        mm.updateByPrimaryKeySelective(menu);
    }

    @Override
    public void deleteMenu(int id) {
        mm.deleteByPrimaryKey(id);
    }
}
