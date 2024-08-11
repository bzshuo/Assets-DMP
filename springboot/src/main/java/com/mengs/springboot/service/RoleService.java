package com.mengs.springboot.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengs.springboot.common.Result;
import com.mengs.springboot.entity.Menu;
import com.mengs.springboot.entity.Role;
import com.mengs.springboot.entity.RoleMenu;
import com.mengs.springboot.mapper.MenuMapper;
import com.mengs.springboot.mapper.RoleMapper;
import com.mengs.springboot.mapper.RoleMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService extends ServiceImpl<RoleMapper, Role> {
    @Resource
    RoleMenuMapper roleMenuMapper;

    @Resource
    MenuMapper menuMapper;

    @Autowired
    MenuService menuService;

    @Transactional
    public void setRoleMenu(Integer roleId, List<Integer> menuIds) {
//        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("role_id", roleId);
//        roleMenuMapper.delete(queryWrapper);

        // 先删除当前角色id所有的绑定关系
        roleMenuMapper.deleteByRoleId(roleId);

        // 再把前端传过来的菜单id数组绑定到当前的这个角色id上去
        List<Integer> menuIdsCopy = CollUtil.newArrayList(menuIds);
        for (Integer menuId : menuIds) {
            Menu menu = menuService.getById(menuId);
//            if (menu.getPid() != null && !menuIdsCopy.contains(menu.getPid())) { // 二级菜单 并且传过来的menuId数组里面没有它的父级id
//                // 那么我们就得补上这个父级id
//                RoleMenu roleMenu = new RoleMenu();
//                roleMenu.setRoleId(roleId);
//                roleMenu.setMenuId(menu.getPid());
//                roleMenuMapper.insert(roleMenu);
//                menuIdsCopy.add(menu.getPid());
//            }
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.insert(roleMenu);
        }
    }

    public List<Integer> getRoleMenu(Integer roleId) {

        return roleMenuMapper.selectByRoleId(roleId);
    }

    public Result delRoleInfo(Integer id) {
        try {
            boolean b = removeById(id);
            return Result.success("删除成功！");
        } catch (Exception e) {
            System.out.println(e.toString());
            return Result.error("该角色已被赋予用户，不可删除！");
        }
    }
}
