package com.example.usermanagement.controller;

import com.example.usermanagement.common.Result;
import com.example.usermanagement.entity.SysMenu;
import com.example.usermanagement.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public Result<List<SysMenu>> list() {
        List<SysMenu> menus = menuService.listMenus();
        return Result.success(menus);
    }

    @GetMapping("/{id}")
    public Result<SysMenu> getById(@PathVariable Long id) {
        SysMenu menu = menuService.getMenuById(id);
        return Result.success(menu);
    }

    @PostMapping
    public Result<SysMenu> create(@RequestBody SysMenu menu) {
        SysMenu created = menuService.createMenu(menu);
        return Result.success("创建成功", created);
    }

    @PutMapping
    public Result<SysMenu> update(@RequestBody SysMenu menu) {
        SysMenu updated = menuService.updateMenu(menu);
        return Result.success("更新成功", updated);
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return Result.success("删除成功");
    }
}
