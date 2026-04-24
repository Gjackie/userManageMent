package com.example.usermanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usermanagement.entity.SysMenu;

import java.util.List;

public interface MenuService {
    List<SysMenu> listMenus();
    SysMenu getMenuById(Long id);
    SysMenu createMenu(SysMenu menu);
    SysMenu updateMenu(SysMenu menu);
    void deleteMenu(Long id);
}
