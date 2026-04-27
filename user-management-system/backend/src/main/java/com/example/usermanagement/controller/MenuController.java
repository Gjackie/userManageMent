package com.example.usermanagement.controller;

import com.example.usermanagement.common.Result;
import com.example.usermanagement.entity.SysMenu;
import com.example.usermanagement.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理控制器
 *
 * 提供菜单相关的 RESTful API 接口。
 *
 * 接口列表：
 * - GET    /api/menus/list     - 获取菜单列表
 * - GET    /api/menus/{id}    - 获取菜单详情
 * - POST   /api/menus         - 创建菜单
 * - PUT    /api/menus         - 更新菜单
 * - DELETE /api/menus/{id}    - 删除菜单
 *
 * @see MenuService 菜单服务
 */
@RestController
@RequestMapping("/api/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 获取菜单列表
     */
    @GetMapping("/list")
    public Result<List<SysMenu>> list() {
        List<SysMenu> menus = menuService.listMenus();
        return Result.success(menus);
    }

    /**
     * 获取菜单详情
     */
    @GetMapping("/{id}")
    public Result<SysMenu> getById(@PathVariable Long id) {
        SysMenu menu = menuService.getMenuById(id);
        return Result.success(menu);
    }

    /**
     * 创建菜单
     */
    @PostMapping
    public Result<SysMenu> create(@RequestBody SysMenu menu) {
        SysMenu created = menuService.createMenu(menu);
        return Result.success("创建成功", created);
    }

    /**
     * 更新菜单
     */
    @PutMapping
    public Result<SysMenu> update(@RequestBody SysMenu menu) {
        SysMenu updated = menuService.updateMenu(menu);
        return Result.success("更新成功", updated);
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return Result.success("删除成功");
    }
}
