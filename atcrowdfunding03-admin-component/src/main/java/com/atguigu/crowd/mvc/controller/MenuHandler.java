package com.atguigu.crowd.mvc.controller;

import com.atguigu.crowd.entity.Menu;
import com.atguigu.crowd.service.api.MenuService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: add a description
 *
 * @author Lemon
 * @version 1.0.0
 * @date 2023/06/07 11:43:26
 */
@RestController
public class MenuHandler {
    @Autowired
    public MenuService ms;

    @RequestMapping("menu/get/whole/tree.json")
    public ResultEntity<Menu> getWholeTreeNew() {
        List<Menu> all = ms.getAll();
        Menu root = null;
        Map<Integer, Menu> map = new HashMap<>();
        for (Menu m : all) {
            map.put(m.getId(), m);
        }
        for (Menu m : all) {
            if (m.getPid() == null) {
                root = m;
                continue;
            }
            Menu father = map.get(m.getPid());
            father.getChildren().add(m);
        }
        return ResultEntity.successWithData(root);
    }

    @RequestMapping("menu/save.json")
    public ResultEntity<String> saveMenu(Menu menu) {
        ms.saveMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("menu/update.json")
    public ResultEntity<String> updateMenu(Menu menu) {
        ms.updateMenu(menu);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("menu/remove.json")
    public ResultEntity<String> deleteMenu(@RequestParam("id") Integer id) {
        ms.deleteMenu(id);
        return ResultEntity.successWithoutData();
    }
}
