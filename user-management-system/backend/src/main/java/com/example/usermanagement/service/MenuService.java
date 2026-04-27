package com.example.usermanagement.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.usermanagement.entity.SysMenu;

import java.util.List;

/**
 * 菜单服务接口
 *
 * 定义菜单管理的业务逻辑方法。
 *
 * @see com.example.usermanagement.service.impl.MenuServiceImpl
 */
public interface MenuService {

    /**
     * 获取所有菜单列表
     *
     * @return 菜单列表
     */
    List<SysMenu> listMenus();

    /**
     * 根据ID获取菜单详情
     *
     * @param id 菜单ID
     * @return 菜单对象
     */
    SysMenu getMenuById(Long id);

    /**
     * 创建新菜单
     *
     * @param menu 菜单对象
     * @return 创建的菜单对象
     */
    SysMenu createMenu(SysMenu menu);

    /**
     * 更新菜单
     *
     * @param menu 菜单对象
     * @return 更新后的菜单对象
     */
    SysMenu updateMenu(SysMenu menu);

    /**
     * 删除菜单（逻辑删除）
     *
     * @param id 菜单ID
     */
    void deleteMenu(Long id);
}
