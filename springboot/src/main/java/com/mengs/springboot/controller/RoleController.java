package com.mengs.springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengs.springboot.common.Result;
import com.mengs.springboot.entity.User;
import com.mengs.springboot.service.RoleService;
import com.mengs.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.mengs.springboot.entity.Role;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author MS
 * @since 2022-03-19
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Role role) {
        User currentUser = TokenUtils.getCurrentUser();
        if (currentUser.getRoleId() == 1) {
            roleService.saveOrUpdate(role);
            return Result.success();
        }
        return Result.error("您无权操作角色管理!");
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        return roleService.delRoleInfo(id);
    }

    @GetMapping
    public Result findAll() {
        return Result.success(roleService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(roleService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        try {
            User currentUser = TokenUtils.getCurrentUser();
            if (currentUser.getRoleId() != 1) {
                return Result.error("您无权访问角色管理!");
            }
            QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("name", name);
            queryWrapper.orderByAsc("role_id");
            return Result.success(roleService.page(new Page<>(pageNum, pageSize), queryWrapper));
        } catch (Exception e) {
            return Result.error("系统开小差了!");
        }
    }

    /**
     * 绑定角色和菜单的关系
     * @param roleId 角色id
     * @param menuIds 菜单id数组
     * @return
     */
    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId, @RequestBody List<Integer> menuIds) {
        roleService.setRoleMenu(roleId, menuIds);
        return Result.success();
    }

    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId) {
        return Result.success( roleService.getRoleMenu(roleId));
    }

}

