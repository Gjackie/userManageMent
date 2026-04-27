package com.example.usermanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.usermanagement.entity.SysMenu;
import com.example.usermanagement.mapper.SysMenuMapper;
import com.example.usermanagement.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 菜单服务实现类
 *
 * 实现 MenuService 接口，处理菜单相关的业务逻辑。
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> listMenus() {
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getDeleted, 0);
        wrapper.orderByAsc(SysMenu::getSort);
        return sysMenuMapper.selectList(wrapper);
    }

    @Override
    public SysMenu getMenuById(Long id) {
        SysMenu menu = sysMenuMapper.selectOne(
                new LambdaQueryWrapper<SysMenu>()
                        .eq(SysMenu::getId, id)
                        .eq(SysMenu::getDeleted, 0)
        );
        if (menu == null) {
            throw new RuntimeException("菜单不存在");
        }
        return menu;
    }

    @Override
    public SysMenu createMenu(SysMenu menu) {
        menu.setCreateTime(LocalDateTime.now());
        menu.setUpdateTime(LocalDateTime.now());
        menu.setDeleted(0);
        sysMenuMapper.insert(menu);
        return menu;
    }

    @Override
    public SysMenu updateMenu(SysMenu menu) {
        SysMenu existing = getMenuById(menu.getId());
        if (StringUtils.hasText(menu.getMenuName())) {
            existing.setMenuName(menu.getMenuName());
        }
        if (menu.getParentId() != null) {
            existing.setParentId(menu.getParentId());
        }
        if (StringUtils.hasText(menu.getPath())) {
            existing.setPath(menu.getPath());
        }
        if (StringUtils.hasText(menu.getComponent())) {
            existing.setComponent(menu.getComponent());
        }
        if (StringUtils.hasText(menu.getPermission())) {
            existing.setPermission(menu.getPermission());
        }
        if (menu.getType() != null) {
            existing.setType(menu.getType());
        }
        if (menu.getSort() != null) {
            existing.setSort(menu.getSort());
        }
        if (StringUtils.hasText(menu.getIcon())) {
            existing.setIcon(menu.getIcon());
        }
        existing.setUpdateTime(LocalDateTime.now());
        sysMenuMapper.updateById(existing);
        return existing;
    }

    @Override
    public void deleteMenu(Long id) {
        SysMenu menu = getMenuById(id);
        menu.setDeleted(1);
        menu.setUpdateTime(LocalDateTime.now());
        sysMenuMapper.updateById(menu);
    }
}
