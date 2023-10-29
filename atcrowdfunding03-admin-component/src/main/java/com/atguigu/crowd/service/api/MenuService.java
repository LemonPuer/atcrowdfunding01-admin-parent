package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Menu;

import java.util.List;

/**
 * description: add a description
 *
 * @author Lemon
 * @version 1.0.0
 * @date 2023/06/07 11:37:06
 */
public interface MenuService {
    List<Menu> getAll();
    void saveMenu(Menu menu);
    void updateMenu(Menu menu);
    void deleteMenu(int id);
}
