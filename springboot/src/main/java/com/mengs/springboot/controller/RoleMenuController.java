package com.mengs.springboot.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mengs.springboot.common.Result;
import com.mengs.springboot.service.RoleMenuService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

import com.mengs.springboot.entity.RoleMenu;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 权限菜单表 前端控制器
 * </p>
 *
 * @author MS
 * @since 2022-03-19
 */
@RestController
@RequestMapping("/role-menu")
public class RoleMenuController {

    @Resource
    private RoleMenuService roleMenuService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody RoleMenu roleMenu) {
        roleMenuService.saveOrUpdate(roleMenu);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        roleMenuService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        roleMenuService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(roleMenuService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(roleMenuService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(roleMenuService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

}

